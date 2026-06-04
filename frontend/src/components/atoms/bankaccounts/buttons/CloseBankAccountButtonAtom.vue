<script setup>
const props = defineProps({
    bankAccount: {
        type: Object,
        required: true
    }
})
</script>

<template>
     <RouterLink :to="{path: `/bank-accounts/${bankAccount.iban}/close`}" class="btn btn-danger">Close bank account</RouterLink>
</template>