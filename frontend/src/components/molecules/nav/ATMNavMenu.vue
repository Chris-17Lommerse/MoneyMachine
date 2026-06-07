<script setup>
    import NavLink from '@/components/atoms/nav/NavLink.vue'
    import { useAuthStore } from '@/stores/authStore.js'
    import { computed } from 'vue';

    const authStore = useAuthStore()
    const atmDecodedAuthToken = computed(() => authStore.atmDecodedAuthToken ?? null)
</script>

<template>
    <ul class="navbar-nav mr-auto">
        <template v-if="atmDecodedAuthToken !== null">
            <NavLink to="/atm/select-bank-account" text="Select bank account" />  
        </template>

        <NavLink to="/login" text="Go to website" />
    </ul>
</template>