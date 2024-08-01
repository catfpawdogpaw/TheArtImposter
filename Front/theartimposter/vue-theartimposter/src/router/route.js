import DrawPad from "@/components/drawpad/DrawPad.vue";
import GameHeaderComponent from "@/components/game/header/GameHeaderComponent.vue";
import LoginCompo from "@/components/loginCompo/LoginCompo.vue";
import StoreTokens from "@/components/loginCompo/StoreTokens.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
import SideComponent from "@/components/SideComponent.vue";
// import LoginCompo from '@/components/loginCompo/LoginCompo.vue';
import ChatComponent from "@/components/chatting/ChatCompo.vue";
import LeftGameHeaderComponent from "@/components/game/header/left-header/LeftGameHeaderComponent.vue";
import RightGameHeaderComponent from "@/components/game/header/right-header/RightGameHeaderComponent.vue";
import GameMiddleComponent from "@/components/game/middle/GameMiddleComponent.vue";
import VoteComponent from "@/components/game/middle/main/VoteComponent.vue";
import GameSideComponent from "@/components/game/middle/side/GameSideComponent.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import TestModalComponent from "@/components/test-vue/TestModalComponent.vue";

export default [
    {
        //메인경로마다 상태
        path: "/",
        components: {
            header: HeaderComponent,
            middle: MiddleComponent,
        },
        children: [
            {
                path: "",
                components: {
                    //위에 띄워지는 투표 화면도 생각해야함
                    main: DrawPad,
                    side: SideComponent,
                },
            },
            {
                path: "/lobby",

                components: {
                    main: () => import("@/components/WaitingRoom.vue"),
                    side: SideComponent,
                },
            },
            {
                path: "/login",
                components: {
                    main: DrawPad,
                    side: LoginCompo,
                },
            },
            {
                // 서버에서 토큰을 쿼리스트링으로 전송한 것을 처리하는 path
                path: "/store-tokens",
                components: {
                    main: StoreTokens,
                    side: LoginCompo,
                },
            },
        ],
    },
    {
        path: "/game",
        components: {
            header: GameHeaderComponent,
            middle: GameMiddleComponent,
        },
        children: [
            {
                path: "",
                components: {
                    draw: DrawPad,
                    vote: VoteComponent,
                    side: GameSideComponent,
                    side_chat: ChatComponent,
                    left_game_header: LeftGameHeaderComponent,
                    right_game_header: RightGameHeaderComponent,
                },
            },
        ],
    },
    {
        path: "/testmodal",
        components: {
            header: TestModalComponent,
            middle: HeaderComponent,
        },
    },
];
