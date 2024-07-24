import MiddleFrontCompo from "@/components/MiddleFrontCompo.vue";
import PageLoginCompo from "@/components/PageLoginCompo.vue";
import PageLogoutCompo from "@/components/PageLogoutCompo.vue";
import PageMainCompo from "@/components/PageMainCompo.vue";
import PageMyPageCompo from "@/components/PageMyPageCompo.vue";
import PageMemoCompo from "@/components/memo/PageMemoCompo.vue";

export default [
    {
        path: "/",
        component: MiddleFrontCompo,
        children: [
            // '/'로 접근하면 PageMainCompo도 같이 보여준다. => 대표페이지
            { path: "main", component: PageMainCompo, alias: ["/"] },
            { path: "/mypage", component: PageMyPageCompo },
            { path: "/login", component: PageLoginCompo },
            { path: "/logout", component: PageLogoutCompo },
            {
                path: "/memo",
                component: PageMemoCompo,
                children: [
                    //클릭시 로딩 (lazy loading)
                    {
                        path: "/",
                        component: () =>
                            import("@/components/memo/MemoListCompo.vue"),
                    },
                    {
                        path: "/memo/input/:idx?",
                        component: () =>
                            import("@/components/memo/MemoInputCompo.vue"),
                    },
                ],
            },
        ],
    },
];
