<template>
  <div>
    <h1>Player Info</h1>
    <p>ID: {{ player.playerId }}</p>
    <p>Nickname: {{ player.nickName }}</p>
    <p>Profile Image: {{ player.profileImage }}</p>
    <p>Victory Count: {{ player.vicCnt }}</p>
    <p>Game Count: {{ player.gameCnt }}</p>

    <h1>Other Players</h1>
    <ul>
      <li v-for="player in otherPlayerList" :key="player.playerId">
        ID: {{ player.playerId }}, Nickname: {{ player.nickName }}, Profile Image: {{ player.profileImage }}
      </li>
    </ul>

    <h1>Game Room</h1>
    <p>ID: {{ gameRoom.gameRoomId }}</p>
    <p>Title: {{ gameRoom.title }}</p>

    <h1>Game Setting</h1>
    <p>Version: {{ gameSetting.version }}</p>
    <p>Turn Time Limit: {{ gameSetting.turnTimeLimit }}</p>
    <p>Round Time Limit: {{ gameSetting.roundTimeLimit }}</p>
    <p>Min People: {{ gameSetting.minPeople }}</p>

    <h1>Subjects</h1>
    <ul>
      <li v-for="subject in subjectList" :key="subject.subjectId">
        ID: {{ subject.subjectId }}, Category: {{ subject.category }}, Subject: {{ subject.subject }}
      </li>
    </ul>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  computed: {
    ...mapState([
      'player',
      'otherPlayerList',
      'gameRoom',
      'gameSetting',
      'subjectList'
    ])
  },
  created() {
    console.log('Component created, state:', {
      player: this.player,
      otherPlayerList: this.otherPlayerList,
      gameRoom: this.gameRoom,
      gameSetting: this.gameSetting,
      subjectList: this.subjectList
    });
  },
  watch: {
    player(newVal) {
      console.log('Player updated:', newVal);
    },
    otherPlayerList(newVal) {
      console.log('OtherPlayerList updated:', newVal);
    },
    gameRoom(newVal) {
      console.log('GameRoom updated:', newVal);
    },
    gameSetting(newVal) {
      console.log('GameSetting updated:', newVal);
    },
    subjectList(newVal) {
      console.log('SubjectList updated:', newVal);
    }
  }
};
</script>

<style scoped>
/* 추가적인 스타일링 */
</style>
