<template>
    <div>
        <span class="sub">Join User</span>
        <br />
        <br />
        <div class="game-side-bar">
            <GameSideUserCardComponent v-for="user in users" :key="user.id" :user="user" />
        </div>
    </div>
</template>

<script>
import GameSideUserCardComponent from './user-card/GameSideUserCardComponent.vue';

export default {
    name: 'GameSideComponent',
    components: {
        GameSideUserCardComponent,
    },
    data() {
        return {
            users: [
                {
                    id: 1,
                    profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickname: '바람처럼 스쳐가는',
                    score: '1점',
                    isMe: false,
                    number: null,
                },
                {
                    id: 2,
                    profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickname: '정열과 낭만아',
                    score: '2점',
                    isMe: false,
                    number: null,
                },
                {
                    id: 3,
                    profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickname: '아직도',
                    score: '0점',
                    isMe: false,
                    number: null,
                },
                {
                    id: 4,
                    profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickname: '내겐',
                    score: '0점',
                    isMe: false,
                    number: null,
                },
                {
                    id: 5,
                    profilePicture: require('@/assets/user/image/profile/u1.png'),
                    nickname: '거췬 꿈이 이쒀~',
                    score: '2점',
                    isMe: true,
                    number: 2,
                },
            ],
        };
    },
};
</script>

<style scoped>
.sub {
    font-size: 30px;
}
/* .game-side-bar {
} */
</style>
