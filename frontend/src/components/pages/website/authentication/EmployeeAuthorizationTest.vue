<script setup>  
    import { onMounted, ref } from 'vue'
    import axios from "@/utils/axios.js"
    import { useAuthStore } from "@/stores/authStore.js"

    const authStore = useAuthStore();
    const response = ref('')

    onMounted(async () => {
        try {
            response.value = await axios.get('/auth/employee-test')
        }
        catch (ex){
            if (ex.response){
                console.log(ex.response.data)
            }
        }
    })
</script>

<template>
    <strong>Response from backend:</strong>
    <br>
    {{ response.data }}
    <br>
</template>
