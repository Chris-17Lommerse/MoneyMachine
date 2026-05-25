<script setup >
    import Navbar from '@/components/organisms/layout/Navbar.vue'
</script>

<template>

    <Navbar />

    <main class="container">
        <router-view></router-view>
    </main>
</template>