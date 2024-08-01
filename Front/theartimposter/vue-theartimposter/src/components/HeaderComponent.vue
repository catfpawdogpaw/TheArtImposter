<template>
    <div class="header">
      <div class="title">
        <h1>The Art Imposter
        </h1>
        <div class="auth-button" v-if="user">
          <button @click="logout">로그아웃</button>
        </div>
        <div v-else>
          <button class="auth-button" @click="login">로그인</button>
        </div>
      </div>

    </div>
</template>

<script>
import {mapActions, mapState} from "vuex";

export default {
  computed: {
    ...mapState({
      user: state => state.user,
    })
  },
  methods: {
    ...mapActions(['logout','login']),
  },
};
</script>

<style scoped>
.title {
  display: flex; /* flex 컨테이너로 설정 */
  flex-direction: row; /* 자식 요소들을 행(row) 방향으로 배치 */
  align-items: center; /* 수직 중앙 정렬 */
  justify-content: center;
}
.auth-button {
  margin-left: auto; /* 오른쪽 끝으로 밀어냄 */
  align-self: flex-end; /* 아래쪽으로 정렬 */
}
</style>
