const drawingHandler = require("./drawingHandler");
const { getGameRoomStatus } = require("./roomHandler");
const { testPlayerDTO } = require("../model/gameDTO");
const { validateToken } = require("../config/redisConfig");

async function joinHandler(io, socket) {
    socket.on("joinRoom", (roomId, accessToken) => {
        // redis jwt토큰 있는지 검증후 해당유저정보 가져오기
        // redis에서 유효한 방인지 검증
        // const player = await validateToken(accessToken, socket);
        const testplayer = testPlayerDTO();

        // 생성된 GameRoomStatus의 gameRoomId에 socket.join(gameRoomId)
        // + player의 socketid 설정
        const gameRoomStatus = getGameRoomStatus(roomId);
        console.log(gameRoomStatus);
        if (!gameRoomStatus) {
            socket.emit("error", { message: "해당하는 방이 없습니다." });
            console.log(`${roomId}에 해당하는 방이 없습니다`);
            socket.disconnect();
            return;
        }

        gameRoomStatus.addPlayer(testplayer);

        console.log(gameRoomStatus.gameRoomTitle + "접속");
        console.log(
            `현재 인원: ${JSON.stringify(gameRoomStatus.players, null, 2)}`
        );

        const scoketId = socket.id;
        playerIndex = scoketId;
        socket.join(roomId);
        drawingHandler(io, socket, gameRoomStatus);
        return gameRoomStatus;
    });
}

module.exports = joinHandler;
