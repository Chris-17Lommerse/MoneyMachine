<script setup>
    import NavLink from '@/components/atoms/nav/NavLink.vue'
    import { useAuthStore } from '@/stores/authStore.js'
    import { computed } from 'vue';

    const authStore = useAuthStore()
    const websiteDecodedAuthToken = computed(() => authStore.websiteDecodedAuthToken ?? null)
</script>

<template>
    <ul class="navbar-nav mr-auto">
        <template v-if="websiteDecodedAuthToken !== null">
            <NavLink to="/user-test" text="Test your JWT" /> 
            <NavLink v-if="websiteDecodedAuthToken.role === 'EMPLOYEE'" to="/employee-test" text="Test your employee rights" /> 
            <NavLink to="/users" text="Users" /> 
        </template>

        <NavLink to="/atm/login" text="Go to ATM" /> 
    </ul>
</template>