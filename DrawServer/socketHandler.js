const drawingHandler = require("./socket/drawingHandler");

//GameRoomStatus 가 담긴 객체
const Rooms = {};

// key:방번호
// userid: tokken
//userid: tokken

function setupSocket(io) {
    io.on("connection", (socket) => {
        console.log("A user connected");

        socket.on("joinRoom", (accessToken) => {
            // redis jwt토큰 있는지 검증후 해당유저정보 가져오기
            // redis에서 유효한 방인지 검증
            // playerId = validate(accessToken);
            const playerId = 2;
            const roomId = 1;
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

        drawingHandler(io, socket);

        socket.on("disconnect", () => {
            console.log("User disconnected");
        });
    });
}

module.exports = { setupSocket, Rooms };
