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

        socket.on('GameRoomStatus', (data) => {
            console.log('GameRoomStatus:', data);
            store.dispatch('updateGameRoomStatus', data);
        });

        socket.on('userJoined', (data) => {
            console.log('userJoined:', data);
            store.dispatch('addPlayer', data); // Vuex 스토어에 유저 추가
        });

        socket.on('turnStart', (data) => {
            console.log('turnStart:', data);
            store.dispatch('updateTurnPlayer', data);
        });

        socket.on('disconnect', () => {
            console.log('Socket disconnected');
        });
    },
};
