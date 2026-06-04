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
    <button @click="$emit('closeAccount', bankAccount.iban)" class="btn btn-danger">Close bank account</button>
</template>