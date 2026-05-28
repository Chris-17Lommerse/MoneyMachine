<script setup>
    import router from '@/router/router.js'

    import DepositForm from '@/components/organisms/forms/atm/DepositForm.vue'
    import ReturnBtn from '@/components/atoms/buttons/ReturnBtn.vue'
</script>

<template>
    <ReturnBtn text="Return to my bank account" :to="'/atm/bank-account/' + router.currentRoute.value.params.iban" />
    <h1>Deposit</h1>
    <DepositForm />
</template>