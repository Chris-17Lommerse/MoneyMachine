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

            <!-- USER LINKS -->

            <template v-if="websiteDecodedAuthToken.role === 'EMPLOYEE'">
                <NavLink to="/users" text="Users" />
                <NavLink to="/bank-accounts" text="Bank Accounts" />
            </template>
        </template>

        <NavLink to="/atm/login" text="Go to ATM" /> 
    </ul>
</template>