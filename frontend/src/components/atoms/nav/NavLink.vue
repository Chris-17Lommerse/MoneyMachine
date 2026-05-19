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
    <li class="nav-link">
        <RouterLink class="nav-btn" :to="to">{{ text }}</RouterLink>
    </li>
</template>