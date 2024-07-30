<template>
  <div class="store-tokens">
    <h1>Storing Tokens...</h1>
  </div>
</template>

<script>
export default {
  computed: {
    isAuthenticated() {
      return this.$store.state.isAuthenticated;
    },
    user() {
      return this.$store.state.user;
    }
  },
  mounted() {
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get('access');
    const refreshToken = urlParams.get('refresh');

    if (accessToken && refreshToken) {
      // 로컬 스토리지에 access token 저장
      localStorage.setItem('access_token', accessToken);

      // 쿠키에 refresh token 저장
      document.cookie = `refresh_token=${refreshToken};`;

      // 유저 정보 가져오기
      this.$store.dispatch('fetchUser').then(() => {
        // 리다이렉트
        this.$router.push('/');
      }).catch(error => {
        console.error('Failed to fetch user:', error);
      });
    } else {
      console.error('Tokens not found in URL');
    }
  }
}
</script>
