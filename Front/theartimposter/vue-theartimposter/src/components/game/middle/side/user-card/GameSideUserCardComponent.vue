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
    background-color: #ffffff;
    border: 1px solid #7a7a7a;
    margin: 0 0 20px;
    width: 300px;
    height: 100px;
}

.profile-picture {
    position: relative;
    margin-right: 10px;
}

.profile-picture img {
    width: 75px;
    height: 75px;
    border-radius: 50%;
    margin-left: 12.5px;
}

.user-number {
    position: absolute;
    bottom: -5px;
    right: -15px;
    background-color: #ff00ff;
    color: white;
    border-radius: 50%;
    padding: 5px;
    width: 35px;
    height: 35px;
}

.user-info {
    flex-direction: column;
}

.nickname {
    text-align: left;
    font-weight: bold;
    margin-left: 15px;
    margin-bottom: 10px;
    scale: 1.2;
}

.score {
    text-align: left;
    color: #999;
    margin-left: 120px;
}
</style>
