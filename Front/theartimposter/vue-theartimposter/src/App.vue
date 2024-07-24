<template>
    <div id="app">
        <DrawPad></DrawPad>
    </div>
</template>

<script>
import DrawPad from "./components/drawpad/DrawPad.vue";

export default {
    name: "App",
    components: {
        DrawPad,
    },
};
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
}
</style>
