<script setup>
const props = defineProps({
    user: {
        type: Object,
        required: true
    }
})
</script>

<template>
    <h4>{{ user.firstName }}</h4>
</template>