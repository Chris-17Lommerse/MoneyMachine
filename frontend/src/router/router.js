import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
import { useAuthStore } from "@/stores/authStore.js"
import { useErrorHandlingStore } from "@/stores/errorHandlingStore"
import AllTransactions from '../components/pages/website/transactions/AllTransactions.vue'
import CreateTransactionAsUser from '@/components/pages/website/transactions/CreateTransactionAsUser.vue'
import CreateTransactionAsEmployee from '@/components/pages/website/transactions/CreateTransactionAsEmployee.vue'

import AtmLayout from '../components/layout/AtmLayout.vue'
import WebsiteLayout from '@/components/layout/WebsiteLayout.vue'

import temporaryAccountPage from"@/components/pages/website/bankAccounts/bankAccount.vue"

import NotFound from '@/components/pages/website/NotFound.vue'

import ATMLogin from '@/components/pages/atm/authentication/ATMLogin.vue'
import ATMUserAuthorizationTest from '@/components/pages/atm/authentication/ATMUserAuthorizationTest.vue'
import ATMBankAccountSelection from '@/components/pages/atm/ATMBankAccountSelection.vue'
import Deposit from '@/components/pages/atm//Deposit.vue'
import ATMMyBankAccount from '@/components/pages/atm/ATMMyBankAccount.vue'
import Withdraw from '@/components/pages/atm/Withdraw.vue'

import UsersWithoutBankAccountPage from '@/components/pages/website/users/UsersWithoutBankAccountPage.vue'
import CreateBankAccountPage from '@/components/pages/website/CreateBankAccountPage.vue'
import UsersWithBankAccuntsPage from '@/components/pages/website/UsersWithBankAccountsPage.vue'

import Login from '@/components/pages/website/authentication/Login.vue'
import UserAuthorizationTest from '@/components/pages/website/authentication/UserAuthorizationTest.vue'

import EmployeeAuthorizationTest from '@/components/pages/website/authentication/EmployeeAuthorizationTest.vue'
import CloseBankAccountPage from "@/components/pages/website/CloseBankAccountPage.vue"

const routes = [
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
                path: 'select-bank-account', 
                component: ATMBankAccountSelection, 
                meta: { 
                    title: 'Select your bank account',
                    isAtmAuthenticated: true
                }
            },
            { 
                path: 'bank-account/:iban', 
                component: ATMMyBankAccount, 
                meta: { 
                    title: 'My bank account',
                    isAtmAuthenticated: true
                }
            },
            { 
                path: 'deposit/:iban', 
                component: Deposit, 
                meta: { 
                    title: 'Deposit',
                    isAtmAuthenticated: true
                }
            },
            { 
                path: 'withdraw/:iban', 
                component: Withdraw, 
                meta: { 
                    title: 'Withdraw',
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
        path: '/transactions',
        component: WebsiteLayout,
        children: [
            {
                path: '',
                component: AllTransactions,
                meta: {
                    isWebsiteAuthenticated: true,
                    title: 'All Transactions',
                    roles: ['EMPLOYEE']
                }
            },
            {
                path: 'create/user',
                component: CreateTransactionAsUser,
                meta: {
                    isWebsiteAuthenticated: true,
                    title: 'Create Transaction',
                }
            },
            {
                path: 'create/employee',
                component: CreateTransactionAsEmployee,
                meta: {
                    isWebsiteAuthenticated: true,
                    title: 'Create Transaction',
                }
            },
        ]
    },
    {
        path: '/',
        component: WebsiteLayout,
        redirect: '/login',
        children: [
            {
                path: 'users',
                component: UsersWithoutBankAccountPage,
                meta: {
                    title: 'Users',
                    isWebsiteAuthenticated: true,
                    roles: ['EMPLOYEE']
                }
            },
            {
                path: 'login',
                component: Login,
                meta: {
                    title: 'Users',
                }
            },
            {
                path: 'user-test', 
                component: UserAuthorizationTest,
                meta: {
                    title: 'User test',
                    isWebsiteAuthenticated: true
                }

            },
            {
                path: 'employee-test',
                component: EmployeeAuthorizationTest,
                meta: {
                    title: 'Employee test',
                    isWebsiteAuthenticated: true,
                    roles: ['EMPLOYEE']
                }
            },
            {
                path: 'bank-accounts',
                component: UsersWithBankAccuntsPage,
                meta: {
                    title: 'bank-accounts',
                    isWebsiteAuthenticated: true,
                    roles: ['EMPLOYEE']
                }
            },
            {
                path: 'bank-accounts/:iban/close',
                component: CloseBankAccountPage,
                meta: {
                    title: "close-bank-accounts-preview",
                    isWebsiteAuthenticated: true,
                    roles: ['EMPLOYEE']
                }
            },
            {
                path: ':pathMatch(.*)*',
                component: NotFound
            }
        ]
    },
    {
        path: '/users/:user_id/bank-accounts',
        component: CreateBankAccountPage
    },
    {
        path: '/bank-accounts/temporary',
        component: temporaryAccountPage
    }
]

const router = createRouter({
    "history": createWebHashHistory(),
    routes,
})

router.beforeEach((to) => {

    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    if (to.meta.isAtmAuthenticated && authStore.atmDecodedAuthToken === null) {
        errorHandlingStore.errorMessage = 'You need to be logged in into the ATM to perform this action.'
        return { 
            path: '/atm/login', 
            query: { 
                _refresh: Date.now() 
            } 
        }
    }

    if (to.meta.isWebsiteAuthenticated) {

        const websiteDecodedAuthToken = authStore.websiteDecodedAuthToken

        if (websiteDecodedAuthToken === null) {
            errorHandlingStore.errorMessage = 'You need to be logged in to perform this action.'
            return { 
                path: '/login', 
                query: { 
                    _refresh: Date.now() 
                } 
            }
        }

        if (to.meta.roles) {

            let isAuthorized = false

            for (const role of to.meta.roles) {
                if (websiteDecodedAuthToken.role === role) {
                    isAuthorized = true
                    break
                }
            }

            if (isAuthorized === false) {
                errorHandlingStore.errorMessage = `Your account doesn't have the right role to perform this action.`
                return { 
                    path: '/login', 
                    query: { 
                        _refresh: Date.now() 
                    } 
                }
            }
        }
    }

    if (to.meta.title) {
        document.title = to.meta.title
    }
})

export default router