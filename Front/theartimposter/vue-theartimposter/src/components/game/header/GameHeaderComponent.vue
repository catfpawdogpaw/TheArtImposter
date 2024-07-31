<template>
    <div class="game-header" style="display: flex">
        <div class="left-content">
            <router-view name="left_game_header"></router-view>
        </div>
        <div class="right-content">
            <router-view name="right_game_header"></router-view>
        </div>
    </div>
</template>

<script>
export default {
    created() {
        this.$store.dispatch('fetchUser').then((user) => {
            console.log('내가 원하는 순서 2');
            console.log(user.userId);
            this.$socket.emit('joinRoom', 'TestRoom', this.$store.getters.refreshToken, user.userId);
        });
    },
};
</script>

<style scoped>
.left-content {
    flex: 3;
}
.right-content {
    flex: 1;
}
</style>
