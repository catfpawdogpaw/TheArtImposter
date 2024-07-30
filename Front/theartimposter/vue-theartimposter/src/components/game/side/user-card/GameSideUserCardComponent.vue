<template>
    <div class="user-card">
        <div class="profile-picture">
            <img :src="user.profilePicture" alt="Profile Picture" />
            <div v-if="user.isMe" class="user-number">{{ user.number }}</div>
        </div>
        <div class="user-info">
            <div class="nickname">{{ user.nickname }}</div>
            <div class="score">{{ user.score }}</div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GameSideUserCardComponent',
    props: {
        user: {
            type: Object,
            required: true,
        },
    },
};
</script>

<style scoped>
.user-card {
    display: flex;
    align-items: center;
    border: 1px solid #ddd;
    padding: 10px;
    margin: 5px 0;
}

.profile-picture {
    position: relative;
    margin-right: 10px;
}

.profile-picture img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
}

.user-number {
    position: absolute;
    bottom: -5px;
    right: -5px;
    background-color: #ff00ff;
    color: white;
    border-radius: 50%;
    padding: 5px;
}

.user-info {
    display: flex;
    flex-direction: column;
}

.nickname {
    font-weight: bold;
    margin-bottom: 5px;
}

.score {
    color: #999;
}
</style>
