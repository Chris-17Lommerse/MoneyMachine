import { createRouter, createWebHistory } from 'vue-router'
import HelloWorld from '@/components/HelloWorld.vue'
import ATMLogin from '@/components/views/atm/authentication/ATMLogin.vue'
import AdminAuthorizationTest from '@/components/views/atm/authentication/AdminAuthorizationTest.vue'
import UserAuthorizationTest from '@/components/views/atm/authentication/UserAuthorizationTest.vue'
import ATMLayout from '@/components/layout/ATMLayout.vue'
import { useAuthStore } from "@/stores/authStore.js"
import { useErrorHandlingStore } from "@/stores/errorHandlingStore"
import ATMLogout from '@/components/views/atm/authentication/ATMLogout.vue'

const routes = [
    {
        path: '/', 
        component: HelloWorld
    },
    {
        path: '/atm',
        component: ATMLayout,
        children: [
            { 
                path: 'login', 
                component: ATMLogin, 
                meta: { 
                    title: 'Login'
                }
            },
            { 
                path: 'logout', 
                component: ATMLogout, 
                meta: { 
                    title: 'Logout'
                }
            },
            { 
                path: 'user-test', 
                component: UserAuthorizationTest,
                meta: { 
                    title: 'UserTest',
                    isAuthenticated: true
                }
            },
        ],
    },
]

const router = createRouter({
    "history": createWebHistory(),
    routes,
})

router.beforeEach((to) => {

    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    if (to.meta.isAuthenticated) {

        const decodedAuthToken = authStore.decodedAuthToken

        if (decodedAuthToken === null) {
            errorHandlingStore.setErrorMessage('You need to be logged in to perform this action.')
            return '/atm/login'
        }
    }
    
    if (to.meta.title) {
        document.title = to.meta.title
    }
})

export default router