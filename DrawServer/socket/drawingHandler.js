function drawingHandler(io, socket, GameRoomStatus) {
    socket.on("joinRoom", (room) => {
        socket.emit("initDrawing", GameRoomStatus.drawingData);
    });

    socket.on("draw", (data) => {
        // 그림 데이터 저장
        GameRoomStatus.drawingData.push(data);

        // 그림 데이터 방에 전파
        socket.to(GameRoomStatus.gameRoomId).emit("draw", data);
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
