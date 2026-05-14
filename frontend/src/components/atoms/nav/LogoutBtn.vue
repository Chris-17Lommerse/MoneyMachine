<script setup>
    defineProps({
        url: { 
            type: String, 
            required: true 
        },
    })
</script>

<template>
    <a :href="url" class="btn btn-danger text-light">Logout</a>
</template>