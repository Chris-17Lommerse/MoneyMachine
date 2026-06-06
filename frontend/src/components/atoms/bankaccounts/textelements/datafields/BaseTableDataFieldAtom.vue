<script setup>
const props = defineProps({
    text: {
        type: String,
        required: true 
    }
})
</script>

<template>
    <td>{{ text }}</td>
</template>