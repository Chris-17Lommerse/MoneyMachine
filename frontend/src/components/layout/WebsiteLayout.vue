<script setup >
    import Navbar from '@/components/organisms/layout/Navbar.vue'
</script>

<template>

    <Navbar />

    <main class="container vh-100">
        <RouterView />
    </main>
</template>