<script setup>
const emits = defineEmits('createBankAccount');
</script>

<template>
    <button @click="$emit('createBankAccount')" class="btn btn-success">Create bank account</button>
</template>