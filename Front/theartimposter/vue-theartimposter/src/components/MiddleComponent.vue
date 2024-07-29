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
    name: 'MiddleComponent',
};
</script>

<style scoped>
.main-content {
    flex: 3;
    border: 1px solid black;
}
.side-content {
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid black;
  background-color: #f4f4f4;
}
</style>
