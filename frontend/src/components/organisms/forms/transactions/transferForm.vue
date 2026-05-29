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
        fromAccount: "",
        toAccount: "",
        amount: "",
        message: "",
        initiatedBy: ""
    })
    const emit = defineEmits(['createTransaction'])
</script>

<template>
  <form @submit.prevent="emit('createTransaction',transaction)">
    <ErrorAlert/>
    <SuccessAlert/>
    <BaseFormField v-model="transaction.fromAccount" labelName="From Account" type="text" id="fromAccount" name="fromAccount"/>
    <BaseFormField v-model="transaction.toAccount" labelName="To Account" type="text" id="toAccount" name="toAccount"/>
    <BaseFormField v-model="transaction.amount" labelName="Amount" type="number" id="amount" name="amount"/>
    <BaseFormField v-model="transaction.message" labelName="Message" type="text" id="message" name="message"/>
    <button type="submit" class="btn btn-primary">create</button>

  </form>
</template>