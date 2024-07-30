import DrawPad from "@/components/drawpad/DrawPad.vue";
import LoginCompo from "@/components/loginCompo/LoginCompo.vue";
import StoreTokens from "@/components/loginCompo/StoreTokens.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
import SideComponent from "@/components/SideComponent.vue";
import MatchComponent from "@/components/MatchComponent.vue";


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
<<<<<<< HEAD
    },{
         //메인경로마다 상태
         path: "/game-matching",
         components: {
             middle: MatchComponent,
         },
    }
   
];
=======
    },
    {   // 서버에서 토큰을 쿼리스트링으로 전송한 것을 처리하는 path
        path: '/store-tokens',
        name: 'StoreTokens',
        component: StoreTokens
    }
];
>>>>>>> fc8ead5b748d10470f748dee1bfa49abaa6b668e
