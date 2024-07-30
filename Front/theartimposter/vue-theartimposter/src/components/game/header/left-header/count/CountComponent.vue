<template>
    <div class="count-component">
        <img class="clock-icon" src="@/assets/game/image/clock.png" alt="Clock Icon" />
        <span>{{ count }}</span>
    </div>
</template>

<script>
export default {
    name: 'CountComponent',
    data() {
        return {
            count: 30,
            timer: null,
        };
    },
    methods: {
        startCountdown() {
            this.timer = setInterval(() => {
                if (this.count > 0) {
                    this.count--;
                } else {
                    clearInterval(this.timer);
                    this.onCountdownEnd();
                }
            }, 1000);
        },
        onCountdownEnd() {
            // 다른 함수 호출
            console.log('Countdown ended');
            // 여기에 원하는 함수를 호출하세요
        },
    },
    destroyed() {
        if (this.timer) {
            clearInterval(this.timer);
        }
    },
};
</script>

<style scoped>
.count-component {
    width: 110px;
    height: 50px;
    display: flex;
    align-items: center;
    background-color: #ccc;
    border-radius: 5px;
    margin-top: 22.5px;
}

.clock-icon {
    width: 55px;
    height: 55px;
}

.count-component span {
    margin-left: 15px;
    scale: 2;
}
</style>
