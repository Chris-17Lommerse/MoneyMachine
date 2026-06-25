<script setup>
import apiClient from '@/utils/axios.js';
import TransactionsTable from "@/components/organisms/transactions/TransactionsTable.vue";
import { ref,computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/authStore.js'
import { useApiHandler } from '@/composables/apihandler'

const { getFilteredTransactions } = useApiHandler()
const authStore = useAuthStore()
const websiteDecodedAuthToken = computed(() => authStore.websiteDecodedAuthToken ?? null)
const route = useRoute();
const transactions = ref([])
const filter = ref([])
const url=`/bank-accounts/${route.params.iban}/transactions`
onMounted(handleFilterChange)
async function handleFilterChange() {
  transactions.value = await getFilteredTransactions(url)
}
</script>

<template>
    <div class="text-center">
        <h1 class="display-4">Account transactions</h1>
        <router-link v-if="websiteDecodedAuthToken.role === 'EMPLOYEE'" to="/transactions/create/employee" class="btn btn-primary mb-3">add transaction</router-link>
        <router-link v-else to="/transactions/create/user" class="btn btn-primary mb-3">add transaction</router-link>
        <TransactionsTable :transactions="transactions" @filter-change="handleFilterChange" />
    </div>
</template>