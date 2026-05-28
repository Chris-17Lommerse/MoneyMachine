<script setup>
const props = defineProps({
    bankAccount: {
        type: Object,
        required: true
    }
})
</script>

<template>
    <td>
        {{ bankAccount.dailyTransferLimit }}
    </td>
</template>