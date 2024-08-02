import { EventBus } from '@/utils/eventBus';
import io from 'socket.io-client';

export default {
    install(Vue, { store }) {
        console.log('소켓 저장');
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

        socket.on('playerJoined', (data) => {
            console.log('playerJoined:', data);
            store.dispatch('addPlayer', data); // Vuex 스토어에 유저 추가
        });

        socket.on('gameInfo', (data) => {
            console.log('gameInfo:', data);
            store.dispatch('updateGameInfo', data);
            console.log('settingGamePlayers - 이벤트 버스 출발');
            EventBus.$emit('settingGamePlayers'); // gameInfo 변경 시 이벤트 발행, 교체되는 컴포
            EventBus.$emit('settingGamePlayers2'); // gameInfo 변경 시 이벤트 발행, 유지 되는 컴포
            EventBus.$emit('settingGame'); // gameInfo 변경 시 이벤트 발행
        });

        socket.on('turnStart', (data) => {
            console.log('turnStart:', data);
            store.dispatch('updateTurnPlayer', data);
        });

        socket.on('voteStart', (data) => {
            console.log('voteStart:', data);
            EventBus.$emit('voteStart', data);
        });

        socket.on('roundEnd', (data) => {
            console.log('roundEnd:', data);
            EventBus.$emit('waitRoundEnd', data);
            //EventBus.$emit('roundEnd', data); 이거 두개는 저기서 하자, 그 결과창 끝날 때
            //socket.emit('GameRoomStatus'); 라운드 시작하는 부분
        });

        socket.on('voteResult', (data) => {
            console.log('voteResult:', data);
            store.dispatch('updateVoteResult', data);
            EventBus.$emit('voteResult', data);
        });

        socket.on('fakeArtistGuessStart', (data) => {
            console.log('fakeArtistGuessStart:', data);
            EventBus.$emit('fakeArtistGuessStart', data);
        });

        socket.on('fakeArtistGuessResult', (data) => {
            console.log('fakeArtistGuessResult:', data);
            EventBus.$emit('fakeArtistGuessResult', data);
        });

        socket.on('disconnect', () => {
            console.log('Socket disconnected');
        });
    },
};
