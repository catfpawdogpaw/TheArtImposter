import DrawPad from "@/components/drawpad/DrawPad.vue";
import MiddleComponent from "@/components/MiddleComponent.vue";
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
        ],
    },
];
