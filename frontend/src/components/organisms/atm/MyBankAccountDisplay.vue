<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute } from 'vue-router'
    import axios from '@/utils/axios.js'
    import router from '@/router/router.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useAuthStore } from '@/stores/authStore.js'

    import ATMActionOptions from '@/components/molecules/atm/ATMActionOptions.vue'
    
    const errorHandlingStore = useErrorHandlingStore()
    const authStore = useAuthStore()
    const route = useRoute()
    const bankAccount = ref(null)
    
    onMounted(async () => {
        try {
            const response = await axios.get('/bank-accounts/' + route.params.iban)
            bankAccount.value = response.data
        }
        catch (ex){
            if (ex.response){
                errorHandlingStore.errorMessage = ex.response.data.details
                router.push('/atm/select-bank-account')
            }
            else {
                useErrorHandlingStore.errorMessage = ex.details
            }
        }
    }) 
</script>

<template>
    <h3>Welcome, {{ authStore.atmDecodedAuthToken?.firstName }} </h3>
    <h3>{{ route.params.iban }} | {{ bankAccount?.bankAccountType }}</h3>
    <h3>Balance: {{ bankAccount?.balance }} </h3>
    <ATMActionOptions :iban="route.params.iban" />
</template>