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
                <router-view v-if="!isVoteComponent" name="draw"></router-view>
                <router-view v-else name="vote"></router-view>
            </div>
            <div class="side-content">
                <router-view v-if="!isChatSideComponent" name="side"></router-view>
                <router-view v-else name="side_chat"></router-view>
            </div>
        </div>
    </div>
</template>

<script>
import { EventBus } from '@/utils/eventBus';

export default {
    name: 'MiddleComponent',
    data() {
        return {
            room: '',
            isChatSideComponent: false,
            isVoteComponent: false,
            socket: this.$socket,
        };
    },
    provide() {
      return {
        socket: () => this.socket, // 소켓 인스턴스를 제공
      };
    },
    watch: {
      socket(newSocket) {
        if (newSocket) {
          this.$forceUpdate(); // 소켓이 생성되면 컴포넌트를 강제로 업데이트
        }
      },
    },
    created() {
        this.socket = this.$socket;
        EventBus.$on('voteStart', this.changeSideComponent);
    },
    methods: {
        joinRoom() {
            this.$socket.emit('joinRoom', 'TestRoom', 23432, 'sadafafaf');
        },
        changeSideComponent() {
            this.isChatSideComponent = true;
            this.isVoteComponent = true;
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
