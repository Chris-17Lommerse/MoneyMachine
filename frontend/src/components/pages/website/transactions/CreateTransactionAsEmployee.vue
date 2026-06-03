<script setup>
import apiClient from '@/utils/axios.js'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import TransferForm from '@/components/organisms/forms/transactions/transferForm.vue'
const router = useRouter()

const createTransaction = async (transaction) => {
  try {
    await apiClient.post("transactions/transfer", transaction)
    router.push("/transactions") 
  }catch (error) {
    console.log("FULL ERROR OBJECT:", error)

    console.log("RESPONSE:", error.response)
    console.log("DATA:", error.response?.data)

    console.log("MESSAGE:", error.response?.data?.message)
    console.log("TYPE:", error.response?.data?.errorType)
    console.log("CODE:", error.response?.data?.code)
    console.log("LOCATION:", error.response?.data?.location)

    console.log("STRINGIFIED:", JSON.stringify(error.response?.data))
   

    }
}
</script>
                
<template>
  <router-link to="/transactions" class="btn btn-primary mb-3">Return</router-link>
  <h1>Employee transfer</h1>
  <transferForm  @createTransaction="createTransaction"/>  
 
</template>
