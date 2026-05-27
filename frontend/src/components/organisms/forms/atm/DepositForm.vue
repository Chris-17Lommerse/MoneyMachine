<script setup>
    import { onMounted, ref } from 'vue'
    import { getBankAccountByIban } from '@/utils/bankAccountLoader.js'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import router from '@/router/router.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { throwIfMoneyAmountIsNotValid } from '@/utils/inputValidator.js'

    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    
    const amount = ref(null)
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)

    function handleDeposit(e) {
        try {
            e.preventDefault()
            throwIfMoneyAmountIsNotValid(amount.value)

            errorHandlingStore.successMessage = 'Successfully deposited [PRICE] to your balance.'
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
        bankAccount.value = await getBankAccountByIban(router.currentRoute.value.params.iban, '/atm/select-bank-account')
    })
</script>

<template>
    <ErrorAlert ref="errorAlertRef" />
    
    <form @submit="handleDeposit">    
        <BaseFormField :labelName="'Amount (max ' + getPriceFormatted(bankAccount?.singleTransferLimit) + ')'" type="number" min="0.00" step="0.01" id="amount" placeholder="Enter amount of money" v-model="amount"/>
        <SubmitBtn text="Deposit" />
    </form>
</template>