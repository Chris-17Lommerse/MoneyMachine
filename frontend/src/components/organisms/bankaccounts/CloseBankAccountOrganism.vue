<script setup>
import CloseAccountTitle from '../../atoms/bankaccounts/textelements/CloseBankAccountTitle.vue';
import BankAccountTypeListDataFieldAtom from '../../atoms/bankaccounts/textelements/listdatafields/BankAccountTypeListDataFieldAtom.vue';
import ButtonSetMolecule from '../../molecules/bankaccounts/buttons/ButtonSetMolecule.vue';
import AbsoluteLimitItemMolecule from '../../molecules/bankaccounts/items/AbsoluteLimitItemMolecule.vue';
import BalanceItemMolecule from '../../molecules/bankaccounts/items/BalanceItemMolecule.vue';
import DailyTransferLimirtItemMolecule from '../../molecules/bankaccounts/items/DailyTransferLimirtItemMolecule.vue';
import IBANNumberItemMolecule from '../../molecules/bankaccounts/items/IBANNumberItemMolecule.vue';
import IsActiveItemMolecule from '../../molecules/bankaccounts/items/IsActiveItemMolecule.vue';
import SingleTransferLimitItemMolecule from '../../molecules/bankaccounts/items/SingleTransferLimitItemMolecule.vue';

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
    <section class="flex flex-col">
        <CloseAccountTitle />
        <BankAccountTypeListDataFieldAtom :bankAccount="bankAccount" />
        <IBANNumberItemMolecule :bankAccount="bankAccount" />
        <BalanceItemMolecule :bankAccount="bankAccount" />
        <AbsoluteLimitItemMolecule :bankAccount="bankAccount" />
        <DailyTransferLimirtItemMolecule :bankAccount="bankAccount" />
        <IsActiveItemMolecule :bankAccount="bankAccount" />
        <SingleTransferLimitItemMolecule :bankAccount="bankAccount" />
        <ButtonSetMolecule />
    </section>
</template>