<template>
    <div class="answer-component">
        <span class="answer-span"
            ><strong class="font">{{ answer }}</strong></span
        >
    </div>
</template>

<script>
export default {
    name: 'AnswerComponent',
    computed: {
        answer() {
            return this.$store.getters.getMyInfo ? this.$store.getters.getMyInfo.keyword : '?';
        },
    },
};
</script>

<style scoped>
.answer-component {
    width: 98px;
    height: 50px;
    margin-top: 22.5px;
    display: flex;
    align-items: center;
    background-color: #464e69;
    border-radius: 5px;
}

.answer-span {
    margin: 0 auto;
}

.font {
    color: #dadada;
}
</style>
