//그림 데이터
let drawingData = {};

function setupSocket(io) {
    io.on("connection", (socket) => {
        console.log("A user connected");
        let currentRoom = null;

        socket.on("joinRoom", (room) => {
            socket.join(room);
            currentRoom = room;

            if (!drawingData[room]) {
                drawingData[room] = [];
            }
            //기존 그림 전송
            socket.emit("initDrawing", drawingData[room]);
        });

        //그리기
        socket.on("draw", (data) => {
            if (currentRoom) {
                drawingData[currentRoom].push(data);
                socket.to(currentRoom).emit("draw", data);
            }
        });

        //초기화
        socket.on("clearCanvas", () => {
            if (currentRoom) {
                drawingData[currentRoom] = [];
                io.to(currentRoom).emit("clearCanvas");
            }
        });

        socket.on("disconnect", () => {
            console.log("User disconnected");
        });
    });
}

module.exports = setupSocket;
