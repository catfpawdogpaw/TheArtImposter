<template>
    <div v-if="show" class="modal-overlay">
        <div class="modal-content">
            <h2 class="subject">{{ subject }}</h2>
            <p class="word">{{ word }}</p>
            <div class="options">
                <button class="btn">Yes</button>
                <button class="btn">No</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        show: Boolean,
        subject: String,
        word: String,
    },
};
</script>

<style scoped>
.subject {
    margin: 10px;
    font-size: 50px;
}

.word {
    margin: 20px;
    font-size: 80px;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    margin-left: 79px;
    margin-top: 121px;
    width: 800px;
    height: 580px;
    background-color: rgba(0, 0, 0, 0.1);
    justify-content: center;
    align-items: center;
    border: #000000 solid 1px;
}

.modal-content {
    border: #000000 solid 3px;
    background-color: white;
    width: 600px;
    height: 300px;
    opacity: 0.8;
    margin: 120px auto;
    border-radius: 50px;
    text-align: center;
    color: black;
    font-family: 'Questrial', sans-serif;
    background-color: #f6f6f6;
    background-image: linear-gradient(45deg, #dedede 25%, transparent 25%, transparent 75%, #dedede 75%, #dedede),
        linear-gradient(-45deg, #dedede 25%, transparent 25%, transparent 75%, #dedede 75%, #dedede);
    background-size: 60px 60px;
    background-position: 0 0;
    animation: slide 4s infinite linear;
}

@keyframes slide {
    from {
        background-position: 0 0;
    }

    to {
        background-position: -120px 60px;
    }
}

/* 
.modal {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    width: auto;
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    padding: 1.6rem 3rem;
    border: 3px solid black;
    border-radius: 5px;
    background: white;
    box-shadow: 8px 8px 0 rgba(0, 0, 0, 0.2);
} */

.btn {
    color: inherit;
    font-family: inherit;
    font-size: inherit;
    background: white;
    padding: 0.3rem 3.4rem;
    border: 3px solid black;
    margin-right: 2.6rem;
    box-shadow: 0 0 0 black;
    transition: all 0.2s;
}

.btn:last-child {
    margin: 0;
}

.btn:hover {
    box-shadow: 0.4rem 0.4rem 0 black;
    transform: translate(-0.4rem, -0.4rem);
}

.btn:active {
    box-shadow: 0 0 0 black;
    transform: translate(0, 0);
}

.options {
    /* display: flex;
    flex-direction: row; */
    /* justify-content: space-between; */
}
</style>
