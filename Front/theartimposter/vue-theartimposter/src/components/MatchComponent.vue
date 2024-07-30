<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div>
        <button @click="connect">Connect</button>
        <button @click="disconnect">Disconnect</button>
        <div>
            <input v-model="messageToSend" placeholder="Enter message" />
            <button @click="sendMessage">Send Message</button>
        </div>
        <h2>Received Messages</h2>
        <ul>
            <li v-for="(msg, index) in receivedMessages" :key="index">{{ msg }}</li>
        </ul>
        <h2>Matched Game Room</h2>
        <p v-if="matchedGameRoom">{{ matchedGameRoom }}</p>
    </div>
    
</body>


<script>
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

export default {
    data() {
        return {
            stompClient: null,
            connected: false,
            messageToSend: '',
            receivedMessages: [],
            matchedGameRoom: null,
        };
    },
    methods: {
        connect() {
            const socket = new SockJS('http://localhost:8080/wait-service/wait-websocket');
            this.stompClient = new Client({
                webSocketFactory: () => socket,
                onConnect: (frame) => {
                    this.connected = true;
                    console.log('Connected: ' + frame);

                    this.stompClient.subscribe('/wait-service/waitroom', (message) => {
                        const receivedMessage = message.body;
                        console.log('Received: ' + receivedMessage);
                        this.receivedMessages.push(receivedMessage);

                        if (receivedMessage.startsWith('Match Found: ')) {
                            this.matchedGameRoom = receivedMessage;
                        }
                    });

                    this.sendMessage('User connected');
                },
                onDisconnect: () => {
                    this.connected = false;
                    console.log('Disconnected');
                },
            });
            this.stompClient.activate();
        },
        disconnect() {
            if (this.stompClient !== null) {
                this.stompClient.deactivate();
            }
        },
        sendMessage(message) {
            if (this.stompClient && this.connected) {
                const msg = message || this.messageToSend;
                console.log('Sending: ' + msg);
                this.stompClient.publish({
                    destination: '/wait-service/waitroom/join',
                    body: msg,
                });
                this.messageToSend = '';
            }
        },
    },
};
</script>

<style scoped>
h1 {
    color: #42b983;
}
</style>
</html>