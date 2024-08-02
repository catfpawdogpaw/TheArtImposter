<template>
  <div class="user-Info">
    <!-- 유저 정보 -->
    <div>
      <img :src=user.profileImageUrl alt="유저 프로필 사진">
    </div>
    <div class="user-detail" v-if="user">
      <!-- 추가적인 사용자 정보 표시 -->
      <p><strong>{{user.nickname}}</strong></p>
      <p><strong>{{user.socialId}}</strong></p>
      <div class="user-count">
        <p><strong>총 게임 수: {{user.gameCnt}}</strong></p>
        <p><strong>승리 게임 수: {{user.vicCnt}}</strong></p>
      </div>
    </div>
    <div v-else>
      <p>Loading user information...</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserCompo',
  computed: {
    user() {
      return this.$store.getters.getUser;
    },
  },
  created() {
    this.$store.dispatch('fetchUser');
  },
}
</script>

<style scoped>
/* 스타일을 여기에 추가하세요 */
.user-Info {
  border: 1px solid black;
  max-width: 500px;
  min-width: 400px;
  margin: 0 auto;
  align-items: center;

}
img {
  width: 130px;
  border-radius: 50%;
  margin-top: 70px;
}

.user-detail {
  margin-top: 50px;
  border: 1px solid black;
}

.user-count{
  display: inline;
}
</style>
