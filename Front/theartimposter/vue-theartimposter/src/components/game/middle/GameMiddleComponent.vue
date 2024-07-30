<template>
    <div class="middle">
        <div class="header">
            <div class="room-input">
                <label for="room">Room:</label>
                <input v-model="room" id="room" placeholder="Enter room name" />
                <button @click="joinRoom">Join Room</button>
            </div>
        </div>
        <div class="content">
            <div class="main-content">
                <router-view name="main"></router-view>
            </div>
            <div class="side-content">
                <router-view name="side"></router-view>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MiddleComponent',
    data() {
        return {
            room: '',
        };
    },
    methods: {
        joinRoom() {
            if (this.room.trim() !== '') {
                this.$root.$emit('joinRoom', this.room);
                console.log(this.room);
            }
        },
    },
};
</script>

<style scoped>
.middle {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
}

.header {
    width: 100%;
    height: 50px;
    display: flex;
    justify-content: center;
    margin: 10px 5px; /* 상단 마진을 줄이기 위해 수정 */
}

.room-input {
    display: flex;
    align-items: center;
    gap: 5px;
}

.content {
    display: flex;
    justify-content: center;
    width: 100%;
}

.main-content {
    flex: 3;
    border-right: 1px solid #ccc;
    box-sizing: border-box;
}

.side-content {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px solid black;
    background-color: #ffffff;
}
</style>
