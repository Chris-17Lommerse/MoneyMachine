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
        {{ bankAccount.balance }}
    </td>
</template>