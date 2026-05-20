import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from "@/stores/authStore.js"
import { useErrorHandlingStore } from "@/stores/errorHandlingStore"
import ATMLogout from '@/components/pages/atm/authentication/ATMLogout.vue'
import AllTransactions from '@/components/views/transactions/allTransactions.vue'
import Transaction from '@/components/views/transactions/transaction.vue'
import CreateTransaction from '@/components/views/transactions/CreateTransaction.vue'
import TransactionLayout from '@/components/layout/transactionLayout.vue'

import ATMLayout from '@/components/layout/ATMLayout.vue'

import ATMLogin from '@/components/pages/atm/authentication/ATMLogin.vue'
import ATMUserAuthorizationTest from '@/components/pages/atm/authentication/ATMUserAuthorizationTest.vue'
import UsersWithoutBankAccountPage from '@/components/pages/website/UsersWithoutBankAccountPage.vue'

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
    {
        path: '/transactions',
        component: TransactionLayout,
        children: [
            {
                path: '',
                component: AllTransactions,
                meta: {
                    title: 'All Transactions'
                }
            },
            {
                path: 'create',
                component: CreateTransaction,
                meta: {
                    title: 'Create Transaction'
                }
            },
            {
                path: ':id',
                component: Transaction,
                meta: {
                    title: 'Transaction Details'
                }
            }
        ]
    },
    {
        path: '/users', 
        component: UsersWithoutBankAccountPage

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