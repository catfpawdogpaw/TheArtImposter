function chatHandler(io, socket, GameRoomStatus) {
    socket.on("sendMessage", (messageData) => {
        // 소켓에 저장된 플레이어 정보를 사용합니다.
        const player = GameRoomStatus.players.find(p => p.socketId === socket.id);
        const user = player ? player.nickName : 'Unknown';

        console.log(`[${user}]: ${messageData}`);
        const chatMessage = {
            user: user,
            message: messageData,
            timestamp: new Date().toISOString(),
        };
        console.log(chatMessage);
        // 메시지를 현재 방에 있는 모든 클라이언트에게 전송
        console.log(GameRoomStatus.gameRoomTitle);
        io.to(GameRoomStatus.gameRoomTitle).emit("receiveMessage", chatMessage);
    });
}

module.exports = chatHandler;
