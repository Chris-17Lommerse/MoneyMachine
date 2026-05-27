<script setup>
import SingleTransferLimitListDataFieldAtom from '../../../atoms/bankaccounts/textelements/listdatafields/SingleTransferLimitListDataFieldAtom.vue';
import SingleTransferLimitLabelAtom from '../../../atoms/bankaccounts/textelements/labels/SingleTransferLimitLabelAtom.vue';

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
    <section class="flex flex-row">
    <SingleTransferLimitLabelAtom />
    <SingleTransferLimitListDataFieldAtom :bankAccount="bankAccount"/>
    </section>
</template>