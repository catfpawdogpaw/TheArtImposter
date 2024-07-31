import io from 'socket.io-client';

export default {
    connectToServer(url) {
        const socket = io(url);
        console.log('서버에 접속하였습니다');
        return socket;
    },

    joinRoom(socket, room, accessToken) {
        socket.emit('joinRoom', room, accessToken); // accessToken도 함께 전송
    },

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
