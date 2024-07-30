function chatHandler(io, socket, GameRoomStatus) {


    socket.on("sendMessage", (messageData) => {
        const chatMessage = {
            user: messageData.user,
            message: messageData.message,
            timestamp: new Date().toISOString(),
        };

        // 메시지를 현재 방에 있는 모든 클라이언트에게 전송
        io.to(GameRoomStatus.gameRoomTitle).emit("receiveMessage", chatMessage);
    });
}

module.exports = chatHandler;
