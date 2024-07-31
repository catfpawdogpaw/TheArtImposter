<template>
  <div id="chat">
    <div class="message-list">
      <div v-for="message in messages" :key="message.timestamp" class="message">
        <span class="user">{{ message.user }}</span>: <span class="text">{{ message.message }}</span>
      </div>
    </div>
    <div class="message-input">
      <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Enter message" />
    </div>
  </div>
</template>

<script>
import socketHandler from "@/components/chatting/socketHandler";

export default {
  name: 'ChatCompo',
  inject: ['socket'], // 부모로부터 소켓 인스턴스 수신
  data() {
    return {
      messages: [],
      newMessage: '',
      socketInstance: null,
    };
  },
  mounted() {
    console.log('⭐⭐⭐',this.socket());
    this.setupSocket();
  },
  watch: {
    socket(newSocket) {
      if (newSocket) {
        this.setupSocket();
      }
    }
  },
  methods: {
    setupSocket() {
      this.socketInstance = this.socket();
      if (this.socketInstance) {
        socketHandler.setupChatListeners(
            this.socketInstance,
            this.receiveMessage,
            this.userJoined,
            this.userLeft
        );
      } else {
        console.error('Socket is not available');
      }
    },
    sendMessage() {
      const socket = this.socketInstance;
      if (this.newMessage.trim() !== '') {
        socketHandler.sendMessage(socket, this.newMessage);
        this.newMessage = '';
      }
    },
    receiveMessage(message) {
      console.log('Received message:', message); // 메시지 구조 확인용 로그 출력
      if (message && message.user && message.message) {
        this.messages.push(message);
      } else {
        console.error('Received message with missing user information:', message);
      }
    },
    userJoined(user) {
      if (user && user.nickName) {
        this.messages.push({ user: { nickName: 'System' }, message: `${user.nickName} joined the room`, timestamp: Date.now() });
      } else {
        console.error('User joined event with missing user information:', user);
      }
    },
    userLeft(user) {
      if (user && user.nickName) {
        this.messages.push({ user: { nickName: 'System' }, message: `${user.nickName} left the room`, timestamp: Date.now() });
      } else {
        console.error('User left event with missing user information:', user);
      }
    },
  },
}
</script>
<style scoped>
#chat {
  display: flex;
  flex-direction: column;
  height: 60vh;
  max-width: 600px;
  margin: 0 auto;
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
}

.message-list {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background-color: #f9f9f9;
}

.message {
  margin-bottom: 10px;
}

.user {
  font-weight: bold;
  color: #333;
}

.text {
  color: #555;
}

.message-input {
  display: flex;
  border-top: 1px solid #ccc;
  background-color: #fff;
  padding: 10px;
}

.message-input input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}
</style>

