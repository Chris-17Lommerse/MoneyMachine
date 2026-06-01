<script setup>
    import { getPriceFormatted } from '@/utils/stringFormatter.js'

    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'

    defineProps({
        vModel: {
            type: Object,
            required: true
        },
        bankAccount: {
            type: Object,
            required: false
        }
    });
</script>

<template>    
    <form>    
        <BaseFormField :labelName="'Amount (max ' + getPriceFormatted(bankAccount?.singleTransferLimit) + ')'" type="number" step="0.01" id="amount" placeholder="Enter amount of money" v-model="vModel.amount"/>
        <SubmitBtn text="Deposit" />
    </form>
</template>