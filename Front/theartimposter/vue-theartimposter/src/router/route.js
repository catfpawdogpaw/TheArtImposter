import DrawPad from '@/components/drawpad/DrawPad.vue';
import StoreTokens from '@/components/loginCompo/StoreTokens.vue';
import MiddleComponent from '@/components/MiddleComponent.vue';
import SideComponent from '@/components/SideComponent.vue';
import ChatCompo from '@/components/chatting/ChatCompo.vue';
import LoginCompo from '@/components/loginCompo/LoginCompo.vue';
import GameHeaderComponent from '@/components/game/header/GameHeaderComponent.vue';
// import LoginCompo from '@/components/loginCompo/LoginCompo.vue';
import LeftGameHeaderComponent from '@/components/game/header/left-header/LeftGameHeaderComponent.vue';
import RightGameHeaderComponent from '@/components/game/header/right-header/RightGameHeaderComponent.vue';
import GameSideComponent from '@/components/game/middle/side/GameSideComponent.vue';
import GameMiddleComponent from '@/components/game/middle/GameMiddleComponent.vue';
import HeaderComponent from '@/components/HeaderComponent.vue';

export default [
    {
        //메인경로마다 상태
        path: '/',
        components: {
            header: HeaderComponent,
            middle: MiddleComponent,
        },
        children: [
            {
                path: '',
                components: {
                    //위에 띄워지는 투표 화면도 생각해야함
                    main: DrawPad,
                    side: ChatCompo,
                },
            },
            {
                path: '/lobby',

                components: {
                    main: () => import('@/components/WaitingRoom.vue'),
                    side: SideComponent,
                },
            },
            {
                path: '/login',
                components: {
                    main: DrawPad,
                    side: LoginCompo,
                },
            },
            {
                // 서버에서 토큰을 쿼리스트링으로 전송한 것을 처리하는 path
                path: '/store-tokens',
                components: {
                    main: StoreTokens,
                    side: LoginCompo,
                },
            },
        ],
    },
    {
        path: '/game',
        components: {
            header: GameHeaderComponent,
            middle: GameMiddleComponent,
        },
        children: [
            {
                path: '',
                components: {
                    main: DrawPad,
                    side: GameSideComponent,
                    left_game_header: LeftGameHeaderComponent,
                    right_game_header: RightGameHeaderComponent,
                },
            },
        ],
    },
];
