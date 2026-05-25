<script setup>
    import UsersTableGridTemplate from '@/components/organisms/UsersTableGrid.vue';
    import { onMounted, ref } from 'vue';
    import axios from '@/utils/axios';
    import TabMolecule from '@/components/molecules/tabs/TabMolecule.vue';

    const loading = ref(true);
    const error = ref(null);
    const usersWithoutBankAccounts = ref([])

    const fetchUsersWithoutAccount = async () => {
        loading.value = true;
        error.value = null;
        try {
            const result = await axios.get("/users");
            usersWithoutBankAccounts.value = result.data.users;
            console.log(result.data);
        }
        catch (err) {
            console.log("Error fetching users without bank accounts", err);
            error.value =
                err.message || "Failed to fetch users without bank accounts. Please try again later.";
            usersWithoutBankAccounts.value = [];
        }
        finally {
            loading.value = false;
        }
    };

    onMounted(() => {
        fetchUsersWithoutAccount();
    })
</script>

<template>
    <TabMolecule />
    <section>
        <!-- Loading State -->
        <section v-if="loading" class="min-h-screen flex items-center justify-center">
            <section class="text-center">
                <section class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4">
                    <p class="text-blue">Loading Users without bankaccount</p>
                </section>
            </section>
        </section>

        <!-- Error State -->
        <section v-else-if="error" class="min-h-screen flex items-center justify-center">
            <section class="text-red-600 text-5xl mb-4">⚠️</section>
            <h2 class="text-2xl font-bold text-gray-900 mb-2">
                Error Loading Users without bankaccount
            </h2>
            <p class="text-gray-600 mb-4">{{ error }}</p>
            <button @click="fetchUsersWithoutAccount"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                Try Again
            </button>
        </section>
        <UsersTableGridTemplate v-else :users="usersWithoutBankAccounts" />
    </section>
</template>