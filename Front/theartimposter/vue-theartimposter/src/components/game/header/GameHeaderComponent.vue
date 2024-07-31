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
            console.log('user' + user);
            //this.$socket.emit('joinRoom', 'TestRoom', user.userId, this.$store.getters.refreshToken);
            this.$socket.emit('joinRoom', 'TestRoom', 23432, 'sadafafaf');
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
