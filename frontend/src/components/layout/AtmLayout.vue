<script setup >
    import ATMNavbar from '@/components/organisms/layout/ATMNavbar.vue';
</script>

<template>

    <ATMNavbar />

    <main class="container">
        <router-view></router-view>
    </main>
</template>