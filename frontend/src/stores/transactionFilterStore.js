import { defineStore } from 'pinia'

export const useTransactionFilterStore = defineStore('transactionFilter', {
  state: () => ({
    filters: {
      fromIban: null,
      toIban: null,
      minAmount: null,
      maxAmount: null,
      exactAmount: null,
      startDate: null,
      endDate: null,
      exactDate: null,
      userId: null
    },

    transactions: [],
    loading: false
  }),

  actions: {

    setFilter(name, value) {
      this.filters[name] = value
    },

    resetFilters() {
      Object.keys(this.filters).forEach(k => {
        this.filters[k] = null
      })
    },
  }
})