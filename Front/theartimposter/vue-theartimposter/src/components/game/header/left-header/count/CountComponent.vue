<template>
    <div class="count-component">
        <img class="clock-icon" src="@/assets/game/image/clock-invert.png" alt="Clock Icon" />
        <span class="font">{{ count }}</span>
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
            console.log('시간초 흐르기 시작!');

            if (this.timer) {
                clearInterval(this.timer); // 이전 타이머가 있다면 제거
            }
            console.log('getGameInfo :' + this.$store.getters.getGameRoomStatus);
            this.count = this.$store.getters.getGameRoomStatus
                ? this.$store.getters.getGameRoomStatus.gameSetting.turnTimeLimit
                : 30; // 카운트 초기화
            this.count = 8;
            this.timer = setInterval(() => {
                if (this.count > 0) {
                    this.count--;
                } else {
                    this.onCountdownEnd();
                    clearInterval(this.timer);
                }
            }, 1000);
        },
        onCountdownEnd() {
            console.log('Countdown ended');
            this.$socket.emit('drawEnd');
        },
        turnEnd() {
            this.onCountdownEnd();
            clearInterval(this.timer);
        },
    },
    created() {
        EventBus.$on('turnPlayerChanged', this.startCountdown); // 이벤트 수신 및 핸들러 등록
        EventBus.$on('turnEnd', this.turnEnd); // 이벤트 수신 및 핸들러 등록
        EventBus.$on('voteStart', (time) => {
            EventBus.$emit('voteTime', time.timeLimit);
            this.count = time.timeLimit;
            this.timer = setInterval(() => {
                if (this.count > 0) {
                    EventBus.$emit('voteTime', --this.count);
                } else {
                    EventBus.$emit('voteTime', -1);
                    this.onCountdownEnd();
                    clearInterval(this.timer);
                }
            }, 1000);
        }); // 이벤트 수신 및 핸들러 등록
    },
    beforeDestroy() {
        EventBus.$off('turnPlayerChanged', this.startCountdown); // 이벤트 핸들러 해제
        if (this.timer) {
            clearInterval(this.timer); // 컴포넌트가 파괴되기 전 타이머 제거
        }
        EventBus.$off('turnEnd', this.turnEnd); // 이벤트 수신 및 핸들러 해제
        EventBus.$off('voteStart'); // 이벤트 수신 및 핸들러 해제
    },
};
</script>

<style scoped>
.count-component {
    width: 110px;
    height: 50px;
    display: flex;
    align-items: center;
    background-color: #464e69;
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

.font {
    color: #dadada;
}
</style>
