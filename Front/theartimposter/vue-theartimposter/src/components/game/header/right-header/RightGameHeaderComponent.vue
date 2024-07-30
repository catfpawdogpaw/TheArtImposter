<template>
    <div class="right-game-header">
        <div class="t">
            <div class="profile-container">
                <profile-image-component />
                <my-turn-component />
            </div>
            <user-info-component />
        </div>
    </div>
</template>

<script>
import MyTurnComponent from './my-turn/MyTurnComponent.vue';
import ProfileImageComponent from './profile-image/ProfileImageComponent.vue';
import UserInfoComponent from './user-info/UserInfoComponent.vue';

export default {
    components: { MyTurnComponent, ProfileImageComponent, UserInfoComponent },
};
</script>

<style scoped>
.right-game-header {
    display: flex;
    align-items: center;
    height: var(--h-game-header-height);
    margin-bottom: var(--h-game-header-margin-bottom);
    border: 1px solid black;
}

.t {
    margin-left: 30px;
    display: flex;
    align-items: center;
    gap: 10px;
}

.profile-container {
    position: relative;
    display: inline-block;
    margin-bottom: 12.5px;
}
</style>
