<script setup>
const props = defineProps({
    text: {
        type: String,
        required: true 
    }
})
</script>

<template>
    <p>{{ text }}</p>
</template>