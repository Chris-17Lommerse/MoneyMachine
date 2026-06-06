<script setup>
import AbsoluteLimitItemMolecule from "@/molecules/bankaccounts/items/AbsoluteLimitItemMolecule.vue";
import BalanceItemMolecule from "@/molecules/bankaccounts/items/BalanceItemMolecule.vue";
import DailyTransferLimitItemMolecule from "@/molecules/bankaccounts/items/DailyTransferLimitItemMolecule.vue";
import IBANNumberItemMolecule from "@/molecules/bankaccounts/items/IBANNumberItemMolecule.vue";
import IsActiveItemMolecule from "@/molecules/bankaccounts/items/IsActiveItemMolecule.vue";
import SingleTransferLimitItemMolecule from "@/molecules/bankaccounts/items/SingleTransferLimitItemMolecule.vue";
import BaseListDataFieldAtomForHeadings from "@/atoms/bankaccounts/textelements/listdatafields/BaseListDataFieldAtomForHeadings.vue";
import BankAccountCreationTitle from "../../atoms/bankaccounts/textelements/BankAccountCreationTitle.vue";

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

const emits = defineEmits(['createBankAccount'])
</script>

<template>
    <section class="flex flex-col">
        <BankAccountCreationTitle />
        <BaseListDataFieldAtomForHeadings :text="bankAccount.bankAccountType" />
        <IBANNumberItemMolecule :bankAccount="bankAccount" />
        <BalanceItemMolecule :bankAccount="bankAccount" />
        <AbsoluteLimitItemMolecule :bankAccount="bankAccount" />
        <DailyTransferLimitItemMolecule :bankAccount="bankAccount" />
        <IsActiveItemMolecule :bankAccount="bankAccount" />
        <SingleTransferLimitItemMolecule :bankAccount="bankAccount" />
    </section>
</template>