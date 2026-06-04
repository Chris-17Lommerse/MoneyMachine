<script setup>
    import MyBankAccountDisplay from '@/components/organisms/atm/MyBankAccountDisplay.vue'
    import ReturnBtn from '@/components/atoms/buttons/ReturnBtn.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
</script>

<template>
    <ReturnBtn text="Return to bank account selection" to="/atm/select-bank-account" />
    <ErrorAlert />
    <SuccessAlert />
    <MyBankAccountDisplay />
</template>