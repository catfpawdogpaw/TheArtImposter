<template>
  <div></div>
</template>

<script>
export default {
  data() {
    return {
      socket: null,
    };
  },
  created() {
    this.connectWebSocket();
  },
  methods: {
    connectWebSocket() {
      this.socket = new WebSocket('ws://localhost:8080/ws');

      this.socket.onopen = () => {
        console.log('WebSocket connection opened');
      };

      this.socket.onmessage = (event) => {
        this.handleMessage(event.data);
      };

      this.socket.onclose = () => {
        console.log('WebSocket connection closed');
      };
    },
    handleMessage(data) {
      // 특정 데이터 수신 시 WebSocket 연결 끊기
      if (data === 'specific data') {
        this.disconnectWebSocket();
      }
    },
    disconnectWebSocket() {
      if (this.socket) {
        this.socket.close();
        console.log('WebSocket manually closed');
      }
    }
  },
  beforeDestroy() {
    if (this.socket) {
      this.socket.close();
    }
  }
};
</script>

<style scoped> 
</style>
