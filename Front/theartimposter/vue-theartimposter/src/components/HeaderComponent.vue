<template>
    <div class="header" style="display: flex">
        <h3>Header</h3>
        <router-view></router-view>
    </div>
</template>

<script>
export default {};
</script>

<style scoped>
h3 {
    width: 70%;
}

.info {
    flex: 1 0;
    /* 각각 */
    align-self: flex-end;
}
</style>
