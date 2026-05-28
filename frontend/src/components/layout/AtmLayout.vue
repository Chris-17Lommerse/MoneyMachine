<script setup >
    import ATMNavbar from '@/components/organisms/layout/ATMNavbar.vue';
</script>

<template>

    <ATMNavbar />

    <main class="container vh-100">
        <RouterView />
    </main>
</template>