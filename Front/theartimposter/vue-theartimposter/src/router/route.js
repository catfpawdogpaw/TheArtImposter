import DrawPad from "@/components/drawpad/DrawPad.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
import LoginCompo from "@/components/loginCompo/LoginCompo.vue";
import StoreTokens from "@/components/loginCompo/StoreTokens.vue";
import SideComponent from "@/components/SideComponent.vue";

export default [
    {
        //메인경로마다 상태
        path: "/",
        components: {
            middle: MiddleComponent,
        },
        children: [
            {
                path: "/",
                components: {
                    //위에 띄워지는 투표 화면도 생각해야함
                    main: DrawPad,
                    side: LoginCompo,
                },
            },
            {
                path: "/lobby",
                components: {
                    main: () => import("@/components/WaitingRoom.vue"),
                    side: SideComponent,
                },
            },
        ],
    },
    {   // 서버에서 토큰을 쿼리스트링으로 전송한 것을 처리하는 path
        path: '/store-tokens',
        name: 'StoreTokens',
        component: StoreTokens
    }
];