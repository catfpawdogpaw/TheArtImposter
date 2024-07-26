// 그림 데이터 저장 객체
let drawingData = {};

function drawingHandler(io, socket) {
    socket.on("joinRoom", (room) => {
        socket.join(room);
        currentRoom = room;

        if (!drawingData[room]) {
            drawingData[room] = [];
        }
        // 기존 그림 전송
        socket.emit("initDrawing", drawingData[room]);
    });

    socket.on("draw", (data) => {
        if (currentRoom) {
            // 그림 데이터 저장
            drawingData[currentRoom].push(data);

            // 그림 데이터 방에 전파
            socket.to(currentRoom).emit("draw", data);
        }
    });

    socket.on("clearCanvas", () => {
        if (currentRoom) {
            // 그림 데이터 초기화
            drawingData[currentRoom] = [];

            // 클리어 이벤트 방에 전파
            io.to(currentRoom).emit("clearCanvas");
        }
    });

    socket.on("disconnect", () => {
        console.log("User disconnected");
    });
}

module.exports = drawingHandler;
