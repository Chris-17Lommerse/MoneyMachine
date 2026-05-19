<script setup>
    defineProps({
        to: { 
            type: String, 
            required: true 
        },
        text: {
            type: String,
            default: "Login"
        }
    })
</script>

<template>
    <RouterLink :to="to" class="btn btn-success text-light">{{ text }}</RouterLink>
</template>