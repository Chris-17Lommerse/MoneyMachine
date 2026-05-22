<script setup>
    import { useRoute } from 'vue-router'

    const route = useRoute()
    console.log(route.params.iban)
</script>

<template>
    my bankaccount
</template>