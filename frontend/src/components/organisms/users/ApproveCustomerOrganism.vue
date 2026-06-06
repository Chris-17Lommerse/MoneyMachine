<script setup>
import AbsoluteLimitItemMolecule from "@/molecules/bankaccounts/items/AbsoluteLimitItemMolecule.vue";
import BalanceItemMolecule from "@/molecules/bankaccounts/items/BalanceItemMolecule.vue";
import DailyTransferLimitItemMolecule from "@/molecules/bankaccounts/items/DailyTransferLimitItemMolecule.vue";
import IBANNumberItemMolecule from "@/molecules/bankaccounts/items/IBANNumberItemMolecule.vue";
import IsActiveItemMolecule from "@/molecules/bankaccounts/items/IsActiveItemMolecule.vue";
import SingleTransferLimitItemMolecule from "@/molecules/bankaccounts/items/SingleTransferLimitItemMolecule.vue";
import BaseListDataFieldAtomForHeadings from "@/atoms/bankaccounts/textelements/listdatafields/BaseListDataFieldAtomForHeadings.vue";
import BankAccountCreationTitle from "@/atoms/bankaccounts/textelements/BankAccountCreationTitle.vue";
import CancelBankAccountCreationButton from "@/atoms/bankaccounts/buttons/CancelBankAccountCreationButton.vue";
import CreateBankAccountButtonAtom from "@/atoms/bankaccounts/buttons/CreateBankAccountButtonAtom.vue";
const props = defineProps({
    bankAccount: {
        type: Object,
        required: true,
        validator: (value) => {
            return typeof value.iban === 'string' &&
                typeof value.userId === 'number' &&
                typeof value.bankAccountType === 'string'
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
        <section class="flex flex-row">
            <CreateBankAccountButtonAtom @createBankAccount="$emit('createBankAccount')" />
            <CancelBankAccountCreationButton />
        </section>
    </section>
</template>