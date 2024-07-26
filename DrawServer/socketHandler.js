const drawingHandler = require("./socket/drawingHandler");
const joinHandler = require("./socket/joinHandler");
//GameRoomStatus 가 담긴 객체
const Rooms = {};
const gameNamespaces = {};

function setupSocket(io) {
    function createGameNamespace(roomId) {
        if (gameNamespaces[roomId]) {
            console.log(`${roomId} 네임스페이스가 이미 존재합니다.`);
            return gameNamespaces[roomId];
        }

        const nsp = io.of(`/${roomId}`);

        nsp.on("connection", (socket) => {
            console.log(`클라이언트가 ${roomId} 네임스페이스에 연결됨`);

            joinHandler(nsp, socket, roomId);
            drawingHandler(nsp, socket, roomId);

            socket.on("disconnect", () => {
                console.log(
                    `클라이언트가 ${roomId} 네임스페이스에서 연결 해제됨`
                );
            });
        });

        gameNamespaces[roomId] = nsp;
        console.log(`${roomId} 네임스페이스 생성됨`);
        return nsp;
    }
}

app.post("/create-room", (req, res) => {
    const roomId = req.body.roomId; // 스프링에서 받은 방 ID
    const nsp = createGameNamespace(roomId);
    res.json({ message: `Room ${roomId} created` });
});

//게임이 끝났을 때 스프링에 전달구현

module.exports = { setupSocket, Rooms };
