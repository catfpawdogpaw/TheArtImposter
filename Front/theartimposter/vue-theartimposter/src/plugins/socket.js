import io from 'socket.io-client';

export default {
  install(Vue, { store }) {
    const socket = io('http://localhost:3000');
    Vue.prototype.$socket = socket;

    socket.on('connect', () => {
      console.log('Socket connected');
    });

    socket.on('receiveData', (data) => {
      console.log('Data received in socket plugin:', data);
      // Vuex 액션 호출 등 데이터 처리
      store.dispatch('updateReceivedData', data);
    });

    socket.on('disconnect', () => {
      console.log('Socket disconnected');
    });
  },
};
