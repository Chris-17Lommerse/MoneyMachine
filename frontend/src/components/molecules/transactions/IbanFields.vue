<script setup>
import { useAuthStore } from '@/stores/authStore.js'
import { ref, computed } from 'vue'
import EmployeeFromField from '@/components/atoms/transactions/EmployeeFromField.vue'
import UserFromField from '@/components/atoms/transactions/UserFromField.vue'
import ToField from '@/components/atoms/transactions/ToField.vue'
const authStore = useAuthStore()
const websiteDecodedAuthToken = computed(() => authStore.websiteDecodedAuthToken ?? null)

</script>

<template>
    <div>
        <div v-if="websiteDecodedAuthToken.role=='EMPLOYEE'">
            <EmployeeFromField/>
        </div>
        <div v-else-if="websiteDecodedAuthToken.role=='USER'">
            <UserFromField/>
        </div>
        <ToField/>
    </div>
</template>
