<script setup>
import apiClient from '@/utils/axios.js';
import TransactionsTable from "@/components/organisms/TransactionsTable.vue";
import { ref,computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/authStore.js'


const authStore = useAuthStore()
const websiteDecodedAuthToken = computed(() => authStore.websiteDecodedAuthToken ?? null)
const route = useRoute();
const transactions = ref([])
onMounted(async () => {
    try {

        const response = await apiClient.get(`/users/${route.params.id}/transactions`)

         console.log("RESPONSE:", response)

        if (response.status === 200) {

            transactions.value = response.data.transactions


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
</script>

<template>
    <div class="text-center">
        <h1 class="display-4">User transactions</h1>
        <router-link v-if="websiteDecodedAuthToken.role === 'EMPLOYEE'" to="/transactions/create/employee" class="btn btn-primary mb-3">add transaction</router-link>
        <router-link v-else to="/transactions/create/user/" class="btn btn-primary mb-3">add transaction</router-link>
        <TransactionsTable :transactions="transactions" />
    </div>
</template>