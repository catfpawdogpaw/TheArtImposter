<!--
<template>
    <div class="left-game-header">
        <div class="t">
            <h1>왼쪽헤더</h1>
            <div>
                <count-component></count-component>
            </div>
            <div>
                <turn-component></turn-component>
            </div>
            <div>
                <category-component></category-component>
            </div>
            <div>
                <answer-component></answer-component>
            </div>
        </div>
    </div>
</template>

<script>
import CountComponent from './count/CountComponent.vue';
import TurnComponent from './turn/TurnComponent.vue';
import CategoryComponent from './category/CategoryComponent.vue';
import AnswerComponent from './answer/AnswerComponent.vue';

export default {
    components: { CountComponent, TurnComponent, CategoryComponent, AnswerComponent },
};
</script>

<style scoped>
.left-game-header {
}

.t {
    width: var(--h-left-game-header-width);
    margin: 0 auto;
    height: var(--h-game-header-height);
    margin-bottom: var(--h-game-header-margin-bottom);
    border: 1px solid black;
}
</style>

-->

<template>
    <div class="left-game-header">
        <div class="t">
            <count-component></count-component>
            <turn-component></turn-component>
            <category-component></category-component>
            <answer-component></answer-component>
        </div>
    </div>
</template>

<script>
import CountComponent from './count/CountComponent.vue';
import TurnComponent from './turn/TurnComponent.vue';
import CategoryComponent from './category/CategoryComponent.vue';
import AnswerComponent from './answer/AnswerComponent.vue';

export default {
    components: { CountComponent, TurnComponent, CategoryComponent, AnswerComponent },
};
</script>

<style scoped>
.left-game-header {
    width: var(--h-left-game-header-width);
    height: var(--h-game-header-height);
    margin: 0 auto;
    margin-bottom: var(--h-game-header-margin-bottom);
    border: 1px solid black;
}

.t {
    display: flex;
    align-items: center;
    border-radius: 5px;
    background-color: #f9f9f9;
}

.count-component,
.turn-component,
.category-component,
.answer-component {
    margin-right: 15px;
    margin-left: 15px;
}

.count-component:last-child,
.turn-component:last-child,
.category-component:last-child,
.answer-component:last-child {
    margin-right: 0;
}
</style>
