<template>
    <div class="my-turn">
        <span class="turn-number" :style="userNumberStyle">{{ userTurnNumber }}</span>
    </div>
</template>

<script>
import { EventBus } from '@/utils/eventBus';

export default {
    name: 'MyTurnComponent',
    data() {
        return {
            userTurnNumber: '?',
            userNumberStyle: {
                backgroundColor: '#404040',
            },
        };
    },
    methods: {
        updateUserInfo(myInfo) {
            if (myInfo) {
                this.userTurnNumber = myInfo.turn;
                this.userNumberStyle = {
                    backgroundColor: myInfo.color,
                };
            } else {
                this.userTurnNumber = '?';
                this.userNumberStyle = {
                    backgroundColor: '#404040',
                };
            }
        },
    },
    mounted() {
        EventBus.$on('settingGamePlayers', (state) => {
            this.updateUserInfo(state.myInfo);
        });
    },
    beforeDestroy() {
        EventBus.$off('settingGamePlayers');
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
    background-color: #404040;
    color: white;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;
    font-size: 20px;
}
</style>
