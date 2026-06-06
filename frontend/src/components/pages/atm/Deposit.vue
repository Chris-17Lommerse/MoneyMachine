<script setup>
    import router from '@/router/router.js'
    import { onMounted, ref } from 'vue'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import axios from '@/utils/axios.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { useBankAccountStore } from '@/stores/bankAccountStore.js'
    import { throwIfMoneyAmountIsNotValid } from '@/utils/inputValidator.js'

    import DepositForm from '@/components/organisms/forms/atm/DepositForm.vue'
    import ReturnBtn from '@/components/atoms/buttons/ReturnBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    const bankAccountStore = useBankAccountStore()
    
    const vModel = ref({})
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)
    const routeIban = router.currentRoute.value.params.iban

    async function handleDeposit(e) {
        try {
            e.preventDefault()
            throwIfMoneyAmountIsNotValid(vModel.value.amount, bankAccount.value.singleTransferLimit)

            const response = await axios.post('/transactions/deposit', {
                'toBankAcountIban': routeIban,
                'amount': vModel.value.amount
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
    <ReturnBtn text="Return to my bank account" :to="'/atm/bank-account/' + router.currentRoute.value.params.iban" />
    <h1>Deposit</h1>
    <ErrorAlert ref="errorAlertRef" />
    <DepositForm @submit="handleDeposit" :vModel="vModel" :bankAccount="bankAccount" />
</template>