<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import axios from '@/utils/axios.js'

    import FormLabel from '@/components/atoms/forms/FormLabel.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const authStore = useAuthStore()

    const selectedBankAccountIban = ref('')
    const bankAccounts = ref(null)

    function handleSelectBankAccount(e) {
        try {
            e.preventDefault()
            console.log(selectedBankAccountIban.value)
        }
        catch (ex){
            errorHandlingStore.errorMessage = ex.message   
        }
    }

    onMounted(async () => {
        try {
            bankAccounts.value = (await axios.get('users/' + authStore.atmDecodedAuthToken.sub + '/bank-accounts')).data
        }
        catch (ex){
            if (ex.response){
                errorHandlingStore.errorMessage = ex.response.data.message
            }
            else {
                errorHandlingStore.errorMessage = ex.message
            }
        }
    })
</script>

<template>

    <SuccessAlert />
    <ErrorAlert />

    <form @submit="handleSelectBankAccount">
        <div class="mb-3">
            <FormLabel id="bankAccount" labelName="Select a bank account" />
            <select v-model="selectedBankAccountIban" id="bankAccount" class="custom-select">
                <option value="" disabled>Select an account</option>
                <option v-for="bankAccount in bankAccounts" :value="bankAccount.iban">{{ bankAccount.iban }}</option>
            </select>
        </div>

        <SubmitBtn text="Select bank account"/>
    </form>
</template>
