<script setup>
import { onMounted, ref } from 'vue';
import axios from '@/utils/axios';

const loading = ref(true);
const error = ref(null);
const userWithoutBankAccount = ref([]);

const fetchUserWithoutABankAccount = async () => {
    loading.value = true;
    error.value = null;
}

</script>

<template>

</template>