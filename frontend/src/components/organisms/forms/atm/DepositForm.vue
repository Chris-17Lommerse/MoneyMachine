<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute } from 'vue-router'
    import { getBankAccountByIban } from '@/utils/bankAccountLoader.js'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import router from '@/router/router.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore'

    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
import axios from 'axios'

    const errorHandlingStore = useErrorHandlingStore()
    
    const route = useRoute()
    const amount = ref(null)
    const bankAccount = ref(null)
    const errorAlertRef = ref(null)

    function handleDeposit(e) {
        try {
            e.preventDefault()
            
            if (amount.value <= 0) {
                throw new Error('Amount cannot be less or equal to 0.')
            }
            
            // axios.post('/transactions/desposit', {
            //     'amount': amount
            // })

            errorHandlingStore.successMessage = 'Successfully deposited [PRICE] to your balance.'
            router.push('/atm/bank-account/' + route.params.iban)
        }
        catch (ex) {
            if (ex.response){
                errorAlertRef.value.displayErrorMessage(ex.response.data.details)
            }
            else{
                errorAlertRef.value.displayErrorMessage(ex.details)
            }
        }
    }

    onMounted(async () => {
        bankAccount.value = await getBankAccountByIban(route.params.iban, '/atm/select-bank-account')
    })
</script>

<template>
    <form @submit="handleDeposit">
        <ErrorAlert ref="errorAlertRef" />
        <BaseFormField :labelName="'Amount (max ' + getPriceFormatted(bankAccount?.singleTransferLimit) + ')'" type="number" id="amount" placeholder="Enter amount of money" v-model="amount"/>
        <SubmitBtn text="Deposit" />
    </form>
</template>