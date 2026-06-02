<script setup>
import CloseBankAccountButtonAtom from "@/atoms/bankaccounts/buttons/CloseBankAccountButtonAtom.vue";
import TransferMoneyButtonAtom from "@/atoms/bankaccounts/buttons/TransferMoneyButtonAtom.vue";
import UpdateTransferLimitsButtonAtom from "@/atoms/bankaccounts/buttons/UpdateTransferLimitsButtonAtom.vue";
import ViewTransactionHistoryButtonAtom from "@/atoms/bankaccounts/buttons/ViewTransactionHistoryButtonAtom.vue";
import BaseTableDataFieldAtom from "@/atoms/bankaccounts/textelements/datafields/BaseTableDataFieldAtom.vue";
const props = defineProps({
    bankAccount: {
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
    <tr class="tabe table-dark">
        <BaseTableDataFieldAtom :text="bankAccount.iban" />
        <BaseTableDataFieldAtom :text="bankAccount.bankAccountType" />
        <BaseTableDataFieldAtom :text="bankAccount.balance" />
        <BaseTableDataFieldAtom :text="bankAccount.absoluteLimit" />
        <BaseTableDataFieldAtom :text="bankAccount.dailyTransferLimit" />
        <BaseTableDataFieldAtom :text="bankAccount.singleTransferLimit" />
        <BaseTableDataFieldAtom :text="bankAccount.isActive" />
        <td class="flex flex-column">
            <CloseBankAccountButtonAtom :bankAccount="bankAccount" />
            <UpdateTransferLimitsButtonAtom />
            <ViewTransactionHistoryButtonAtom />
            <TransferMoneyButtonAtom />
        </td>
    </tr>
</template>