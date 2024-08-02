import DrawPad from "@/components/drawpad/DrawPad.vue";
import StoreTokens from "@/components/loginCompo/StoreTokens.vue";
// import SideComponent from '@/components/SideComponent.vue';
import LoginCompo from "@/components/loginCompo/LoginCompo.vue";
import GameHeaderComponent from "@/components/game/header/GameHeaderComponent.vue";
import LeftGameHeaderComponent from "@/components/game/header/left-header/LeftGameHeaderComponent.vue";
import RightGameHeaderComponent from "@/components/game/header/right-header/RightGameHeaderComponent.vue";
import GameSideComponent from "@/components/game/middle/side/GameSideComponent.vue";
import GameMiddleComponent from "@/components/game/middle/GameMiddleComponent.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import ChatComponent from "@/components/chatting/ChatCompo.vue";
import VoteComponent from "@/components/game/middle/main/VoteComponent.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
import UserCompo from "@/components/loginCompo/UserCompo.vue";
import TestModalComponent from "@/components/test-vue/TestModalComponent.vue";
import MainCompo from "@/components/websocket/MainCompo.vue";

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
          // 메인 화면에 띄우는 컴포넌트 변경하기
          main: MainCompo,
          side: LoginCompo,
        },
      },
      {
        path: "/lobby",

        components: {
          main: () => import("@/components/WaitingRoom.vue"),
          side: UserCompo,
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
