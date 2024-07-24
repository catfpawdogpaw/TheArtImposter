//그림 데이터
let drawingData = [];

function setupSocket(io) {
    io.on("connection", (socket) => {
        console.log("A user connected");

        //기존 그림 전송
        socket.emit("initDrawing", drawingData);

        //그리기
        socket.on("draw", (data) => {
            drawingData.push(data);
            socket.broadcast.emit("draw", data);
        });

        //초기화
        socket.on("clearCanvas", () => {
            drawingData = [];
            socket.broadcast.emit("clearCanvas");
        });

        socket.on("disconnect", () => {
            console.log("User disconnected");
        });
    });
}

module.exports = setupSocket;
