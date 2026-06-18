<script setup>
import TransactionsTable from "@/components/organisms/transactions/TransactionsTable.vue";
import { ref, onMounted } from 'vue';
import { useTransactionFilterStore } from '@/stores/transactionFilterStore'
import { useApiHandler } from '@/composables/apihandler'

const { getFilteredTransactions } = useApiHandler()
const transactions = ref([])
const filterStore = useTransactionFilterStore()
const url="/transactions"

onMounted(handleFilterChange)
async function handleFilterChange() {
  transactions.value = await getFilteredTransactions(url)
}

</script>

<template>
    <div class="text-center">
        <h1 class="display-4">All transactions</h1>
        <router-link to="/transactions/create/employee" class="btn btn-primary mb-3">add transaction</router-link>
        <TransactionsTable :transactions="transactions" @filter-change="handleFilterChange" />
    </div>
</template>
