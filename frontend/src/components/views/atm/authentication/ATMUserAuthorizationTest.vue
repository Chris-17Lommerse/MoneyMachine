<script setup>  
    import { onMounted, ref } from 'vue'
    import axios from "@/utils/axios.js"
    import { useAuthStore } from "@/stores/authStore.js"

    const authStore = useAuthStore();
    const user = ref('')

    onMounted(async () => {
        try {
            const response = await axios.get('/users/me?loginType=ATM')
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
    user authorization 
    <br>
    {{ JSON.stringify(user, null, 2) }}
    <br>
    <br>
    jwt 
    <br>
    {{ JSON.stringify(authStore.atmDecodedAuthToken, null, 2) }}
</template>
