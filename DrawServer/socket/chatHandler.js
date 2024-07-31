function chatHandler(io, socket, GameRoomStatus) {
    socket.on("sendMessage", (messageData) => {
        // messageData.user가 없는 경우 기본 사용자 정보 설정
        // const user = this.userId || { nickname: 'Unknown' };

        // 소켓에 저장된 플레이어 정보를 사용합니다.
        const user = socket.playerInfo || { nickName: 'Unknown' };

        console.log(user);
        console.log(messageData);
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
