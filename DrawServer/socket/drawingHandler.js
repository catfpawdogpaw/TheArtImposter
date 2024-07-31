function addDrawListeners(io, socket, GameRoomStatus) {
    socket.on("draw", (data) => {
        GameRoomStatus.drawingData.push(data);
        io.to(GameRoomStatus.gameRoomTitle).emit("draw", data);
    });

    socket.on("clearCanvas", () => {
        GameRoomStatus.drawingData = [];
        io.to(GameRoomStatus.gameRoomTitle).emit("clearCanvas");
    });
}

function removeDrawListeners(socket) {
    socket.removeAllListeners("draw");
    socket.removeAllListeners("clearCanvas");
}

module.exports = {
    addDrawListeners,
    removeDrawListeners,
};
