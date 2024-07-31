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
import socketHandler from '@/components/chatting/socketHandler';

export default {
    name: 'MiddleComponent',
    data() {
        return {
          room: '',
          socket: null,
        };
    },
  computed: {
    userId() {
      const user = this.$store.getters.getUser;
      return user ? user.userId : null;
    },
  },
  methods: {
    joinRoom() {
      const refreshToken = this.$store.getters.refreshToken;
      if (this.room.trim() !== '' && refreshToken) {
        if (!this.socket) {
          this.socket = socketHandler.connectToServer('http://localhost:3000'); // 서버 URL을 적절히 변경
        }
        socketHandler.joinRoom(this.socket, this.room, refreshToken, this.userId);
        console.log(`Joining room: ${this.room} with userId: ${this.userId}`);
      } else {
        console.error('Room name or refresh token is missing');
      }
    },
  },
  provide() {
    return {
      socket: () => this.socket,
    };
  },
  created() {
    if (!this.socket) {
      this.socket = socketHandler.connectToServer('http://localhost:3000'); // 서버 URL을 적절히 변경
    }
    this.$store.dispatch('fetchUser').then(() => {
      console.log('User fetched:', this.userId);
    });
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
    padding: 20px;
    box-sizing: border-box;
}

.side-content {
    flex: 1;
    padding: 50px;
    box-sizing: border-box;
}
</style>
