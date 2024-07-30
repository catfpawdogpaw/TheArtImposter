import io from "socket.io-client";

export default {
    connectToServer(url) {
        const socket = io(url);
        console.log("채팅 서버에 접속하였습니다");
        return socket;
    },

    joinRoom(socket, room, accessToken) {
        socket.emit("joinRoom", room, accessToken);  // accessToken도 함께 전송
    },

    setupChatListeners(socket, messageCallback, userJoinedCallback, userLeftCallback) {
        socket.on("receiveMessage", messageCallback);
        socket.on("userJoined", userJoinedCallback);
        socket.on("userLeft", userLeftCallback);
    },

    sendMessage(socket, messageData) {
        socket.emit("sendMessage", messageData);  // 수정된 이벤트 이름
    }
};
