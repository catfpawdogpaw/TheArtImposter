const redisModule = require("redis");
require("dotenv").config();
const { PlayerDTO } = require("../model/gameDTO");

// Redis 클라이언트 생성
const redis = redisModule.createClient({
    socket: {
        host: `${process.env.REDIS_HOST}`,
        port: process.env.REDIS_PORT,
        connectTimeout: process.env.REDIS_TIMEOUT,
    },
    password: process.env.REDIS_PASSWORD,
});

// Redis 클라이언트 오류 처리
redis.on("error", (err) => {
    console.error("Redis error:", err);
});

// 클라이언트가 성공적으로 연결되었을 때
redis.on("connect", () => {
    console.log("Connected to Redis server");
});

async function findRedisDataByKey(key) {
    try {
        const exists = await redis.exists(key);
        if (!exists) {
            console.log(`키: ${key} 를 찾을 수 없습니다.`);
            return null;
        }

        const result = await redis.hGetAll(key);
        console.log(`데이터를 찾았습니다. ${key}:`, result);
        return result;
    } catch (err) {
        throw err;
    }
}

async function validateToken(accessToken, socket) {
    try {
        // Redis에서 토큰으로 사용자 정보 조회
        const userInfo = await redis.hGetAll(accessToken);
        if (!userInfo || Object.keys(userInfo).length === 0) {
            console.log(`존재하지 않는 토큰: ${accessToken} `);
            socket.emit("error", { message: "유효하지 않은 토큰입니다." });
            socket.disconnect();
            return null;
        }

        //userid를 키로하고 refreshToken필드를검사하는경우
        // const userInfo1 = await redis.hGetAll(userid);
        // if (!userInfo || Object.keys(userInfo).length === 0) {
        //     console.log(`존재하지 않는 토큰: ${accessToken} `);
        //     socket.emit("error", { message: "유효하지 않은 아이디입니다." });
        //     socket.disconnect();
        //     return null;
        //     if (userInfo1.refreshToken != refreshToken) {
        //         socket.emit("error", { message: "유효하지 않은 토큰입니다." });
        //         socket.disconnect();
        //         return null;
        //     }
        // }

        // PlayerDTO 객체 생성 및 반환
        return new PlayerDTO(
            parseInt(userInfo.userId),
            userInfo.nickname,
            userInfo.profileImage,
            parseInt(userInfo.vicCnt),
            parseInt(userInfo.gameCnt)
        );
    } catch (err) {
        console.error("Token validation error:", err);
        socket.emit("error", { message: "서버 오류가 발생했습니다." });
        socket.disconnect();
        return null;
    }
}

async function updateRedisRoomStatus(gameRoomStatus) {
    try {
        const serializedStatus = {
            roundResults: JSON.stringify(gameRoomStatus.roundResults),
            players: JSON.stringify(gameRoomStatus.players),
            gameRoomId: gameRoomStatus.gameRoomId,
            gameRoomTitle: gameRoomStatus.gameRoomTitle,
            playerCount: gameRoomStatus.playerCount,
            currentRound: gameRoomStatus.currentRound,
            currentTurnIndex: gameRoomStatus.currentTurnIndex,
            minPeople: gameRoomStatus.gameSetting.minPeople,
            maxPeople: gameRoomStatus.gameSetting.maxPeople,
            drawingData: JSON.stringify(gameRoomStatus.drawingData),
            subjects: JSON.stringify(gameRoomStatus.subjects),
            gameSetting: JSON.stringify(gameRoomStatus.gameSetting),
            startTime: gameRoomStatus.startTime.toISOString(),
            roundStartTime: gameRoomStatus.roundStartTime.toISOString(),
        };

        await redis.hSet(
            "gameroom:" + gameRoomStatus.gameRoomId,
            serializedStatus
        );
        redis.expire("gameroom:" + gameRoomStatus.gameRoomId, 1800);
    } catch (err) {
        console.error("방을 업데이트 하는중 에러발생", err);
    }
}

// 클라이언트를 내보내기
module.exports = {
    redis,
    findRedisDataByKey,
    validateToken,
    updateRedisRoomStatus,
};
