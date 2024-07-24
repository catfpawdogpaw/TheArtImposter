<template>
    <div id="app">
        <draw-pad></draw-pad>
        <header-component></header-component>
        <middle-component></middle-component>
        <footer-component></footer-component>
    </div>
</template>

<script>
import DrawPad from "@/components/drawpad/DrawPad.vue";
import FooterComponent from "@/components/FooterComponent.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";

export default {
    name: "App",
    components: {
        DrawPad,
        HeaderComponent,
        MiddleComponent,
        FooterComponent,
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
