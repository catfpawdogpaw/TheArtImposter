<template>
  <div id="chat">
    <div v-for="message in messages" :key="message.timestamp">
      {{ message.user.nickname }}: {{ message.message }}
    </div>
    <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Enter message" />
  </div>
</template>

<script>
import socketHandler from "@/components/chatting/socketHandler";

export default {
  name: 'ChatCompo',
  inject: ['socket'],
  data() {
    return {
      messages: [],
      newMessage: '',
    };
  },
  mounted() {
    const socket = this.socket();
    if (this.socket) {
      socketHandler.setupChatListeners(
          socket,
          this.receiveMessage,
          this.userJoined,
          this.userLeft
      );
    } else {
      console.error('Socket is not available');
    }
  },
  methods: {
    sendMessage() {
      const socket = this.socket();
      if (this.newMessage.trim() !== '') {
        socketHandler.sendMessage(socket, this.newMessage);
        this.newMessage = '';
      }
    },
    receiveMessage(message) {
      console.log('Received message:', message); // 메시지 구조 확인용 로그 출력
      if (message && message.user && message.user.nickname) {
        this.messages.push(message);
      } else {
        console.error('Received message with missing user information:', message);
      }
    },
    userJoined(user) {
      if (user && user.nickname) {
        this.messages.push({ user: { nickname: 'System' }, message: `${user.nickname} joined the room`, timestamp: Date.now() });
      } else {
        console.error('User joined event with missing user information:', user);
      }
    },
    userLeft(user) {
      if (user && user.nickname) {
        this.messages.push({ user: { nickname: 'System' }, message: `${user.nickname} left the room`, timestamp: Date.now() });
      } else {
        console.error('User left event with missing user information:', user);
      }
    },
  },
}
</script>

<style scoped>

</style>