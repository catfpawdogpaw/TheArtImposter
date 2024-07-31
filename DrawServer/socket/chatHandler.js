function chatHandler(io, socket, GameRoomStatus) {
    socket.on("sendMessage", (messageData) => {
        // messageData.user가 없는 경우 기본 사용자 정보 설정
        const user = this.userId || { nickname: 'Unknown' };
        console.log(user);
        console.log(messageData);
        const chatMessage = {
            user: user,
            message: messageData.message,
            timestamp: new Date().toISOString(),
        };
        console.log(chatMessage);
        // 메시지를 현재 방에 있는 모든 클라이언트에게 전송
        io.to(GameRoomStatus.gameRoomTitle).emit("receiveMessage", chatMessage);
    });
}

module.exports = chatHandler;
