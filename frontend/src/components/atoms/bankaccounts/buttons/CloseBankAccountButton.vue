<script setup>
const props = defineProps({
    bankAccount: {
        type: Object,
        required: true
    }
})

const emits = defineEmits(['closeAccount'])
</script>

<template>
    <RouterLink @click="$emit('closeAccount', bankAccount.iban)" class="btn btn-danger">Close bank account</RouterLink>
</template>