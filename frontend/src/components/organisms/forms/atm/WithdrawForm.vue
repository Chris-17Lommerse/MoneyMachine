<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute } from 'vue-router'
    import { getBankAccountByIban } from '@/utils/bankAccountLoader.js'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import router from '@/router/router.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import { throwIfWithdrawAmountIsNotValid } from '@/utils/inputValidator.js'

    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'

    const errorHandlingStore = useErrorHandlingStore()
    
    const amount = ref(null)
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)

    function handleWithdraw(e) {
        try {
            e.preventDefault() 
            throwIfWithdrawAmountIsNotValid(amount.value, bankAccount.value.balance, bankAccount.value.absoluteLimit)

            errorHandlingStore.successMessage = 'Successfully withdrawn [PRICE] from your balance.'
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
    <h4>Current balance: {{ getPriceFormatted(bankAccount?.balance) }}</h4>  
    <h4>Absolute limit: {{ getPriceFormatted(bankAccount?.absoluteLimit) }}</h4>
    
    <form @submit="handleWithdraw" class="mt-4">    
        <BaseFormField :labelName="'Amount (max ' + getPriceFormatted(bankAccount?.singleTransferLimit) + ')'" type="number" min="0.00" step="0.01" id="amount" placeholder="Enter amount of money" v-model="amount"/>
        <SubmitBtn text="Withdraw" />
    </form>
</template>