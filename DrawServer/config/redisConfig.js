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

// 클라이언트를 내보내기
module.exports = { redis, findRedisDataByKey, validateToken };
