<template>
    <div class="count-component">
        <img class="clock-icon" src="@/assets/game/image/clock.png" alt="Clock Icon" />
        <span>{{ count }}</span>
    </div>
</template>

<script>
import { EventBus } from '@/utils/eventBus';

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
            if (this.timer) {
                clearInterval(this.timer); // 이전 타이머가 있다면 제거
            }
            this.count = 30; // 카운트 초기화
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
            console.log('Countdown ended');
            this.$socket.emit('turnEnd');
        },
    },
    created() {
        EventBus.$on('turnPlayerChanged', this.startCountdown); // 이벤트 수신 및 핸들러 등록
    },
    beforeDestroy() {
        EventBus.$off('turnPlayerChanged', this.startCountdown); // 이벤트 핸들러 해제
        if (this.timer) {
            clearInterval(this.timer); // 컴포넌트가 파괴되기 전 타이머 제거
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
