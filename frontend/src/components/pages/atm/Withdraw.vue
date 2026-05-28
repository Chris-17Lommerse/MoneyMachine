<script setup>
    import router from '@/router/router.js'

    import WithdrawForm from '@/components/organisms/forms/atm/WithdrawForm.vue'
    import ReturnBtn from '@/components/atoms/buttons/ReturnBtn.vue'
</script>

<template>
    <ReturnBtn text="Return to my bank account" :to="'/atm/bank-account/' + router.currentRoute.value.params.iban" />
    <h1>Withdraw</h1>
    <WithdrawForm />
</template>