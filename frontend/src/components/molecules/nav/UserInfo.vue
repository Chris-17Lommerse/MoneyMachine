<script setup>
    import { computed } from 'vue';
    import { useAuthStore } from '@/stores/authStore.js'
    import LoginBtn from '@/components/atoms/nav/LoginBtn.vue';
    import LogoutBtn from '@/components/atoms/nav/LogoutBtn.vue';

    const authStore = useAuthStore()
    const websiteDecodedAuthToken = computed(() => authStore.websiteDecodedAuthToken ?? null)
</script>

<template>
    <div v-if="websiteDecodedAuthToken !== null" class="form-inline my-2 my-lg-0">
        <div class="nav-link">
            Logged in as: {{ websiteDecodedAuthToken.email }}
        </div>
        <LogoutBtn to="/logout" />
    </div>
    <div v-else class="form-inline my-2 my-lg-0">
        <LoginBtn to="/login" />
    </div>
</template>