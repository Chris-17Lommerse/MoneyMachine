<script setup>  
    import { onMounted, ref } from 'vue'
    import axios from "@/utils/axios.js"
    import { useAuthStore } from "@/stores/authStore.js"

    const authStore = useAuthStore();
    const user = ref('')

    onMounted(async () => {
        try {
            const response = await axios.get('/auth/admin-test')

            console.log(response.data)
            user.value = response.data
        }
        catch (ex){
            console.log(ex.response.data)
        }
    })
</script>

<template>
    admin authorization 
    <br>
    {{ JSON.stringify(user, null, 2) }}
    <br>
    <br>
    jwt 
    <br>
    {{ JSON.stringify(authStore.decodedAuthToken, null, 2) }}
</template>
