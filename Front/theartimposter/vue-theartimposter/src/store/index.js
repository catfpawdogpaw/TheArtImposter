import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    player: {},
    otherPlayerList: [],
    gameRoom: {},
    gameSetting: {},
    subjectList: []
  },
  mutations: {
    SET_RECEIVED_DATA(state, data) {
      console.log('Setting received data in mutation:', data);
      state.player = data.player;
      state.otherPlayerList = data.otherPlayerList;
      state.gameRoom = data.gameRoom;
      state.gameSetting = data.gameSetting;
      state.subjectList = data.subjectList;
    }
  },
  actions: {
    updateReceivedData({ commit }, data) {
      console.log('Updating received data in action:', data);
      commit('SET_RECEIVED_DATA', data);
    }
  },
  modules: {}
});
