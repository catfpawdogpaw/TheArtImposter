<template>
    <div class="middle">
<!--        <div class="header">-->
<!--            <div class="room-input" v-if="isAuthenticated">-->
<!--                <label for="room">Room:</label>-->
<!--                <input v-model="room" id="room" placeholder="Enter room name" />-->
<!--                <button @click="joinRoom">Join Room</button>-->
<!--            </div>-->
<!--            <div v-else>-->
<!--                <p>Please log in to join a room.</p>-->
<!--            </div>-->
<!--        </div>-->
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
        isAuthenticated() {
            return this.$store.getters.isAuthenticated;
        },
    },
    methods: {
        joinRoom() {
            console.log('조인 버튼 누름');
            const refreshToken = this.$store.getters.refreshToken;
            if (this.room.trim() !== '' && refreshToken) {
                if (!this.socket) {
                    this.socket = socketHandler.connectToServer('http://localhost:3000'); // 서버 URL을 적절히 변경
                }
                console.log('소켓' + this.socket);
                socketHandler.joinRoom(this.socket, this.room, refreshToken, this.userId);
                console.log(`Joining room: ${this.room} with userId: ${this.userId}`);
            } else {
                console.error('Room name or refresh token is missing');
            }
        },
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
        if (!this.socket) {
            this.socket = socketHandler.connectToServer('http://localhost:3000'); // 서버 URL을 적절히 변경
        }
        const accessToken = this.$store.getters.refreshToken;
        if (accessToken) {
            this.$store
                .dispatch('fetchUser')
                .then(() => {
                    console.log('User fetched:', this.userId);
                    if (!this.socket) {
                        this.socket = socketHandler.connectToServer('http://localhost:3000'); // 서버 URL을 적절히 변경
                    }
                })
                .catch(() => {
                    console.error('Failed to fetch user information');
                });
        } else {
            console.error('Access token is missing');
        }
    },
};
</script>

<style scoped>
.middle {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
  background-image: url('https://skribbl.io/img/background.png');

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
    padding: 50px;
    box-sizing: border-box;
}

.side-content {
    flex: 1;
    padding: 50px;
    box-sizing: border-box;
    height: 700px;
    margin-top: 50px;
}
</style>
