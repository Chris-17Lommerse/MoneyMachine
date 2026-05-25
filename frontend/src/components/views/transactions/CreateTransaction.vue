<script setup>
import apiClient from '@/utils/axios.js'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()

const transaction = ref({
  fromAccount: "",
  toAccount: "",
  amount: "",
  message: "",
  initiatedBy: ""
})

const createTransaction = async () => {
  try {
    await apiClient.post("transactions/transfer", transaction.value)
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
  <router-link to="/transactions" class="btn btn-primary mb-3">terug</router-link>
  <form @submit.prevent="createTransaction">
    <div class="mb-3">
      <label for="fromAccount" class="form-label">From Account</label>
      <input v-model="transaction.fromAccount" type="text" class="form-control" id="fromAccount" name="fromAccount">
    </div>
    <div class="mb-3">
      <label for="toAccount" class="form-label">To Account</label>
      <input v-model="transaction.toAccount" type="text" class="form-control" id="toAccount" name="toAccount">
    </div>
    <div class="mb-3">
      <label for="amount" class="form-label">Amount</label>
      <input v-model="transaction.amount" type="number" class="form-control" id="amount" name="amount">
    </div>
    <div class="mb-3">
      <label for="message" class="form-label">Message</label>
      <input v-model="transaction.message" type="text" class="form-control" id="message" name="message">
    </div>
     <div class="mb-3">
      <label for="initiatedBy" class="form-label">Initiated By</label>
      <input v-model="transaction.initiatedBy" type="text" class="form-control" id="initiatedBy" name="initiatedBy">
    </div>
    <button type="submit" class="btn btn-primary">create</button>

  </form>
 
</template>
