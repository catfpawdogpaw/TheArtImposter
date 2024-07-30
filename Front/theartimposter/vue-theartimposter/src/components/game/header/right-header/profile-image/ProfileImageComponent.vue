<template>
    <div class="profile-image">
        <img :src="profileImageUrl" alt="Profile Image" />
    </div>
</template>

<script>
// import { mapGetters } from 'vuex';

export default {
  name: 'ProfileImageComponent',
  computed: {
    // ...mapGetters(['getUser']), // Vuex의 getUser getter를 계산된 속성으로 가져옵니다.
    profileImageUrl() {
      // 콘솔에 getUser 결과를 출력하여 확인합니다.
      console.log(this.getUser);
      // getUser 결과가 존재하고 profileImageUrl 속성이 있는 경우 해당 값을 사용합니다.
      return this.$store.getters.getUser && this.$store.getters.getUser.profileImageUrl ? this.$store.getters.getUser.profileImageUrl : require('@/assets/user/image/profile/u1.png');
    }
  },
  created() {
    // 컴포넌트가 생성될 때 fetchUser 액션을 호출합니다.
    this.$store.dispatch('fetchUser');
  }
};
</script>

<style scoped>
.profile-image {
    width: 50px;
    height: 50px;
    position: relative;
    margin: 0;
}

.profile-image img {
    width: 130%;
    height: 130%;
    border-radius: 50%;
    object-fit: cover;
}
</style>
