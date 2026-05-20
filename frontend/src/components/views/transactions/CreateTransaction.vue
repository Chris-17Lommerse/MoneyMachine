<script setup>
console.log("🔥 THIS IS THE FORM COMPONENT")
import axios from '@/utils/axios.js'
import { ref } from 'vue'

const transaction = ref({
  fromAccount: "",
  toAccount: "",
  amount: "",
  message: "",
  initiatedBy: ""
})

const createTransaction = async () => {
  try {
    await axios.post("/api/transactions", transaction.value)
  } catch (error) {
    console.log(error)
  }
}
</script>
                <td> {{ transaction.type }}</td>
                

<template>
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
    <button @click="createTransaction" class="btn btn-primary">Submit</button>

  </form>
 
</template>
