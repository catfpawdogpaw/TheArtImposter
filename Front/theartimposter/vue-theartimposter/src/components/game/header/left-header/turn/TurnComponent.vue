<template>
    <div class="turn-component">
        <div v-for="player in players" :key="player.id" :class="{ player: true, active: player.active }">
            <div :style="{ backgroundColor: player.color }" class="player-number">{{ player.number }}</div>
            <span v-if="!isLastPlayer(player)">→</span>
        </div>
    </div>
</template>

<script>
export default {
    name: 'TurnComponent',
    data() {
        return {
            players: [
                { id: 1, number: 1, color: 'red', active: false },
                { id: 2, number: 2, color: 'purple', active: true },
                { id: 3, number: 3, color: 'green', active: false },
                { id: 4, number: 4, color: 'cyan', active: false },
                { id: 5, number: 5, color: 'orange', active: false },
            ],
        };
    },
    methods: {
        isLastPlayer(player) {
            return player.id === this.players.length;
        },
    },
};
</script>

<style scoped>
.turn-component {
    display: flex;
    align-items: center;
    background-color: #ccc;
    width: 380px;
    height: 50px;
    margin-top: 22.5px;
    border-radius: 5px;
}

.player {
    display: flex;
    align-items: center;
}

.player.active .player-number {
    transform: scale(1.5);
}

.player-number {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    font-weight: bold;
    margin: 0 14px;
    transition: transform 0.3s;
}
</style>
