<script setup>
import DailyTransferLimitLabelAtom from "@/atoms/bankaccounts/textelements/labels/DailyTransferLimitLabelAtom.vue";
import BaseListDataFieldAtomForParagraphs from "../../../atoms/bankaccounts/textelements/listdatafields/BaseListDataFieldAtomForParagraphs.vue";

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
</script>

<template>
    <section class="flex flex-row">
    <DailyTransferLimitLabelAtom />
    <BaseListDataFieldAtomForParagraphs :text="bankAccount.dailyTransferLimit" />
    </section>
</template>