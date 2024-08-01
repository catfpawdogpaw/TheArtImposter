<template>
    <div v-if="showModal" class="modal-overlay">
        <div class="modal-content">
            <h2 class="vote-result-header">
                가짜 예술가님 <br />
                주제를 맞춰주세요
            </h2>
            <input
                type="text"
                v-model="userInput"
                @keydown.enter="sendText"
                placeholder="여기에 입력하세요"
                class="input-field"
            />
        </div>
    </div>
</template>

<script>
import { EventBus } from "@/utils/eventBus";

export default {
    name: "FakeArtistGuessModal",
    data() {
        return {
            showModal: false,
            userInput: "",
            socket: null,
        };
    },
    created() {
        EventBus.$on("fakeArtistGuessStart", this.handleModal);
    },
    beforeDestroy() {
        EventBus.$off("fakeArtistGuessStart", this.handleModal);
    },
    mounted() {
        if (!this.socket) {
            this.socket = this.$socket;
        }
    },
    methods: {
        handleModal() {
            this.showModal = true;
        },
        closeModal() {
            this.showModal = false;
            this.winRole = null;
        },
        sendText() {
            if (this.userInput.trim()) {
                this.socket.emit("guessWord", this.userInput);
                this.userInput = "";
                this.closeModal();
            }
        },
    },
};
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    font-size: 18px;
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
    width: 50%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.vote-result-header {
    margin-bottom: 20px;
    font-size: 24px;
    font-weight: bold;
}

.input-field {
    width: 100%;
    padding: 10px;
    font-size: 16px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
}

.input-field:focus {
    outline: none;
    border-color: #4caf50;
    box-shadow: 0 0 5px rgba(76, 175, 80, 0.5);
}
</style>
