<script setup>
    import { computed } from 'vue';
    import { useAuthStore } from '@/stores/authStore.js'
    import LoginBtn from '@/components/atoms/nav/LoginBtn.vue';
    import LogoutBtn from '@/components/atoms/nav/LogoutBtn.vue';

    const props = defineProps({
        isAtm: {
            type: Boolean,
            default: false
        }
    })

    const authStore = useAuthStore()
    const decodedAuthToken = computed(() => {

        if (props.isAtm === true) {
            return authStore.atmDecodedAuthToken;
        }
      
        return authStore.websiteDecodedAuthToken
    })
</script>

<template>
    <div v-if="decodedAuthToken !== null" class="form-inline my-2 my-lg-0">
        <div class="nav-link">
            Logged in as: {{ decodedAuthToken.email }}
        </div>
        <LogoutBtn :isAtm="props.isAtm" />
    </div>
    <div v-else class="form-inline my-2 my-lg-0">
        <LoginBtn :isAtm="props.isAtm" />
    </div>
</template>