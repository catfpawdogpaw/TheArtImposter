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

            if (this.isCheckMe == false && players.length > 0) {
                players[players.length - 1] = {
                    ...players[players.length - 1],
                    isMe: true,
                };
                this.setCheckMe(players.length - 1);
            } else if (this.isCheckMe == true) {
                players[this.myNumber] = {
                    ...players[this.myNumber],
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
            isCheckMe: false,
            myNumber: null,
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
        setCheckMe(myNumber) {
            this.isCheckMe = true;
            this.myNumber = myNumber;
        },
    },
    mounted() {
        EventBus.$on('settingGamePlayers', (state) => {
            this.myColor = state.myInfo.color;
            this.playersToShow;
        });
    },
};
</script>

<style scoped>
.sub {
    font-size: 30px;
}
</style>
