<script setup>
    import { onMounted, ref } from 'vue'
    import { useAuthStore } from '@/stores/authStore.js'
    import { useBankAccountStore } from '@/stores/bankAccountStore.js'
    import { getPriceFormatted } from '@/utils/stringFormatter.js'
    import router from '@/router/router.js'

    import ATMActionOptions from '@/components/molecules/atm/ATMActionOptions.vue'
    
    const authStore = useAuthStore()
    const bankAccountStore = useBankAccountStore()
    const bankAccount = ref(null)
    
    onMounted(async () => {
        bankAccount.value = await bankAccountStore.getBankAccountByIbanFromDatabase(router.currentRoute.value.params.iban, '/atm/select-bank-account')        
    }) 
</script>

<template>
    <h3>Welcome, {{ authStore.atmDecodedAuthToken?.firstName }} </h3>
    <h3>{{ router.currentRoute.value.params.iban }} | {{ bankAccount?.bankAccountType }}</h3>
    <h3>Balance: {{ getPriceFormatted(bankAccount?.balance) }} </h3>
    <ATMActionOptions :iban="router.currentRoute.value.params.iban" />
</template>