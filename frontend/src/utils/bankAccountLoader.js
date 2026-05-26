import axios from '@/utils/axios.js'
import { useErrorHandlingStore } from '@/stores/errorHandlingStore'

async function getBankAccountByIban(iban, failureRedirectUrl) {
    try {
        const errorHandlingStore = useErrorHandlingStore()
        const response = await axios.get('/bank-accounts/' + iban)
        
        return response.data
    }
    catch (ex){
        if (ex.response){
            errorHandlingStore.errorMessage = ex.response.data.details
        }
        else {
            useErrorHandlingStore.errorMessage = ex.details
        }

        if (failureRedirectUrl !== undefined) {
            router.push(failureRedirectUrl)
        }
    }
}

export { getBankAccountByIban }