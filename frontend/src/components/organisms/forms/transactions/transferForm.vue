<script setup>
    import { ref } from 'vue'
    import axios from '@/utils/axios.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import AuthsubmitBtn from '@/components/atoms/buttons/AuthSubmitBtn.vue'
    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    
    const transaction = ref({
        fromBankAcountIban: "",
        toBankAcountIban: "",
        amount: "",
        message: "",
    })
    const emit = defineEmits(['createTransaction'])
</script>

<template>
  <form @submit.prevent="emit('createTransaction',transaction)">
    <ErrorAlert/>
    <SuccessAlert/>
    <BaseFormField v-model="transaction.fromBankAcountIban" labelName="From IBAN" type="text" id="fromIban" name="fromIban"/>
    <BaseFormField v-model="transaction.toBankAcountIban" labelName="To IBAN" type="text" id="toIban" name="toIban" />
    <BaseFormField v-model="transaction.amount" labelName="Amount" type="number" id="amount" name="amount"/>
    <BaseFormField v-model="transaction.message" labelName="Message" type="text" id="message" name="message"/>
    <button type="submit" class="btn btn-primary">Create</button>

  </form>
</template>