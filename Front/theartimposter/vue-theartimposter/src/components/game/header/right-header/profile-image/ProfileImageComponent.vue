<template>
    <div class="profile-image">
        <img :src="profileImageUrl" alt="Profile Image" />
    </div>
</template>

<script>
export default {
    name: 'ProfileImageComponent',
    data() {
        return {
            profileImageUrl: require('@/assets/user/image/profile/u1.png'), // 프로필 이미지 경로
        };
    },
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
