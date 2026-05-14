<script setup>
    import { computed } from 'vue';
    import { useAuthStore } from '@/stores/authStore.js'
    import LoginBtn from '@/components/atoms/nav/LoginBtn.vue';
    import LogoutBtn from '@/components/atoms/nav/LogoutBtn.vue';

    const authStore = useAuthStore()
    const atmDecodedAuthToken = computed(() => authStore.atmDecodedAuthToken ?? null)
</script>

<template>
    <div v-if="atmDecodedAuthToken !== null" class="form-inline my-2 my-lg-0">
        <div class="nav-link">
            Logged in as: {{ atmDecodedAuthToken.email }}
        </div>
        <LogoutBtn url="/atm/logout" />
    </div>
    <div v-else class="form-inline my-2 my-lg-0">
        <LoginBtn url="/atm/login" />
    </div>
</template>