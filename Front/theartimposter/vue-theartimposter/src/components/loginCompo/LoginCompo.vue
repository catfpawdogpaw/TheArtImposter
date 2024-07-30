<template>
  <div class="login">
    <template v-if="isAuthenticated">
<!--      <h1>Welcome, {{ user.name }}!</h1>-->
<!--      <p>Email: {{ user.email }}</p>-->
      <button @click="logout">Logout</button>
    </template>
    <template v-else>
      <h1>Login</h1>
      <div class="login-container">
        <button class="login-btn naver" @click="loginWithOAuth2('naver')">
          <img src="@/assets/naverBtn.png" alt="Login with Naver">
        </button>
        <button class="login-btn google" @click="loginWithOAuth2('google')">
          <img src="@/assets/googleBtn.png" alt="Login with Google">
        </button>
        <button class="login-btn kakao" @click="loginWithOAuth2('kakao')">
          <img src="@/assets/kakaoBtn.png" alt="Login with Kakao">
        </button>
      </div>
    </template>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';

export default {
  computed: {
    ...mapState({
      isAuthenticated: state => state.isAuthenticated,
      user: state => state.user
    })
  },
  methods: {
    ...mapActions(['logout']),
    loginWithOAuth2(provider) {
      window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
    }
  },
  created() {
    if (this.isAuthenticated && !this.user) {
      this.$store.dispatch('fetchUser');
    }
  }
}
</script>

<style scoped>
.login-container {
  text-align: center;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.login-btn {
  display: block;
  width: 200px;
  padding: 10px;
  margin: 10px auto;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: transparent; /* 배경 투명화 */
  transition: transform 0.3s; /* 애니메이션 추가 */
}

.login-btn img {
  width: 100%;
  height: auto;
  border-radius: 5px; /* 이미지의 둥근 모서리 추가 */
}
</style>
