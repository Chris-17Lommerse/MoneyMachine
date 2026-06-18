<script setup>
import { ref} from 'vue';
import tableNavbar from "@/components/molecules/transactions/tableNavbar.vue";
const props = defineProps({
    transactions: {
        type: Array,
        required: true
    },
    filter: {
        type: Object,
        required: false,
        default: () => ({
            filterName: "",
            filterValue: ""
        })
    }
})
function formatDateTime(dateString) {
    if (!dateString) return ""

    const date = new Date(dateString)

    return date.toLocaleString("nl-NL", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
    })
}
const emits = defineEmits(['filter-change'])
</script>

<template>
    <tableNavbar :filter="filter" @filter-change="emits('filter-change', $event)" />

    <table class="table table-bordered table-striped table-hover">
        <thead>
            <tr>
                <th> fromAccount </th>
                <th>toAccount </th>
                <th> amount </th>
                <th> time </th>
                <th> initiated by </th>
                <th> message </th>
            </tr>
        </thead>
        <tbody>
            <tr  v-for="transaction in transactions" :key="transaction.transactionId">
                <td>{{ transaction.fromAccountIban }}</td>
                <td> {{ transaction.toAccountIban }}</td>
                <td>€ {{ transaction.amount }}</td>
                <td>{{ formatDateTime(transaction.dateTime) }}</td>
                <td> {{ transaction.initiatingUserId }}</td>
                <td> {{ transaction.message }}</td>
            </tr>
        </tbody>
    </table>
</template>
