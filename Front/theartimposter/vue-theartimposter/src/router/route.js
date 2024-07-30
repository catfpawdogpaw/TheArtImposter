import DrawPad from "@/components/drawpad/DrawPad.vue";
import StoreTokens from "@/components/loginCompo/StoreTokens.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
import SideComponent from "@/components/SideComponent.vue";
import ChatCompo from "@/components/chatting/ChatCompo.vue";
import LoginCompo from "@/components/loginCompo/LoginCompo.vue";
import GameHeaderComponent from "@/components/game/header/GameHeaderComponent.vue";
// import LoginCompo from '@/components/loginCompo/LoginCompo.vue';
// import GameHeaderComponent from '@/components/game/header/GameHeaderComponent.vue';
// import LeftGameHeaderComponent from '@/components/game/header/left-header/LeftGameHeaderComponent.vue';
// import RightGameHeaderComponent from '@/components/game/header/right-header/RightGameHeaderComponent.vue';
// import GameSideComponent from '@/components/game/side/GameSideComponent.vue';

export default [
  {
    //메인경로마다 상태
    path: "/",
    components: {
      middle: MiddleComponent,
      game_header: GameHeaderComponent,
    },
    children: [
      {
        path: "",
        components: {
          //위에 띄워지는 투표 화면도 생각해야함
          main: DrawPad,
          side: ChatCompo,
        },
      },
      // {
      //     path: "lobby",

      //     path: '/',
      //     components: {
      //         //위에 띄워지는 투표 화면도 생각해야함
      //         main: DrawPad,
      //         side: GameSideComponent,
      //         // side: LoginCompo,
      //         // side: SideComponent,
      //         left_game_header: LeftGameHeaderComponent,
      //         right_game_header: RightGameHeaderComponent,
      //     },
      // },
      {
        path: "/lobby",

        components: {
          main: () => import("@/components/WaitingRoom.vue"),
          side: SideComponent,
        },
      },
      {
        path: "login",
        components: {
          main: DrawPad,
          side: LoginCompo,
        },
      },
    ],
  },
  {
    // 서버에서 토큰을 쿼리스트링으로 전송한 것을 처리하는 path
    path: "/store-tokens",
    name: "StoreTokens",
    component: StoreTokens,
  },
];
