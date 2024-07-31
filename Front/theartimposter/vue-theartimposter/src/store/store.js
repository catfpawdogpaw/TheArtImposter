import Vue from "vue";
import axios from "@/plugins/axios";
import {eraseCookie, getCookie} from "@/utils/cookie";
import Vuex from "vuex"; // 쿠키 유틸리티 함수 가져오기

Vue.use(Vuex);
export default new Vuex.Store({
    state: {
        accessToken: localStorage.getItem('access_token') || '',
        isAuthenticated: !!localStorage.getItem('access_token'),
        user: null, // 유저 정보 필드
        gameRoomStatus: null, // 게임 룸 상태 필드
        players: [], // 유저 목록 필드 추가
        turnPlayer: null,
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
                    commit('setUser', response.data);
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
    },
    getters: {
        getUser: (state) => state.user,
        isAuthenticated: (state) => state.isAuthenticated,
        getGameRoomStatus: (state) => state.gameRoomStatus, // 게임 룸 상태 getter 추가
        getPlayers: (state) => state.players, // 플레이어 목록 getter 추가
        getTurnPlayer: (state) => state.turnPlayer, // getTurnPlayer getter 추가
        refreshToken: () => getCookie('refresh_token')
    },
});
