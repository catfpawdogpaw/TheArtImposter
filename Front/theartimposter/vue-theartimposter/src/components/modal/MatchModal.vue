<template>
    <div v-if="show" class="modal-overlay">
        <div class="modal-content">
            <h2>매칭중</h2>
            <p>잠시 후 게임이 시작됩니다.</p>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        show: Boolean,
    },
};
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    text-align: center;
}
</style>
