import { createRouter, createWebHistory } from 'vue-router'
import HelloWorld from '@/components/HelloWorld.vue'
import ATMLogin from '@/components/views/atm/authentication/ATMLogin.vue'
import AdminAuthorizationTest from '@/components/views/atm/authentication/AdminAuthorizationTest.vue'
import UserAuthorizationTest from '@/components/views/atm/authentication/UserAuthorizationTest.vue'
import AtmLayout from '@/components/layout/AtmLayout.vue'

const routes = [
    {
        path: '/', 
        component: HelloWorld
    },
    {
        path: '/atm',
        component: AtmLayout,
        children: [
            { 
                path: 'login', 
                component: ATMLogin, 
                meta: { 
                    title: 'Login'
                }
            },
            { 
                path: 'admin-test', 
                component: AdminAuthorizationTest,
                meta: { 
                    title: 'AdminTest',
                    isAuthenticated: true,
                    roles: ['Admin']
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
    "history": createWebHistory('/MoneyMachine/'),
    routes,
})

export default router