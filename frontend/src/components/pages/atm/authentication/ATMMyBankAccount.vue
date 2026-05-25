<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute } from 'vue-router'
    import axios from '@/utils/axios.js'
    import router from '@/router/router.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    
    const errorHandlingStore = useErrorHandlingStore()
    const route = useRoute()
    const bankAccount = ref(null)
    
    onMounted(async () => {
        try {
            const response = await axios.get('/bank-accounts/' + route.params.iban)
            bankAccount.value = response.data

            console.log(bankAccount.value)
        }
        catch (ex){
            if (ex.response){
                errorHandlingStore.errorMessage = ex.response.data.message
                router.push('/atm/select-bank-account')
            }
            else {
                useErrorHandlingStore.errorMessage = ex.message
            }
        }
    })
</script>

<template>
    
</template>