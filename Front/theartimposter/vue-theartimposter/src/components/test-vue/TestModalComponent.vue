<template>
    <div>
        <button @click="showVoteResultModal">투표 결과 모달 열기</button>
        <br />
        <button @click="showResultFakeArtist">투표 결과 모달 가짜예술가 승</button>
        <br />
        <button @click="showResultArtist">투표 결과 예술가 승</button>
        <br />
        <button @click="showGuessWaitingModal">가짜 예술가 주제입력 대기</button>
        <br />
        <button @click="showFakeArtistGuessModal">가짜 예술가 주제 입력 모달</button>
        <br />
        <VoteResultModal ref="voteResultModal" />
        <GuessWaitingModal ref="guessWaitingModal" />
        <FakeArtistGuessModal ref="fakeArtistGuessModal" />
    </div>
</template>

<script>
import FakeArtistGuessModal from "@/components/modal/FakeArtistGuessModal.vue";
import GuessWaitingModal from "@/components/modal/GuessWatingModal.vue";
import VoteResultModal from "@/components/modal/VoteResultModal.vue";
import { EventBus } from "@/utils/eventBus";

export default {
    components: {
        VoteResultModal,
        GuessWaitingModal,
        FakeArtistGuessModal,
    },
    methods: {
        showVoteResultModal() {
            // EventBus를 통해 VoteResultModal을 열기
            EventBus.$emit("voteResult", {
                voteResults: [
                    { username: "Alice", votes: 3, role: "artist" },
                    { username: "Bob", votes: 5, role: "fake_artist" },
                ],
                abstentionCount: 2,
            });
        },
        showResultFakeArtist() {
            EventBus.$emit("roundEnd", {
                winner: "fake_artist",
            });
        },
        showResultArtist() {
            EventBus.$emit("roundEnd", {
                winner: "artist",
            });
        },
        showGuessWaitingModal() {
            EventBus.$emit("fakeArtistGuessStart1");
        },
        showFakeArtistGuessModal() {
            EventBus.$emit("fakeArtistGuessStart");
        },
    },
};
</script>
