<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import axios from '@/utils/axios.js'
    import router from '@/router/router.js'

    import FormLabel from '@/components/atoms/forms/FormLabel.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const authStore = useAuthStore()

    const selectedBankAccountIban = ref('')
    const bankAccounts = ref(null)
    const errorAlertRef = ref(null)

    function handleSelectBankAccount(e) {
        try {
            e.preventDefault()

            if (!selectedBankAccountIban.value || selectedBankAccountIban.value.trim() === '') {
                throw new Error('Selected option must be a valid bank account iban.')
            }
            
            router.push('/atm/bank-account/' + selectedBankAccountIban.value)
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

    <SuccessAlert />
    <ErrorAlert ref="errorAlertRef"/>

    <form @submit="handleSelectBankAccount">
        <div class="mb-3">
            <FormLabel id="bankAccount" labelName="Select a bank account" />
            <select v-model="selectedBankAccountIban" id="bankAccount" class="custom-select">
                <option value="" disabled>Select an account</option>
                <option v-for="bankAccount in bankAccounts" :value="bankAccount.iban">{{ bankAccount.iban }} | {{ bankAccount.bankAccountType }}</option>
            </select>
        </div>

        <SubmitBtn text="Select bank account"/>
    </form>
</template>
