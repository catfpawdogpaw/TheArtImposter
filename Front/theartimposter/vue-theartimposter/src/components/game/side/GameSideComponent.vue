<template>
    <div class="game-side-bar">
        <GameSideUserCardComponent v-for="user in users" :key="user.id" :user="user" />
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
                    profilePicture: '../assets/user/image/profile/u1.png',
                    nickname: '닉네임1',
                    score: '점수1',
                    isMe: false,
                    number: null,
                },
                {
                    id: 2,
                    profilePicture: '@/assets/user/image/profile/u1.png',
                    nickname: '닉네임2',
                    score: '점수2',
                    isMe: false,
                    number: null,
                },
                {
                    id: 3,
                    profilePicture: '@/assets/user/image/profile/u1.png',
                    nickname: '닉네임3',
                    score: '점수3',
                    isMe: false,
                    number: null,
                },
                {
                    id: 4,
                    profilePicture: '@/assets/user/image/profile/u1.png',
                    nickname: '닉네임4',
                    score: '점수4',
                    isMe: false,
                    number: null,
                },
                {
                    id: 5,
                    profilePicture: '@/assets/user/image/profile/u1.png',
                    nickname: '닉네임5',
                    score: '점수5',
                    isMe: true,
                    number: 2,
                },
            ],
        };
    },
};
</script>

<style scoped>
.game-side-bar {
    width: 300px;
    border: 1px solid #ddd;
    padding: 10px;
    background-color: #f9f9f9;
}
</style>
