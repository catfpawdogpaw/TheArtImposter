<template>
    <div class="lobby">
        <h1>대기실</h1>
        <button @click="startMatching">매칭 시작 버튼</button>
        <match-modal :show="showMatch" />

        <button @click="startSubject">주제 모달</button>
        <subject-modal :show="showSubject" :subject="subject" :word="word" />

        <!-- 삭제할 것 -->

        <button @click="getCurrentSessions">Get Current Sessions</button>
    </div>
</template>

<script>
import MatchModal from "@/components/modal/MatchModal.vue";
import SubjectModal from "@/components/modal/SubjectModal.vue";

import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export default {
    components: {
        MatchModal,
        SubjectModal,
    },
    data() {
        return {
            showMatch: false,
            showSubject: false,
            subject: "자동차",
            word: "람보르기니",
        };
    },
    methods: {
        connect() {
            const socket = new SockJS(`http://${this.$springHost}/wait-websocket`);
            this.stompClient = new Client({
                webSocketFactory: () => socket,
                onConnect: (frame) => {
                    this.connected = true;
                    console.log("Connected: " + frame);

                    this.stompClient.subscribe("/wait-service/waitroom/sessions", (message) => {
                        this.showMessage(JSON.parse(message.body));
                    });
                },
            });
            this.stompClient.activate();
        },
        showMessage(message) {
            const output = document.getElementById("output");
            output.innerHTML = "";
            message.forEach((session) => {
                const p = document.createElement("p");
                p.appendChild(document.createTextNode(session));
                output.appendChild(p);
            });
        },
        getCurrentSessions() {
            if (this.stompClient && this.connected) {
                this.stompClient.publish({ destination: "/app/waitroom/sessions", body: "{}" });
                console.log("get current session");
            } else {
                console.error("stompClient is not connected");
            }
        },
        startMatching() {
            // 여기에 매칭 로직을 구현합니다.

            const socket = new SockJS("http://localhost:8080/wait-service/wait-websocket");
            this.stompClient = new Client({
                webSocketFactory: () => socket,
                onConnect: (frame) => {
                    this.connected = true;
                    console.log("Connected: " + frame);

                    this.stompClient.subscribe("/wait-service/waitroom", (message) => {
                        const receivedMessage = message.body;
                        console.log("Received: " + receivedMessage);
                        this.receivedMessages.push(receivedMessage);

                        if (receivedMessage.startsWith("Match Found: ")) {
                            this.matchedGameRoom = receivedMessage;
                        }
                    });

                    this.sendMessage("------User connected-------");
                },
            });
            this.stompClient.activate();

            // 매칭이 성공하면 모달을 표시하고 게임으로 전환합니다.

            // 매칭 성공했는지 확인
            this.showMatch = true;
            setTimeout(this.startGame, 3000);
        },
        sendMessage(message) {
            if (this.stompClient && this.connected) {
                const msg = message || this.messageToSend;
                console.log("Sending: " + msg);
                this.stompClient.publish({
                    destination: "/wait-service/waitroom/join",
                    body: msg,
                });
                this.messageToSend = "";
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

<style scoped>
.lobby {
  min-height: 700px;
  border: 1px solid black;
  min-width: 650px;
}
</style>