<script setup>
import { onMounted, ref } from 'vue';
import axios from '@/utils/axios';
import CloseBankAccountOrganism from '../../organisms/bankaccounts/CloseBankAccountOrganism.vue';

const loading = ref(true);
const error = ref(null);
const bankAccount = ref();

const fetchBankAccount = async () => {
    loading.value = true;
    error.value = null;
    try {
        const result = await axios.get("/bank-accounts.{iban}");
        bankAccount.value = result.data;
        console.log(result.data)
    }
    catch (err) {
        console.log("Error fetching bank account", err);
        error.value =
            err.message || "Failed to fetch bank account. Please try again later.";
        bankAccount.value = [];
    }
    finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchBankAccount();
})
</script>

<template>
    <section>
        <!-- Loading State -->
        <section v-if="loading" class="min-h-screen flex items-center justify-center">
            <section class="text-center">
                <section class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4">
                    <p class="text-blue">Loading bankaccount</p>
                </section>
            </section>
        </section>

        <!-- Error State -->
        <section v-else-if="error" class="min-h-screen flex items-center justify-center">
            <section class="text-red-600 text-5xl mb-4">⚠️</section>
            <h2 class="text-2xl font-bold text-gray-900 mb-2">
                Error Loading a bankaccount
            </h2>
            <p class="text-gray-600 mb-4">{{ error }}</p>
            <button @click="fetchBankAccount"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                Try Again
            </button>
        </section>
        <CloseBankAccountOrganism v-else :bankAccount="bankAccount" />
    </section>
</template>