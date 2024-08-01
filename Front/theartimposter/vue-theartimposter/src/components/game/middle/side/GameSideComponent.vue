<template>
    <div>
        <span class="sub">Join User</span>
        <br />
        <br />
        <div class="game-side-bar">
            <GameSideUserCardComponent v-for="user in playersToShow" :key="user.id" :user="user" />
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import GameSideUserCardComponent from './user-card/GameSideUserCardComponent.vue';
import { EventBus } from '@/utils/eventBus';

export default {
    name: 'GameSideComponent',
    components: {
        GameSideUserCardComponent,
    },
    computed: {
        ...mapGetters(['getPlayers']),
        playersToShow() {
            const players = this.getPlayers.length > 0 ? this.getPlayers : [];

            console.log('방 players 수 :' + players.length);

            const storeMyRoomNumber = this.$store.getters.getMyRoomNumber;
            console.log('내 방 번호 :' + storeMyRoomNumber);

            //첫번째는 확정 안됨, 나머지는 간헐적 안됨
            if (storeMyRoomNumber == null && players.length > 0) {
                let myNumber = players.length - 1;
                players[myNumber] = {
                    ...players[myNumber],
                    isMe: true,
                    color: this.myColor,
                };
                console.log('저장 할 내 방 번호 :' + myNumber);
                this.setMyNumber(myNumber);
            } else if (storeMyRoomNumber != null) {
                console.log('저장 된 내 방 번호 :' + storeMyRoomNumber);
                players[storeMyRoomNumber] = {
                    ...players[storeMyRoomNumber],
                    isMe: true,
                    color: this.myColor,
                };
            }

            // 스토어에서 받아온 유저 데이터가 있으면 그것을 사용하고, 그렇지 않으면 더미 데이터를 사용
            const dummyPlayers = this.dummyPlayers.slice(players.length);
            return players.concat(dummyPlayers).slice(0, 5);
        },
    },
    data() {
        return {
            myColor: null,
            dummyPlayers: [
                {
                    id: 1,
                    profileImage: require('@/assets/user/image/profile/u1.png'),
                    // profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickName: 'Empty',
                    curScore: 0,
                    isMe: false,
                    number: null,
                },
                {
                    id: 2,
                    profileImage: require('@/assets/user/image/profile/u1.png'),
                    // profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickName: 'Empty',
                    curScore: 0,
                    isMe: false,
                    number: null,
                },
                {
                    id: 3,
                    profileImage: require('@/assets/user/image/profile/u1.png'),
                    // profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickName: 'Empty',
                    curScore: 0,
                    isMe: false,
                    number: null,
                },
                {
                    id: 4,
                    profileImage: require('@/assets/user/image/profile/u1.png'),
                    // profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickName: 'Empty',
                    curScore: 0,
                    isMe: false,
                    number: null,
                },
                {
                    id: 5,
                    profileImage: require('@/assets/user/image/profile/u1.png'),
                    // profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickName: 'Empty',
                    curScore: 0,
                    isMe: false,
                    number: null,
                },
            ],
        };
    },
    methods: {
        setMyNumber(myNumber) {
            this.$store.dispatch('updateMyRoomNumber', myNumber);
        },
        addPlayer() {
            this.playersToShow();
        },
    },
    created() {
        EventBus.$on('settingGamePlayers', () => {
            console.log('settingGamePlayers - 이벤트 버스 도착 - 사이드');
            this.myColor = this.$store.getters.getMyInfo.color;
            this.playersToShow;
        });
        EventBus.$on('addPlayer', this.addPlayer);
    },
    beforeDestroy() {
        EventBus.$off('settingGamePlayers');
        EventBus.$off('addPlayer');
    },
};
</script>

<style scoped>
.sub {
    font-size: 30px;
}
</style>
