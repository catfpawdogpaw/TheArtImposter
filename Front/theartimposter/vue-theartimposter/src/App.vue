<template>
    <div id="app">
        <!-- <draw-pad></draw-pad> -->
        <header-component></header-component>
        <router-view name="middle"></router-view>
        <footer-component></footer-component>
        <!--<test-data-render-component></test-data-render-component>-->
    </div>
</template>

<script>
import FooterComponent from "@/components/FooterComponent.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
//import TestDataRenderComponent from "@/components/test-vue/TestDataRenderComponent.vue";

export default {
    name: "App",
    components: {
        HeaderComponent,
        FooterComponent,
//TestDataRenderComponent,
    },
};
</script>

<style>
@import "assets/base.css";
</style>
