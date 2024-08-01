import Vue from 'vue';
import axios from '@/plugins/axios';
import { eraseCookie, getCookie } from '@/utils/cookie';
import Vuex from 'vuex'; // 쿠키 유틸리티 함수 가져오기
import { EventBus } from '@/utils/eventBus'; // 이벤트 버스 가져오기

Vue.use(Vuex);
export default new Vuex.Store({
    state: {
        accessToken: localStorage.getItem('access_token') || '',
        isAuthenticated: !!localStorage.getItem('access_token'),
        user: null, // 유저 정보 필드
        gameRoomStatus: null, // 게임 룸 상태 필드
        players: [], // 유저 목록 필드 추가
        turnPlayer: null,
        myInfo: null,
        otherPlayers: [],
    },
    mutations: {
        setAccessToken(state, token) {
            state.accessToken = token;
            state.isAuthenticated = true;
            localStorage.setItem('access_token', token);
        },
        setUser(state, user) {
            // 유저 정보를 저장하는 mutation 추가
            state.user = user;
        },
        logout(state) {
            state.accessToken = '';
            state.isAuthenticated = false;
            state.user = null;
            localStorage.removeItem('access_token');
            eraseCookie('refresh_token');
        },
        setGameRoomStatus(state, status) {
            // 게임 룸 상태를 저장하는 mutation 추가
            state.gameRoomStatus = status;
            state.players = status.players;
        },
        addPlayer(state, player) {
            // 유저를 추가하는 mutation 추가
            state.players.push(player);
        },
        setTurnPlayer(state, player) {
            // setTurnPlayer 뮤테이션 추가
            state.turnPlayer = player;
            EventBus.$emit('turnPlayerChanged', player); // turnPlayer 변경 시 이벤트 발행
        },
        setGameInfo(state, gameInfo) {
            // state.myInfo = gameInfo.myInfo;
            // state.otherPlayers = gameInfo.otherPlayers;

            // turn 값을 1씩 증가시킴
            const incrementTurn = (player) => ({
                ...player,
                turn: player.turn + 1,
            });
            state.myInfo = incrementTurn(gameInfo.myInfo);
            state.otherPlayers = gameInfo.otherPlayers.map(incrementTurn);
            EventBus.$emit('settingGamePlayers', state); // gameInfo 변경 시 이벤트 발행
        },
    },
    actions: {
        login({ commit }, { accessToken }) {
            commit('setAccessToken', accessToken);
        },
        fetchUser({ commit }) {
            return axios
                .get('/user/me')
                .then((response) => {
                    console.log('내가 원하는 순서 0');
                    commit('setUser', response.data);
                    console.log('내가 원하는 순서 1');
                    console.log(response.data);
                    return response.data; // Promise 반환
                })
                .catch(() => {
                    commit('logout');
                });
        },
        logout({ commit }) {
            return axios
                .post('/user/logout', {}, { withCredentials: true }) // withCredentials 추가
                .then(() => {
                    commit('logout');
                })
                .catch((error) => {
                    console.error('Logout failed:', error);
                    if (error.response && error.response.status === 403) {
                        commit('logout');
                    }
                });
        },
        refreshToken({ commit }) {
            return axios
                .post('/reissue', {}, { withCredentials: true })
                .then((response) => {
                    const { access, refresh } = response.data;
                    console.log(`accessToken: ${access}, refreshToken: ${refresh}`);
                    commit('setAccessToken', access);
                    // Optional: Set new refresh token as cookie if necessary
                    document.cookie = `refresh=${refresh};path=/;SameSite=Lax;`;
                    return access;
                })
                .catch((error) => {
                    commit('logout');
                    return Promise.reject(error);
                });
        },
        updateGameRoomStatus({ commit }, status) {
            // 게임 룸 상태를 업데이트하는 action 추가
            commit('setGameRoomStatus', status);
        },
        addPlayer({ commit }, player) {
            // 유저를 추가하는 action 추가
            commit('addPlayer', player);
        },
        updateTurnPlayer({ commit }, player) {
            // updateTurnPlayer 액션 추가
            commit('setTurnPlayer', player);
        },
        updateGameInfo({ commit }, gameInfo) {
            // updateGameInfo 액션 추가
            commit('setGameInfo', gameInfo);
        },
    },
    getters: {
        getUser: (state) => state.user,
        isAuthenticated: (state) => state.isAuthenticated,
        getGameRoomStatus: (state) => state.gameRoomStatus, // 게임 룸 상태 getter 추가
        getPlayers: (state) => state.players, // 플레이어 목록 getter 추가
        getTurnPlayer: (state) => state.turnPlayer, // getTurnPlayer getter 추가
        refreshToken: () => getCookie('refresh_token'),
        getMyInfo: (state) => state.myInfo,
        getOtherPlayers: (state) => state.otherPlayers,
    },
});
