<template>
    <div class="middle">
        <div class="main-content">
            <router-view name="main"></router-view>
        </div>
        <div class="side-content">
            <router-view name="side"></router-view>
        </div>
    </div>
</template>

<script>
export default {
    name: "MiddleComponent",
};
</script>

<style scoped>
.main-content {
    flex: 3;
}
.side-content {
    flex: 1;
    border: 1px solid black;
}
</style>
