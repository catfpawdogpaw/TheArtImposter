<template>
    <div v-if="showModal" class="modal-overlay">
        <div class="modal-content">
            <h2 class="vote-result-header">투표 결과</h2>
            <div v-if="winRole" class="winner-announcement">{{ winRoleText }}팀이 승리했습니다.</div>
            <div v-for="(result, index) in voteResults" :key="index" class="vote-result">
                <p>
                    {{ result.username }}: {{ result.votes }} 표
                    <span v-if="result.role === 'fake_artist'" class="fake-artist">(가짜예술가)</span>
                </p>
            </div>
            <p>기권: {{ abstentionCount }} 표</p>
            <button @click="closeModal">닫기</button>
        </div>
    </div>
</template>

<script>
import { EventBus } from "@/utils/eventBus";

export default {
    name: "VoteResultModal",
    data() {
        return {
            showModal: false,
            voteResults: [],
            abstentionCount: 0,
            winRole: null,
        };
    },
    computed: {
        winRoleText() {
            return this.winRole === "fake_artist" ? "가짜예술가" : "예술가";
        },
    },
    created() {
        EventBus.$on("voteResult", this.handleVoteResult);
        EventBus.$on("roundEnd", this.handleRoundEnd);
    },
    beforeDestroy() {
        EventBus.$off("voteResult", this.handleVoteResult);
        EventBus.$off("roundEnd", this.handleRoundEnd);
    },
    methods: {
        handleVoteResult(data) {
            this.voteResults = data.voteResults;
            this.abstentionCount = data.abstentionCount;

            this.showModal = true;
            // setTimeout(() => {
            //     this.showSubject = false;
            // }, 3000);
        },
        handleRoundEnd(data) {
            this.winRole = data.winner;
            this.showModal = true;
        },
        closeModal() {
            this.showModal = false;
            this.winRole = null;
        },
    },
};
</script>

<style scoped>
.modal {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    width: auto;
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    padding: 1.6rem 3rem;
    border: 3px solid black;
    border-radius: 5px;
    background: white;
    box-shadow: 8px 8px 0 rgba(0, 0, 0, 0.2);
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgb(255, 255, 255, 0);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    font-size: 30px;
    background-color: grey;
    /* opacity: 0.8; */
    padding: 20px;
    border-radius: 5px;
    text-align: center;
    width: 70%;
}

.vote-result {
    margin-bottom: 10px;
    text-align: left;
    margin-left: 10%;
}

.vote-result-header {
    margin-bottom: 50px;
}

.winner-announcement {
    font-weight: bold;
    font-size: 1.2em;
    margin-bottom: 20px;
    color: #4caf50;
}

.fake-artist {
    font-weight: bold;
    color: #ff5722;
    margin-left: 20px;
}
</style>
