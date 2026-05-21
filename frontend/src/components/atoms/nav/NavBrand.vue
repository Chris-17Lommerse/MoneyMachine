<script setup>
    defineProps({
        text: {
            type: String,
            required: true
        }
    })
</script>

<template>
    <div class="navbar-brand">{{ text }}</div>
</template>