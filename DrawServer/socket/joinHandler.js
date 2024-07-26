const redis = require("../config/redisConfig");

function joinHandler(io, socket) {
    socket.on("joinRoom", (accessToken, roomId) => {
        // redis jwt토큰 있는지 검증후 해당유저정보 가져오기
        // redis에서 유효한 방인지 검증
        const player = validateToken(accessToken);

        // 생성된 GameRoomStatus의 gameRoomId에 socket.join(gameRoomId)
        // + player의 socketid 설정
        const gameRoomStatus = Rooms[roomId];
        if (!gameRoomStatus) {
            socket.emit("error", { message: "해당하는 방이 없습니다." });
            return;
        }
        const playerIndex = gameRoomStatus.players.findIndex(
            (player) => player.playerId === playerId
        );
        const scoketId = socket.id;
        playerIndex = scoketId;
        socket.join(roomId);
    });
}

function validateToken(accessToken) {
    return player;
}

module.exports = joinHandler;
