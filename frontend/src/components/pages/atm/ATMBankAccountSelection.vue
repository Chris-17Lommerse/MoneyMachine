<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import axios from '@/utils/axios.js'
    import router from '@/router/router.js'

    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'
    import ATMSelectBankAccountForm from '@/components/organisms/forms/atm/ATMSelectBankAccountForm.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const authStore = useAuthStore()

    const vModel = ref({
        'selectedBankAccountIban': ''
    })
    
    const bankAccounts = ref(null)
    const errorAlertRef = ref(null)

    function handleSelectBankAccount(e) {
        try {
            e.preventDefault()

            if (!vModel.value.selectedBankAccountIban || vModel.value.selectedBankAccountIban.trim() === '') {
                throw new Error('Selected option must be a valid bank account iban.')
            }
            
            router.push('/atm/bank-account/' + vModel.value.selectedBankAccountIban)
        }
        catch (ex){
            errorAlertRef.value.displayErrorMessage(ex.message)   
        }
    }

    onMounted(async () => {
        try {
            const response = await axios.get('users/' + authStore.atmDecodedAuthToken.sub + '/bank-accounts')
            bankAccounts.value = response.data.items

            if (bankAccounts.value.length === 0) {
                errorHandlingStore.errorMessage = 'You do not have any bank accounts. Please contact an employee to approve your account.'
                router.push('/atm/login')
            }
        }
        catch (ex) {
            if (ex.response){
                errorAlertRef.value.displayErrorMessage(ex.response.data.details)
            }
            else{
                errorAlertRef.value.displayErrorMessage(ex.message)
            }
        }
    })
</script>

<template>
    <h1 class="mb-3">Select your bank account</h1>
    <SuccessAlert />
    <ErrorAlert ref="errorAlertRef"/>
    <ATMSelectBankAccountForm @submit="handleSelectBankAccount" :vModel="vModel" :bankAccounts="bankAccounts" />
</template>
