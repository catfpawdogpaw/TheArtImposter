<template>
    <div class="category-component">
        <span class="category-span"
            ><strong class="font">{{ category }}</strong></span
        >
    </div>
</template>

<script>
export default {
    name: 'CategoryComponent',
    computed: {
        category() {
            return this.$store.getters.getMyInfo ? this.$store.getters.getMyInfo.category : '?';
        },
    },
};
</script>

<style scoped>
.category-component {
    display: flex;
    width: 90px;
    height: 50px;
    margin-top: 22.5px;
    align-items: center;
    background-color: #464e69;
    border-radius: 5px;
}

.category-span {
    margin: 0 auto;
}

.font {
    color: #dadada;
}
</style>
