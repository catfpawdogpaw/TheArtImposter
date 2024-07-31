<template>
    <div id="chat">
        <div v-for="message in messages" :key="message.timestamp">
            {{ message.user.nickname }}: {{ message.message }}
        </div>
        <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Enter message" />
    </div>
</template>

<script>
import ChatSocket from '@/components/chatting/ChatSocket.js';
import { mapState } from 'vuex';

export default {
    name: 'ChatCompo',
    data() {
        return {
            socket: null,
            messages: [],
            newMessage: '',
            room: 'TestRoom',
        };
    },
    computed: {
        ...mapState(['user']),
    },
    mounted() {
        this.$root.$on('joinRoom', (room) => {
            this.joinRoom(room);
        });
    },
    methods: {
        connectToServer() {
            this.socket = ChatSocket.connectToServer('http://localhost/3000');
            ChatSocket.setupChatListeners(this.socket, this.handleMessage, this.handleUserJoined, this.handleUserLeft);
        },
        joinRoom(room) {
            this.connectToServer();
            if (this.socket && room) {
                ChatSocket.joinRoom(this.socket, room, null);
            }
        },
        handleMessage(message) {
            this.messages.push(message);
        },
        handleUserJoined(user) {
            this.messages.push({ user, message: `${user.nickname} joined the room` });
        },
        handleUserLeft(user) {
            this.messages.push({ user, message: `${user.nickname} left the room` });
        },
        sendMessage() {
            if (this.newMessage.trim() !== '') {
                const messageData = {
                    user: this.user, // 유저 정보를 포함
                    message: this.newMessage,
                };
                ChatSocket.sendMessage(this.socket, messageData);
                this.newMessage = '';
            }
        },
    },
};
</script>

<style scoped></style>
