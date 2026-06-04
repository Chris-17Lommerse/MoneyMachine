<script setup>
    import router from '@/router/router.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore'
    import { computed } from 'vue'

    const props = defineProps({
        isAtm: {
            type: Boolean,
            default: false
        }
    })

    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    function handleAtmLogout() {
        if (authStore.atmAuthToken !== null) {
            authStore.setAtmAuthToken(null)
            errorHandlingStore.successMessage = 'Successfully logged out of your ATM account.'
        }
        else {
            errorHandlingStore.errorMessage = 'You already are logged out of the ATM.'
        }

        router.push({
            path: '/atm/login',
            query: { 
                _refresh: Date.now() 
            }
        })
    }

    function handleWebsiteLogout() {
        if (authStore.websiteAuthToken !== null) {
            authStore.setWebsiteAuthToken(null)
            errorHandlingStore.successMessage = 'Successfully logged out of your account.'
        }
        else {
            errorHandlingStore.errorMessage = 'You already are logged out.'
        }

        router.push({
            path: '/login',
            query: { 
                _refresh: Date.now() 
            }
        })
    }

    function handleLogout() {
        if (props.isAtm === true) {
            handleAtmLogout()
        }
        else {
            handleWebsiteLogout()
        }
    }

    const text = computed(() => {
        if (props.isAtm === true) {
            return 'Logout ATM'
        } 

        return 'logout'
    })
</script>

<template>
    <button @click="handleLogout" class="btn btn-danger text-light">{{ text }}</button>
</template>