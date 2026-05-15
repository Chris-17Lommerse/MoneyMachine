<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
const transactions = ref([])
onMounted(async () => {
try {
const result = await axios.get("/api/transactions")
transactions.value = result.data
} catch (error) {
console.log(error)
}
})

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
                <th> iniciated by </th>
                <th> message </th>
            </tr>
        </thead>
        <tbody>
            <div v-for="transaction in transactions" :key="transaction.Id">
                <tr>
                    <td>{{ transaction.FromAccount }}</td>
                    <td> {{ transaction.ToAccount }}</td>
                    <td> {{ transaction.Amount }}</td>
                    <td> {{ transaction.Time }}</td>
                    <td> {{ transaction.Type }}</td>
                    <td> {{ transaction.IniciatedBy }}</td>
                    <td> {{ transaction.Message }}</td>
                </tr>
            </div>
        </tbody>
    </table>
</div>
 
</template>
