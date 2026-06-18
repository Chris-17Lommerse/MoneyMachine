<script setup>
    import NavLink from '@/components/atoms/nav/NavLink.vue'
    import FilterButton from '@/components/atoms/transactions/filterButton.vue'
    import { useTransactionFilterStore } from '@/stores/transactionFilterStore'

    const store = useTransactionFilterStore()
    const emit = defineEmits(["filter-change"])
    const props = defineProps({
   filter:{
        type: Object,
        required: false,
        default: () => ({
        filterName: "",
        filterValue: ""
    })
    }

    
})

function handleFilter(payload) {

  if (payload.filterName === 'reset') {
    store.resetFilters()
    emit("filter-change")
    return
  }

  store.setFilter(payload.filterName, payload.filterValue)
   emit("filter-change")
}
</script>

<template>
    
    <div class="filter-toolbar">

    
        <div class="group">
            <FilterButton
            label="All"
            filterName="reset"
            @filter-change="handleFilter"
            :instant="true"
            />
        </div>

       
        <div class="group">
            <FilterButton
                label="From IBAN"
                filterName="fromIban"
                @filter-change="handleFilter"
            />
            <FilterButton
                label="To IBAN"
                filterName="toIban"
                @filter-change="handleFilter"
            />
        </div>

    
        <div class="group">
            <FilterButton
                label="Amount >"
                filterName="minAmount"
                @filter-change="handleFilter"
            />
            <FilterButton
                label="Amount <"
                filterName="maxAmount"
                @filter-change="handleFilter"
            />
            <FilterButton
                label="Amount ="
                filterName="exactAmount"
                @filter-change="handleFilter"
            />
        </div>

    
        <div class="group">
            <FilterButton
                label="Before"
                filterName="startDate"
                @filter-change="handleFilter"
            />
            <FilterButton
                label="After"
                filterName="endDate"
                @filter-change="handleFilter"
            />
            <FilterButton
                label="Exact"
                filterName="exactDate"
                @filter-change="handleFilter"
            />
        </div>

    
        <div class="group">
            <FilterButton
                label="User"
                filterName="userId"
                @filter-change="handleFilter"
            />
        </div>

    </div>

</template>
<style scoped>
.filter-toolbar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.group {
  display: flex;
  gap: 6px;
  padding: 4px;
  border-right: 1px solid #ddd;
}
.group:last-child {
  border-right: none;
}
</style>
