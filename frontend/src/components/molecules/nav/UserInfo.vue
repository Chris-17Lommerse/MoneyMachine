<script setup>
    import ATMLoginBtn from '@/components/atoms/nav/ATMLoginBtn.vue';
    import ATMLogoutBtn from '@/components/atoms/nav/ATMLogoutBtn.vue';

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
        <ATMLogoutBtn />
    </div>
    <div v-else class="form-inline my-2 my-lg-0">
        <ATMLoginBtn />
    </div>
</template>