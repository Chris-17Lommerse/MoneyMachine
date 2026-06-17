<script setup>
import apiClient from '@/utils/axios.js';
import TransactionsTable from "@/components/organisms/TransactionsTable.vue";
import { ref, onMounted } from 'vue';
const transactions = ref([])
const filter = ref([])
onMounted(async () => {
     getFilteredTransactions()
    })
function handleFilterChange(newFilter) {
    filter.value = newFilter
   getFilteredTransactions()
}
const getFilteredTransactions = async () => {
  try {
    const response = await apiClient.get("/transactions", {
       params: {
    filterName: filter.value.filterName,
    filterValue: filter.value.filterValue
    }
    })

    console.log("RESPONSE:", response)

    if (response.status === 200) {
      transactions.value = response.data.transactions
    }

  } catch (error) {
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
    <div class="text-center">
        <h1 class="display-4">All transactions</h1>
        <router-link to="/transactions/create/employee" class="btn btn-primary mb-3">add transaction</router-link>
        <TransactionsTable :transactions="transactions" :filter="filter" @filter-change="handleFilterChange" />
    </div>
</template>
