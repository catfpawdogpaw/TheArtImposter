<template>
    <div id="app">
        <!-- <draw-pad></draw-pad> -->
        <router-view name="header"></router-view>
        <router-view name="middle"></router-view>
        <footer-component></footer-component>
    </div>
</template>

<script>
import FooterComponent from '@/components/FooterComponent.vue';
//import TestDataRenderComponent from "@/components/test-vue/TestDataRenderComponent.vue";

export default {
    name: 'App',
    components: {
        FooterComponent,
    },
};
</script>

<style>
@import 'assets/base.css';
</style>
