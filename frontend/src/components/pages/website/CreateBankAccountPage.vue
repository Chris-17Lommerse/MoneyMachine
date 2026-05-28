<script setup>
import { onMounted, ref } from 'vue';
import axios from '@/utils/axios';
import router from '../../../router/router';
const loading = ref(true);
const error = ref(null);
const bankAccounts = ref([]);

const currentRoute = router.currentRoute.value.params;
const fetchUserWithoutABankAccount = async () => {
    loading.value = true;
    error.value = null;
        try {
            const result = await axios.get(`/users/{user_id}/bank-accounts`);
            usersWithoutBankAccounts.value = result.data;
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
        fetchUsersWithoutABankAccount();
    })

</script>

<template>

</template>