<script setup>
const props = defineProps({
    user: {
        type: Object,
        required: true
    }
})
</script>

<template>
    <h4>{{ user.lastName }}</h4>
</template>