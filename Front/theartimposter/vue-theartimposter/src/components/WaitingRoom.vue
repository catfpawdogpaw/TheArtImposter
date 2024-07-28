<template>
    <div>
        <h1>대기실</h1>
        <button @click="startMatching">매칭 시작 버튼</button>
        <match-modal :show="showMatch" />

        <button @click="startSubject">주제 모달</button>
        <subject-modal :show="showSubject" :subject="subject" :word="word" />
    </div>
</template>

<script>
import MatchModal from "@/components/modal/MatchModal.vue";
import SubjectModal from "@/components/modal/SubjectModal.vue";

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
        startMatching() {
            // 여기에 매칭 로직을 구현합니다.
            // 매칭이 성공하면 모달을 표시하고 게임으로 전환합니다.
            this.showMatch = true;
            setTimeout(this.startGame, 3000);
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
