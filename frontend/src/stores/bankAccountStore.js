import { defineStore } from 'pinia'
import { ref } from'vue'
import axios from '@/utils/axios.js'
import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
import router from '@/router/router.js'

export const useBankAccountStore = defineStore('bankAccount', () => {

    const bankAccount = ref(null)

    async function getBankAccountByIbanFromDatabase(iban, failureRedirectUrl) {
        
        const errorHandlingStore = useErrorHandlingStore()
        
        try {
            const response = await axios.get('/bank-accounts/' + iban)
            bankAccount.value = response.data

            return bankAccount.value
        }
        catch (ex){
            if (ex.response){
                errorHandlingStore.errorMessage = ex.response.data.message
            }
            else {
                errorHandlingStore.errorMessage = ex.message
            }
    
            if (failureRedirectUrl !== undefined) {
                router.push(failureRedirectUrl)
            }
        }
    }

    async function getBankAccountByIban(iban, failureRedirectUrl) {
        
        if (bankAccount.value === null || bankAccount.value.iban !== iban) {
            return await getBankAccountByIbanFromDatabase(iban, failureRedirectUrl)
        }

        return bankAccount.value
    }

    return {getBankAccountByIban, getBankAccountByIbanFromDatabase}
})