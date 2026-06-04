<script setup>
    defineProps({
        to: { 
            type: String, 
            required: true 
        },
        text: { 
            type: String, 
            required: true 
        },
    })
</script>

<template>
    <RouterLink class="btn btn-secondary btn-lg px-4 py-4 mr-2" :to="to">{{ text }}</RouterLink>
</template>