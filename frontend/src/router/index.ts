import {useAuthStore} from "@/store/AuthStore";
import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        meta: {
            permit: true
        },
        component: () => import("@/pages/MainPage.vue")
    },
    {
        path: '/profile',
        name: 'profile',
        meta: {
            permit: false,
            auth: true,
        },
        component: () => import("@/pages/ProfilePage.vue"),
    },
    {
        path: '/site',
        name: 'site',
        meta: {
            permit: false,
            auth: true,
        },
        component: () => import("@/pages/SitePage.vue"),
    },
    {
        path: '/setting',
        name: 'setting',
        meta: {
            permit: false,
            auth: true,
        },
        component: () => import("@/pages/SettingPage.vue"),
    },
    {
        path: '/email/confirm/token/:token',
        name: 'confirm',
        meta: {
            permit: true
        },
        component: () => import("@/pages/ConfirmPage.vue"),
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'notFound',
        meta: {
            permit: true
        },
        component: () => import("@/pages/NotFound.vue")
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    if (to.meta.permit) return next()
    if (to.meta.auth && !authStore.getAuth) return next('/')
    if (to.meta.auth === authStore.getAuth) return next()

    return next('/')
})

export default router
