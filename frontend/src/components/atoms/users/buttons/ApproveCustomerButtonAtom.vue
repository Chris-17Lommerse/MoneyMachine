<script setup>
import { RouterLink } from 'vue-router';

const props = defineProps({
    userId: {
        type: Number,
        required: true
    }
})

</script>

<template>
    <RouterLink to="/users/:userId/bank-accounts">Approve Custommer</RouterLink>
</template>