import apiClient from '@/utils/axios.js';
import { useTransactionFilterStore } from '@/stores/transactionFilterStore'
export function useApiHandler() {
  const filterStore=useTransactionFilterStore()
  const getFilteredTransactions = async (url) => {
    try {
      const response = await apiClient.get(url, { params: filterStore.filters })

      console.log("RESPONSE:", response)
      return response.data.transactions

    } catch (error) {
      console.log("FULL ERROR OBJECT:", error)
    }
  
  }
  return {
      getFilteredTransactions
    }
}
