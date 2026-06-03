<script setup>
const props = defineProps({
    text: {
        type: String,
        required: true 
    }
})
</script>

<template>
    <h2>{{ text }}</h2>
</template>