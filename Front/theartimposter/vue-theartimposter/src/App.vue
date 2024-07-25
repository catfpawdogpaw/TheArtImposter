<template>
    <div id="app">
        <!-- <draw-pad></draw-pad> -->
        <header-component></header-component>
        <router-view name="middle"></router-view>
        <footer-component></footer-component>
    </div>
</template>

<script>
import FooterComponent from "@/components/FooterComponent.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";

export default {
    name: "App",
    components: {
        HeaderComponent,
        FooterComponent,
    },
};
</script>

<style>
@import "assets/base.css";
</style>
