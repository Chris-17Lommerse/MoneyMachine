<script setup>  
    import { onMounted, ref } from 'vue'
    import axios from "@/utils/axios.js"
    import { useAuthStore } from "@/stores/authStore.js"
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'

    const authStore = useAuthStore()
    const user = ref('')

    onMounted(async () => {
        try {
            const response = await axios.get('/auth/me')
            user.value = response.data
        }
        catch (ex){
            if (ex.response){
                console.log(ex.response.data)
            }
        }
    })
</script>

<template>
    <SuccessAlert />
    <strong>Logged in user returned from backend</strong>
    <br>
    {{ JSON.stringify(user, null, 2) }}
    <br>
    <br>
    <strong>Decoded JWT from frontend cached in authStore</strong> 
    <br>
    {{ JSON.stringify(authStore.websiteDecodedAuthToken, null, 2) }}
</template>
