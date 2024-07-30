<template>
    <div class="my-turn">
        <span class="turn-number">{{ turnNumber }}</span>
    </div>
</template>

<script>
export default {
    name: 'MyTurnComponent',
    data() {
        return {
            turnNumber: 2, // 예시 값
        };
    },
};
</script>

<style scoped>
.my-turn {
    position: absolute;
    bottom: 0;
    left: 0;
    transform: translate(100%, 50%); /* 적절히 조정 */
}

.turn-number {
    width: 40px;
    height: 40px;
    background-color: magenta;
    color: white;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;
    font-size: 20px;
}
</style>
