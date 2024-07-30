<template>
    <div class="category-component">
        <span class="category-span">만화 캐릭터</span>
    </div>
</template>

<script>
export default {
    name: 'CategoryComponent',
};
</script>

<style scoped>
.category-component {
    display: flex;
    width: 90px;
    height: 50px;
    margin-top: 22.5px;
    align-items: center;
    background-color: #ccc;
    border-radius: 5px;
}

.category-span {
    margin: 0 auto;
}
</style>
