<script setup>
    defineProps({
        to: { 
            type: String, 
            required: true 
        },
        text: {
            type: String,
            default: "Logout"
        }
    })
</script>

<template>
    <RouterLink :to="to" class="btn btn-danger text-light">{{ text }}</RouterLink>
</template>