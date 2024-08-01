<template>
    <div class="vote-container">
        <div class="timer">
            <img src="@/assets/game/image/clock.png" alt="clock" />
            <span>{{ timeLeft }}</span>
        </div>
        <div class="prompt">가짜 예술가를 투표해주세요.</div>
        <div class="options">
            <div
                v-for="option in sortedOptions"
                :key="option.id"
                :class="['option', { selected: selectedOption === option.id, hovered: hoveredOption === option.id }]"
                :style="{ borderColor: option.color, color: option.color }"
                @click="selectOption(option.id)"
                @mouseover="hoverOption(option.id)"
                @mouseleave="hoverOption(null)"
            >
                <div class="circle" :style="{ backgroundColor: option.color }">
                    {{ option.number }}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { EventBus } from '@/utils/eventBus';
export default {
    data() {
        return {
            timeLeft: '00',
            timer: null,
            selectedOption: null,
            hoveredOption: null,
        };
    },
    computed: {
        sortedOptions() {
            const players = this.$store.getters.getOtherPlayers;
            if (!players || players.length === 0) {
                // 플레이어 데이터가 없을 경우 더미 데이터를 반환
                return [
                    { id: 1, number: '?', color: 'gray' },
                    { id: 2, number: '?', color: 'gray' },
                    { id: 3, number: '?', color: 'gray' },
                    { id: 4, number: '?', color: 'gray' },
                ];
            }
            return players
                .sort((a, b) => a.turn - b.turn)
                .map((player, index) => ({
                    id: index + 1,
                    number: player.turn,
                    color: player.color,
                }));
        },
    },
    created() {
        EventBus.$on('voteStart', (time) => {
            this.timeLeft = time;
            this.timer = setInterval(() => {
                if (this.timeLeft > 0) {
                    this.timeLeft--;
                } else {
                    clearInterval(this.timer);
                    this.timeLeft = '00';
                }
            }, 1000);
        }); // 이벤트 수신 및 핸들러 등록
    },
    beforeDestroy() {
        EventBus.$off('voteStart'); // 이벤트 수신 및 핸들러 해제
    },
    methods: {
        selectOption(id) {
            this.selectedOption = id;
        },
        hoverOption(id) {
            this.hoveredOption = id;
        },
    },
};
</script>

<style scoped>
.vote-container {
    border: 1px solid black;
    background-color: #ffffff;
    width: 800px;
    height: 700px;
    margin: 0 auto;
}

.timer {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 120px;
    font-size: 80px;
}

.timer img {
    width: 180px;
    height: 180px;
    margin-right: 20px;
}

.prompt {
    font-size: 25px;
    margin-top: 35px;
    margin-bottom: 35px;
}

.options {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 50px;
}

.option {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 150px;
    height: 50px;
    border: 2px solid;
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.3s;
}

.option .circle {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 35px;
    height: 35px;
    border-radius: 50%;
    color: white;
    font-weight: bold;
}

.option.hovered {
    background-color: rgba(0, 0, 0, 0.1);
}

.option.selected {
    background-color: rgba(0, 0, 0, 0.2);
}
</style>
