<template>
    <div class="user-info">
        <div class="nickname">{{ nickname }}</div>
        <div class="stats">{{ wins }}승/{{ losses }}패 ({{ winRate }}%)</div>
    </div>
</template>

<script>
export default {
    name: 'UserInfoComponent',
    computed: {
        nickname() {
            return this.$store.getters.getUser ? this.$store.getters.getUser.nickname : '';
        },
    },

    data() {
        return {
            wins: 10, // 예시 값
            losses: 5, // 예시 값
            get winRate() {
                return this.wins + this.losses === 0 ? 0 : ((this.wins / (this.wins + this.losses)) * 100).toFixed(1);
            },
        };
    },
};
</script>

<style scoped>
.user-info {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    margin-left: 50px;
}

.nickname {
    font-size: 18px;
    font-weight: bold;
}

.stats {
    font-size: 14px;
    color: gray;
    margin-top: 15px;
    margin-left: 50px;
}
</style>
