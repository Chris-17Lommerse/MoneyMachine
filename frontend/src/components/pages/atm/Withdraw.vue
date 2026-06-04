<script setup>
    import router from '@/router/router.js'
    import { onMounted, ref } from 'vue'
    import { useRoute } from 'vue-router'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import axios from '@/utils/axios.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useBankAccountStore } from '@/stores/bankAccountStore.js'
    import { throwIfMoneyAmountIsNotValid, throwIfWithdrawAmountIsNotValid } from '@/utils/inputValidator.js'

    import ReturnBtn from '@/components/atoms/buttons/ReturnBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    import WithdrawForm from '@/components/organisms/forms/atm/WithdrawForm.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const bankAccountStore = useBankAccountStore()

    const vModel = ref({})
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)
    const routeIban = router.currentRoute.value.params.iban

    async function handleWithdraw(e) {
        try {
            e.preventDefault() 
            throwIfMoneyAmountIsNotValid(amount.value, bankAccount.value.singleTransferLimit)
            throwIfWithdrawAmountIsNotValid(amount.value, bankAccount.value.balance, bankAccount.value.absoluteLimit)

            const response = await axios.post('/transactions/withdraw', {
                'fromBankAcountIban': routeIban,
                'amount': vModel.value.amount
            })

            errorHandlingStore.successMessage = 'Successfully withdrawn '+ getPriceFormatted(response.data.amount) +' from your balance.'
            router.push('/atm/bank-account/' + routeIban)
        }
        catch (ex) {
            if (ex.response){
                errorAlertRef.value.displayErrorMessage(ex.response.data.details)
                bankAccount.value = await bankAccountStore.getBankAccountByIbanFromDatabase(routeIban, '/atm/select-bank-account')
            }
            else{
                errorAlertRef.value.displayErrorMessage(ex.message)
            }
        }
    }

    onMounted(async () => {
        bankAccount.value = await bankAccountStore.getBankAccountByIban(routeIban, '/atm/select-bank-account')
    })
</script>

<template>
    <ReturnBtn text="Return to my bank account" :to="'/atm/bank-account/' + routeIban" />
    <h1>Withdraw</h1>
    <ErrorAlert ref="errorAlertRef" />
    <h4>Current balance: {{ getPriceFormatted(bankAccount?.balance) }}</h4>  
    <h4>Absolute limit: {{ getPriceFormatted(bankAccount?.absoluteLimit) }}</h4>
    <WithdrawForm @submit="handleWithdraw" :vModel="vModel" :bankAccount="bankAccount" />
</template>