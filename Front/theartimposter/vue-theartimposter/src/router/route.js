import DrawPad from "@/components/drawpad/DrawPad.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
import SideComponent from "@/components/SideComponent.vue";
import GameHeaderComponent from "@/components/game/header/GameHeaderComponent.vue";
import LeftGameHeaderComponent from "@/components/game/header/left-header/LeftGameHeaderComponent.vue";
import RightGameHeaderComponent from "@/components/game/header/right-header/RightGameHeaderComponent.vue";

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
                path: "/",
                components: {
                    //위에 띄워지는 투표 화면도 생각해야함
                    main: DrawPad,
                    side: SideComponent,
                    left_game_header: LeftGameHeaderComponent,
                    right_game_header:RightGameHeaderComponent,
                },
            },
            {
                path: "/lobby",
                components: {
                    // main: () => import("@/components/OtherMainComponent.vue"),
                    // side: () => import("@/components/OtherMainComponent.vue"),
                },
            },
        ],
    },
];
