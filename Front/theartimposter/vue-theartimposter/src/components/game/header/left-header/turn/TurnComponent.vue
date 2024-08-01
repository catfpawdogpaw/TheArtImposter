<template>
    <div class="turn-component">
        <div
            v-for="(player, index) in displayedPlayers"
            :key="player.id || index"
            :class="{ player: true, active: player.turn === turnPlayerId }"
        >
            <div :style="{ backgroundColor: player.color }" class="player-number">{{ player.turn || index + 1 }}</div>
            <span v-if="index < displayedPlayers.length - 1">→</span>
        </div>
    </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import { EventBus } from '@/utils/eventBus';

export default {
    name: 'TurnComponent',
    data() {
        return {
            displayedPlayers: Array(5).fill({
                turn: null,
                color: '#808080',
            }),
        };
    },
    computed: {
        ...mapState(['turnPlayer']),
        ...mapGetters(['getTurnPlayer']),
        turnPlayerId() {
            return this.turnPlayer ? this.turnPlayer.turn : 1;
        },
    },
    methods: {
        updatePlayers(myInfo, otherPlayers) {
            const players = [...otherPlayers, myInfo];
            const maxPlayers = 5;

            players.forEach((player) => {
                player.color = player.color || '#808080';
            });

            players.sort((a, b) => (a.turn === null ? 1 : b.turn === null ? -1 : a.turn - b.turn));

            while (players.length < maxPlayers) {
                players.push({
                    turn: null,
                    color: '#808080',
                });
            }

            this.displayedPlayers = [...players.slice(0, maxPlayers)];
        },
        handleSettingGamePlayers() {
            console.log('settingGamePlayers2 - 이벤트 버스 도착 - 턴 컴포');
            this.updatePlayers(this.$store.getters.getMyInfo, this.$store.getters.getOtherPlayers);
        },
    },
    created() {
        EventBus.$on('settingGamePlayers2', this.handleSettingGamePlayers);
    },
    beforeDestroy() {
        EventBus.$off('settingGamePlayers2', this.handleSettingGamePlayers);
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
