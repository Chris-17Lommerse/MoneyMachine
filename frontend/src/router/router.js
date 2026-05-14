import { createRouter, createWebHistory } from 'vue-router'
import ATMLogin from '@/components/views/atm/authentication/ATMLogin.vue'
import ATMUserAuthorizationTest from '@/components/views/atm/authentication/ATMUserAuthorizationTest.vue'
import ATMLayout from '@/components/layout/ATMLayout.vue'
import { useAuthStore } from "@/stores/authStore.js"
import { useErrorHandlingStore } from "@/stores/errorHandlingStore"
import ATMLogout from '@/components/views/atm/authentication/ATMLogout.vue'

const routes = [
    {
        path: '/',
        redirect: '/atm/login'
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
                component: ATMUserAuthorizationTest,
                meta: { 
                    title: 'UserTest',
                    isAtmAuthenticated: true
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

    if (to.meta.isAtmAuthenticated && authStore.atmDecodedAuthToken === null) {
        errorHandlingStore.setErrorMessage('You need to be logged in to perform this action.')
        return '/atm/login'
    }
    
    if (to.meta.title) {
        document.title = to.meta.title
    }
})

export default router