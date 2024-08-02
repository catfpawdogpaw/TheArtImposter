<template>
    <div class="left-game-header">
        <div class="t">
            <count-component></count-component>
            <turn-component></turn-component>
            <category-component></category-component>
            <answer-component></answer-component>
            <subject-modal :show="showSubject" :subject="currentCategory" :word="currentAnswer" />
        </div>
    </div>
</template>

<script>
import CountComponent from './count/CountComponent.vue';
import TurnComponent from './turn/TurnComponent.vue';
import CategoryComponent from './category/CategoryComponent.vue';
import AnswerComponent from './answer/AnswerComponent.vue';
import SubjectModal from '@/components/modal/SubjectModal.vue';
import { EventBus } from '@/utils/eventBus';

export default {
    components: { CountComponent, TurnComponent, CategoryComponent, AnswerComponent, SubjectModal },
    data() {
        return {
            showSubject: false,
        };
    },
    computed: {
        currentCategory() {
            return this.$store.getters.getMyInfo ? this.$store.getters.getMyInfo.category : '?';
        },
        currentAnswer() {
            return this.$store.getters.getMyInfo ? this.$store.getters.getMyInfo.keyword : '?';
        },
    },
    created() {
        EventBus.$on('settingGame', this.setSubjectModalOn);
    },
    beforeDestroy() {
        EventBus.$off('settingGame', this.setSubjectModalOn);
    },
    methods: {
        setSubjectModalOn() {
            this.showSubject = true;
            setTimeout(() => {
                this.setSubjectModalOff();
            }, 3000);
        },
        setSubjectModalOff() {
            this.showSubject = false;
        },
    },
};
</script>

<style scoped>
.left-game-header {
    width: var(--h-left-game-header-width);
    height: var(--h-game-header-height);
    margin: 0 auto;
    margin-bottom: var(--h-game-header-margin-bottom);
    border: 1px solid black;
}

.t {
    display: flex;
    align-items: center;
    border-radius: 5px;
    background-color: #f9f9f9;
}

.count-component,
.turn-component,
.category-component,
.answer-component {
    margin-right: 15px;
    margin-left: 15px;
}

.count-component:last-child,
.turn-component:last-child,
.category-component:last-child,
.answer-component:last-child {
    margin-right: 0;
}
</style>
