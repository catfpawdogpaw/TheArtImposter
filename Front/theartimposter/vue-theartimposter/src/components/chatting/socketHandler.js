import io from 'socket.io-client';

export default {
    connectToServer(url) {
        const socket = io(url);
        console.log('서버에 접속하였습니다');
        return socket;
    },

    joinRoom(socket, room, accessToken, userId) {
        socket.emit('joinRoom', room, userId, accessToken);
        console.log('joinRoom 에밋 함');
    },

    // 채팅 기능 설정
    setupChatListeners(socket, messageCallback, userJoinedCallback, userLeftCallback) {
        if (!socket) {
            console.error('Socket is not available');
            return;
        }
        socket.on('receiveMessage', messageCallback);
        socket.on('userJoined', userJoinedCallback);
        socket.on('userLeft', userLeftCallback);
    },

    sendMessage(socket, messageData) {
        socket.emit('sendMessage', messageData);
    },
};
