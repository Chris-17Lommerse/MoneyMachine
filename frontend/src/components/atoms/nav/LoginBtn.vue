<script setup>
    defineProps({
        url: { 
            type: String, 
            required: true 
        },
    })
</script>

<template>
    <a :href="url" class="btn btn-success text-light">Login</a>
</template>