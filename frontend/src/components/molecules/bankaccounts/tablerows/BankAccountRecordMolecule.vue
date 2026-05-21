<script setup>
import AbsoluteLimitDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/AbsoluteLimitDataFieldAtom.vue';
import BalanceDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/BalanceDataFieldAtom.vue';
import BankAccountTypeDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/BankAccountTypeDataFieldAtom.vue';
import DailyTransferLimitDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/DailyTransferLimitDataFieldAtom.vue';
import IBANNumberDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/IBANNumberDataFieldAtom.vue';
import IsActiveDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/IsActiveDataFieldAtom.vue';
import SingleTransferLimitDataFieldAtom from '../../../atoms/bankaccounts/textelements/datafields/SingleTransferLimitDataFieldAtom.vue';
import BSNNumberDataFieldAtom from '../../../atoms/users/textelements/datafields/BSNNumberDataFieldAtom.vue';
import EmailDataFieldAtom from '../../../atoms/users/textelements/datafields/EmailDataFieldAtom.vue';
import FirstNameDataFieldAtom from '../../../atoms/users/textelements/datafields/FirstNameDataFieldAtom.vue';
import LastNameDataFieldAtom from '../../../atoms/users/textelements/datafields/LastNameDataFieldAtom.vue';
import PhoneNumberDataFieldAtom from '../../../atoms/users/textelements/datafields/PhoneNumberDataFieldAtom.vue';
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
        <IBANNumberDataFieldAtom :bank-account="bankAccount" />
        <BankAccountTypeDataFieldAtom :bank-account="bankAccount" />
        <BalanceDataFieldAtom :bank-account="bankAccount" />
        <AbsoluteLimitDataFieldAtom :bank-account="bankAccount" />
        <DailyTransferLimitDataFieldAtom :bank-account="bankAccount" />
        <SingleTransferLimitDataFieldAtom :bank-account="bankAccount" />
        <IsActiveDataFieldAtom :bank-account="bankAccount" />
    </tr>
</template>