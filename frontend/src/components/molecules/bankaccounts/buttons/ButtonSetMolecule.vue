<script setup>
import CancelCloseBankAccountButton from "@/atoms/bankaccounts/buttons/CancelCloseBankAccountButton.vue";
import CloseBankAccountButton from "@/atoms/bankaccounts/buttons/CloseBankAccountButton.vue";

const props = defineProps({
    bankAccount: {
        type: Object,
        required: true,
        validator: (value) => {
            return typeof value.iban === 'string' &&
                typeof value.userId === 'number' &&
                typeof value.balance === 'number' &&
                typeof value.absoluteLimit === 'number' &&
                typeof value.singleTransferLimit === 'number' &&
                typeof value.dailyTransferLimit === 'number' &&
                typeof value.bankAccountType === 'string' &&
                typeof value.isActive == 'boolean'
        }
    }
})
const emits = defineEmits(['closeAccount'])
</script>

<template>
    <section class="flex flex-row">
        <CloseBankAccountButton @closeAccount="$emit('closeAccount', bankAccount.iban)" :bankAccount="bankAccount" />
        <CancelCloseBankAccountButton />
    </section>
</template>