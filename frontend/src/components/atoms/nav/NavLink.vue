<script setup>
    defineProps({
        href: { 
            type: String, 
            required: true 
        },
        label: { 
            type: String, 
            required: true 
        },
    })
</script>

<template>
    <li class="nav-link">
        <a class="nav-btn" :href="href">{{ label }}</a>
    </li>
</template>