<template>
    <div>
        <h1>대기실</h1>
        <button @click="startMatching">매칭 시작 버튼</button>
        <match-modal :show="showMatch" />

        <!-- <button @click="startSubject">주제 모달</button>
        <subject-modal :show="showSubject" :subject="subject" :word="word" /> -->

        <!-- 삭제할 것 -->

        <!-- <button @click="getCurrentSessions">Get Current Sessions</button> -->
        
        <div>
            <h2>Vuex 상태</h2>
            <p>Game Room ID: {{ roomId }}</p>
            <p>Game Room Title: {{ roomTitle }}</p>
        </div>
    
    </div>
</template>

<script>
import MatchModal from "@/components/modal/MatchModal.vue";
// import SubjectModal from "@/components/modal/SubjectModal.vue";
import { mapActions, mapState } from 'vuex';  // Vuex의 mapActions 가져오기

import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

export default {
    components: {
        MatchModal,
        // SubjectModal,
    },
    data() {
        return {
            showMatch: false,
            showSubject: false,
            subject: "자동차",
            word: "람보르기니",
            receivedMessages: [], // 추가: 받은 메시지를 저장하기 위한 배열
        };
    },
    computed: {
        ...mapState(['roomId', 'roomTitle']), // Vuex 상태 맵핑
    },
    methods: {
        ...mapActions(['setRoomInfo']),  // setRoomInfo 액션을 맵핑

        connect() {
            const socket = new SockJS('http://localhost:8080/wait-websocket');
            this.stompClient = new Client({
                webSocketFactory: () => socket,
                onConnect: (frame) => {
                    this.connected = true;
                    console.log('Connected: ' + frame);

                    this.stompClient.subscribe('/wait-service/waitroom/sessions', (message) => {
                        this.showMessage(JSON.parse(message.body));
                    });


                    this.stompClient.subscribe('/wait-service/waitroom', (message) => {
                        const receivedMessage = JSON.parse(message.body);
                        console.log('Received: ', receivedMessage);
                        this.receivedMessages.push(receivedMessage);
                        
                        if (receivedMessage.roomId && receivedMessage.roomTitle) {
                            this.setRoomInfo({
                                roomId: receivedMessage.roomId,
                                roomTitle: receivedMessage.roomTitle,
                            });
                            this.showMatch = true;
                            setTimeout(this.startGame, 3000);
                        }
                    });
                },
            });
            this.stompClient.activate();
        },
        showMessage(message) {
            const output = document.getElementById('output');
            output.innerHTML = '';
            message.forEach((session) => {
                const p = document.createElement('p');
                p.appendChild(document.createTextNode(session));
                output.appendChild(p);
            });
        },
        getCurrentSessions() {
            if (this.stompClient && this.connected) {
                this.stompClient.publish({ destination: "/app/waitroom/sessions", body: "{}" });
                console.log("get current session");
            } else {
                console.error('stompClient is not connected');
            }
        },
        startMatching() {
            const socket = new SockJS('http://localhost:8080/wait-service/wait-websocket');
            this.stompClient = new Client({
                webSocketFactory: () => socket,
                onConnect: (frame) => {
                    this.connected = true;
                    console.log('Connected: ' + frame);

                    this.stompClient.subscribe('/wait-service/waitroom', (message) => {
                        const receivedMessage = JSON.parse(message.body);
                        console.log('Received: ', receivedMessage);
                        this.receivedMessages.push(receivedMessage);

                        if (receivedMessage.roomId && receivedMessage.roomTitle) {
                            this.setRoomInfo({
                                roomId: receivedMessage.roomId,
                                roomTitle: receivedMessage.roomTitle,
                            });
                            this.showMatch = true;
                            setTimeout(this.startGame, 3000);
                        }
                    });

                    this.sendMessage('------User connected-------');
                },
            });
            this.stompClient.activate();

            // 매칭이 성공하면 모달을 표시하고 게임으로 전환합니다.
            this.showMatch = true;
            setTimeout(this.startGame, 3000);
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
        startGame() {
            this.showMatch = false;
            // 게임화면으로 전환
            // this.$router.push("/game");
        },
        startSubject() {
            this.showSubject = true;
            setTimeout(() => {
                this.showSubject = false;
            }, 3000);
        },
    },
};
</script>
