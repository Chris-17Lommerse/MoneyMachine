<script setup>
    import { onMounted, ref } from 'vue'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import router from '@/router/router.js'
    import axios from '@/utils/axios.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useBankAccountStore } from '@/stores/bankAccountStore.js'
    import { throwIfMoneyAmountIsNotValid } from '@/utils/inputValidator.js'

    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const bankAccountStore = useBankAccountStore()
    
    const amount = ref(null)
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)

    async function handleDeposit(e) {
        try {
            e.preventDefault()
            throwIfMoneyAmountIsNotValid(amount.value, bankAccount.value.singleTransferLimit)

            const response = await axios.post('/transactions/deposit', {
                'toBankAcountIban': bankAccount.value.iban,
                'amount': amount.value
            })

            errorHandlingStore.successMessage = 'Successfully deposited '+ getPriceFormatted(response.data.amount) +' to your balance.'
            router.push('/atm/bank-account/' + router.currentRoute.value.params.iban)
        }
        catch (ex) {
            if (ex.response){
                errorAlertRef.value.displayErrorMessage(ex.response.data.details)
                bankAccount.value = await bankAccountStore.getBankAccountByIbanFromDatabase(router.currentRoute.value.params.iban, '/atm/select-bank-account')
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
    
    <form @submit="handleDeposit">    
        <BaseFormField :labelName="'Amount (max ' + getPriceFormatted(bankAccount?.singleTransferLimit) + ')'" type="number" step="0.01" id="amount" placeholder="Enter amount of money" v-model="amount"/>
        <SubmitBtn text="Deposit" />
    </form>
</template>