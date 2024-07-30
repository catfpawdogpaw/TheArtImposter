import io from 'socket.io-client';

export default {
    connectToServer(url) {
        const socket = io(url);
        console.log('서버에 접속하였습니다');
        return socket;
    },

    joinRoom(socket, room, accessToken, userId) {
        socket.emit('joinRoom', room, accessToken, userId);
    },

    // 채팅 기능 설정
    setupChatListeners(socket, messageCallback, userJoinedCallback, userLeftCallback) {
        if (!socket) {
            console.error('Socket is not available');
            return;
        }
        socket.on("receiveMessage", messageCallback);
        socket.on("userJoined", userJoinedCallback);
        socket.on("userLeft", userLeftCallback);
    },

    sendMessage(socket, messageData) {
        socket.emit('sendMessage', messageData);
    },

    // 그림 그리기 기능 설정
    setupDrawingListeners(socket, drawCallback, initDrawingCallback, clearCanvasCallback) {
        socket.on('draw', drawCallback);
        socket.on('initDrawing', initDrawingCallback);
        socket.on('clearCanvas', clearCanvasCallback);
    },

    emitDraw(socket, drawingData) {
        socket.emit('draw', drawingData);
    },

    emitClearCanvas(socket) {
        socket.emit('clearCanvas');
    },
};
