<template>
    <div class="header">
      <div></div>
      <div class="title">
        <h1>The Art Imposter</h1>
      </div>
      <div class="auth-button" v-if="user">
        <button @click="logout">로그아웃</button>
      </div>
      <div v-else>
        <button class="auth-button" @click="login">로그인</button>
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
    ...mapActions(['logout']),
    login() {
      // 같은 경로에 있을 때 다시 경로를 이동하면 버그가 생겨서 if 처리
      if (this.$route.path !== '/login'){
        this.$router.push('/login');
      }
    },
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
  display: flex;
  margin-top: 100px;
  transform: translate(-50px, -30px); /* 원하는 만큼 이동 */
  padding: 10px;
  color: #fff;
  background-color: #273c75;
  border: none;
}
</style>
