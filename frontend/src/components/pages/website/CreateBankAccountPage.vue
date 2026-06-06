<script setup>
import { onMounted, ref } from 'vue';
import axios from '@/utils/axios';
import ApproveCustomerOrganism from '@/organisms/users/ApproveCustomerOrganism.vue';
import { useRouter } from 'vue-router';
import { useAccountApprovalStore } from '@/stores/accountApprovalStore.js';
const loading = ref(false);
const error = ref(null);
const bankAccounts = ref([]);
const router = useRouter();
const currentRoute = router.currentRoute.value.params;
const accountApprovalStore = useAccountApprovalStore();
function fetchUsersWithoutAccount() {
    loading.value = true;
    error.value = null
    accountApprovalStore.pendingBankAccounts;
}

const approveAndCreateAccounts = async () => {
    loading.value = true;
    error.value = null;
    try {
        if (!accountApprovalStore.selectedUserId) {
            throw new Error("No selected user")
        }
        await axios.post("/bank-accounts", accountApprovalStore.pendingBankAccounts.checking);
        await axios.post("/bank-accounts", accountApprovalStore.pendingBankAccounts.savings);

        accountApprovalStore.clearApproval();

        await router.push({
            path: "/users",
            query: {
                success: "Successfully created bank accounts"
            }
        })
    } catch (err) {
        console.log("Error creating bank accounts", err);
        error.value =
            err?.response?.data?.details ||
            err.message || "Failed to create bank accounts. Please try again later.";
    } finally {
        loading.value = false;
    }
}

const cancel = () => {
    accountApprovalStore.clearApproval();
    router.push("/users");
}
</script>

<template>
    <section>
        <!-- Loading State -->
        <section v-if="loading" class="min-h-screen flex items-center justify-center">
            <section class="text-center">
                <section class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4">
                    <p class="text-blue">Creating bankaccounts...</p>
                </section>
            </section>
        </section>

        <!-- Error State -->
        <section v-else-if="error" class="min-h-screen flex items-center justify-center">
            <section class="text-red-600 text-5xl mb-4">⚠️</section>
            <h2 class="text-2xl font-bold text-gray-900 mb-2">
                Error creating a checkings and savings bankaccount
            </h2>
            <p class="text-gray-600 mb-4">{{ error }}</p>
            <button @click="approveAndCreateAccounts"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                Try Again
            </button>
            <button @click="cancel"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                Cancel
            </button>
        </section>
        <ApproveCustomerOrganism @createBankAccount="approveAndCreateAccounts" v-else
            :bankAccount="accountApprovalStore.pendingBankAccounts.checking" +
            :bankaccount="accountApprovalStore.pendingBankAccounts.savings" />
    </section>
</template>