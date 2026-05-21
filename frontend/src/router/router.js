import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from "@/stores/authStore.js"
import { useErrorHandlingStore } from "@/stores/errorHandlingStore"

import ATMLayout from '@/components/layout/ATMLayout.vue'
import WebsiteLayout from '@/components/layout/WebsiteLayout.vue'

import NotFound from '@/components/pages/website/NotFound.vue'

import ATMLogin from '@/components/pages/atm/authentication/ATMLogin.vue'
import ATMUserAuthorizationTest from '@/components/pages/atm/authentication/ATMUserAuthorizationTest.vue'
import ATMLogout from '@/components/pages/atm/authentication/ATMLogout.vue'

import Login from '@/components/pages/website/authentication/Login.vue'
import UserAuthorizationTest from '@/components/pages/website/authentication/UserAuthorizationTest.vue'
import Logout from '@/components/pages/website/authentication/Logout.vue'

import UsersWithoutBankAccountPage from '@/components/pages/website/users/UsersWithoutBankAccountPage.vue'
import EmployeeAuthorizationTest from '@/components/pages/website/authentication/EmployeeAuthorizationTest.vue'

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
                    title: 'Logout',
                    isAtmAuthenticated: true
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
    {
        path: '/',
        component: WebsiteLayout,
        children: [
            {
                path: '/users', 
                component: UsersWithoutBankAccountPage,
                meta: { 
                    title: 'Users',
                    isWebsiteAuthenticated: true
                }
            },
            {
                path: '/login', 
                component: Login,
                meta: { 
                    title: 'Users',
                }
            },
            {
                path: '/logout', 
                component: Logout,
                meta: { 
                    title: 'Users',
                    isWebsiteAuthenticated: true
                }
            },
            {
                path: '/user-test', 
                component: UserAuthorizationTest,
                meta: { 
                    title: 'User test',
                    isWebsiteAuthenticated: true
                }

            },
            {
                path: '/employee-test', 
                component: EmployeeAuthorizationTest,
                meta: { 
                    title: 'Employee test',
                    isWebsiteAuthenticated: true,
                    roles: ['EMPLOYEE']
                }
            },
            {
                path: '/:pathMatch(.*)*',
                component: NotFound
            }
        ]
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
        errorHandlingStore.errorMessage = 'You need to be logged in into the ATM to perform this action.'
        return '/atm/login'
    }

    if (to.meta.isWebsiteAuthenticated) {

        const websiteDecodedAuthToken = authStore.websiteDecodedAuthToken

        if (websiteDecodedAuthToken === null) {
            errorHandlingStore.errorMessage = 'You need to be logged in to perform this action.'
            return '/login'
        }

        if (to.meta.roles) {

            let isAuthorized = false

            for (const role of to.meta.roles) {
                if (websiteDecodedAuthToken.role === role){
                    isAuthorized = true
                    break
                }
            }

            if (isAuthorized === false) {
                errorHandlingStore.errorMessage = `Your account doesn't have the right role to perform this action.`
                return '/login'
            }
        }
    }
    
    if (to.meta.title) {
        document.title = to.meta.title
    }
})

export default router