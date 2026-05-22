<script setup>
import apiClient from '@/utils/axios.js';
import { ref, onMounted } from 'vue';
const transactions = ref([])
onMounted(async () => {
    try {

        const response = await apiClient.get("/api/transactions")

         console.log("RESPONSE:", response)

        if (response.status === 200) {

            transactions.value = response.data


        }

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
})
function formatDateTime(dateString) {
    if (!dateString) return ""

    const date = new Date(dateString)

    return date.toLocaleString("nl-NL", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
    })
}

</script>

<template>
    <div class="text-center">
    <h1 class="display-4">All Transactions</h1>
    <router-link to="/transactions/create" class="btn btn-primary mb-3">add transaction</router-link>

    <table class="table table-bordered table-striped table-hover">
        <thead>
            <tr>
                <th> fromAccount </th>
                <th>toAccount </th>
                <th> amount </th>
                <th> time </th>
                <th> type </th>
                <th> initiated by </th>
                <th> message </th>
            </tr>
        </thead>
        <tbody>
            <tr  v-for="transaction in transactions" :key="transaction.transactionId">
                <td>{{ transaction.fromAccount }}</td>
                <td> {{ transaction.toAccount }}</td>
                <td>€ {{ transaction.amount }}</td>
               <td>{{ formatDateTime(transaction.time) }}</td>
                <td> {{ transaction.type }}</td>
                <td> {{ transaction.initiatedBy }}</td>
                <td> {{ transaction.message }}</td>
            </tr>
        </tbody>
    </table>
</div>
 
</template>
