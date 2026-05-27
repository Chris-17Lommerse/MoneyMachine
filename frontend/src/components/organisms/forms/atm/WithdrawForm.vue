<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute } from 'vue-router'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import router from '@/router/router.js'
    import axios from '@/utils/axios.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useBankAccountStore } from '@/stores/bankAccountStore.js'
    import { throwIfMoneyAmountIsNotValid, throwIfWithdrawAmountIsNotValid } from '@/utils/inputValidator.js'

    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const bankAccountStore = useBankAccountStore()

    const amount = ref(null)
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)

    async function handleWithdraw(e) {
        try {
            e.preventDefault() 
            throwIfMoneyAmountIsNotValid(amount.value, bankAccount.value.singleTransferLimit)
            throwIfWithdrawAmountIsNotValid(amount.value, bankAccount.value.balance, bankAccount.value.absoluteLimit)

            const response = await axios.post('/transactions/withdraw', {
                'fromBankAcountIban': bankAccount.value.iban,
                'amount': amount.value
            })

            errorHandlingStore.successMessage = 'Successfully withdrawn '+ getPriceFormatted(response.data.amount) +' from your balance.'
            router.push('/atm/bank-account/' + router.currentRoute.value.params.iban)
        }
        catch (ex) {
            if (ex.response){
                errorAlertRef.value.displayErrorMessage(ex.response.data.details)
            }
            else{
                errorAlertRef.value.displayErrorMessage(ex.message)
            }
        }
    }

    onMounted(async () => {
        bankAccount.value = await bankAccountStore.getBankAccountByIban(router.currentRoute.value.params.iban, '/atm/select-bank-account')
    })
</script>

<template>
    <ErrorAlert ref="errorAlertRef" />
    <h4>Current balance: {{ getPriceFormatted(bankAccount?.balance) }}</h4>  
    <h4>Absolute limit: {{ getPriceFormatted(bankAccount?.absoluteLimit) }}</h4>
    
    <form @submit="handleWithdraw" class="mt-4">    
        <BaseFormField :labelName="'Amount (max ' + getPriceFormatted(bankAccount?.singleTransferLimit) + ')'" type="number" step="0.01" id="amount" placeholder="Enter amount of money" v-model="amount"/>
        <SubmitBtn text="Withdraw" />
    </form>
</template>