<script setup>
    import LoginBtn from '@/components/atoms/nav/LoginBtn.vue';
    import LogoutBtn from '@/components/atoms/nav/LogoutBtn.vue';

    const props = defineProps({
        email: { 
            type: String, 
            default: null 
        },
    })
</script>

<template>
    <div v-if="email !== null" class="form-inline my-2 my-lg-0">
        <div class="nav-link">
            Logged in as: {{ email }}
        </div>
        <LogoutBtn url="/atm/logout" />
    </div>
    <div v-else class="form-inline my-2 my-lg-0">
        <LoginBtn url="/atm/login" />
    </div>
</template>