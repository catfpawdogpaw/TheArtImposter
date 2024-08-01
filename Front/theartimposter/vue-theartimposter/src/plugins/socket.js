import { EventBus } from "@/utils/eventBus";
import io from "socket.io-client";

export default {
    install(Vue, { store }) {
        console.log("소켓 저장");
        const socket = io("http://localhost:3000");
        Vue.prototype.$socket = socket;

        socket.on("connect", () => {
            console.log("Socket connected");
        });

        socket.on("receiveData", (data) => {
            console.log("Data received in socket plugin:", data);
            // Vuex 액션 호출 등 데이터 처리
            store.dispatch("updateReceivedData", data);
        });

        socket.on("GameRoomStatus", (data) => {
            console.log("GameRoomStatus:", data);
            store.dispatch("updateGameRoomStatus", data);
        });

        socket.on("userJoined", (data) => {
            console.log("userJoined:", data);
            store.dispatch("addPlayer", data); // Vuex 스토어에 유저 추가
        });

        socket.on("gameInfo", (data) => {
            console.log("gameInfo:", data);
            store.dispatch("updateGameInfo", data);
        });

        socket.on("turnStart", (data) => {
            console.log("turnStart:", data);
            store.dispatch("updateTurnPlayer", data);
        });

        socket.on("voteStart", (data) => {
            console.log("voteStart:", data);
            EventBus.$emit("voteStart", data);
        });

        socket.on("voteResult", (data) => {
            console.log("voteResult:", data);
            EventBus.$emit("voteStart", data);
        });

        socket.on("fakeArtistGuessStart", (data) => {
            console.log("fakeArtistGuessStart:", data);
            EventBus.$emit("fakeArtistGuessStart", data);
        });

        socket.on("fakeArtistGuessResult", (data) => {
            console.log("fakeArtistGuessResult:", data);
            EventBus.$emit("fakeArtistGuessResult", data);
        });

        socket.on("roundEnd", (data) => {
            console.log("roundEnd:", data);
            socket.emit("GameRoomStatus");
        });

        socket.on("disconnect", () => {
            console.log("Socket disconnected");
        });
    },
};
