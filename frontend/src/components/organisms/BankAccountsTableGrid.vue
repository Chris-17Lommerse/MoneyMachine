<script setup>
import BankAccountRecordMolecule from '../molecules/bankaccounts/tablerows/BankAccountRecordMolecule.vue';
import BankAccountTableHeader from '../molecules/bankaccounts/tablerows/BankAccountTableHeader.vue';

const props = defineProps({
    bankAccounts: {
        type: Object,
        required: true,
        validator: (value) => {
            return typeof value.iban === 'string' &&
                typeof value.user === 'object' &&
                typeof value.balance === 'number' &&
                typeof value.absoluteLimit === 'number' &&
                typeof value.singleTransferLimit === 'number' &&
                typeof value.dailyTransferLimit === 'number' &&
                typeof value.bankAccountType === 'string' &&
                typeof value.isActive == 'boolean'
        }
    }
})
</script>

<template>
    <table class="table table-striped">
        <thead>
            <BankAccountTableHeader />
        </thead>
        <tbody>
            <BankAccountRecordMolecule v-for="bankAccount in bankAccounts" :key="bankAccount.iban" :bankAccount="bankAccount" />
        </tbody>
    </table>
</template>