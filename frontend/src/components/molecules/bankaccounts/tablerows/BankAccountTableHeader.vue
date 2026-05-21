<script setup>
import ActionsTableHeaderAtom from '../../../atoms/users/textelements/tableheaders/ActionsTableHeaderAtom.vue';
import BSNNumberTableHeaderAtom from '../../../atoms/users/textelements/tableheaders/BSNNumberTableHeaderAtom.vue';
import EmailTableHeaderAtom from '../../../atoms/users/textelements/tableheaders/EmailTableHeaderAtom.vue';
import FirstNameTableHeaderAtom from '../../../atoms/users/textelements/tableheaders/FirstNameTableHeaderAtom.vue';
import LastNameTableHeaderAtom from '../../../atoms/users/textelements/tableheaders/LastNameTableHeaderAtom.vue';
import PhoneNumberTableHeaderAtom from '../../../atoms/users/textelements/tableheaders/PhoneNumberTableHeaderAtom.vue';
import AbsoluteLimitTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/AbsoluteLimitTableHeaderAtom.vue';
import BalanceTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/BalanceTableHeaderAtom.vue';
import BankAccountTypeTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/BankAccountTypeTableHeaderAtom.vue';
import DailyTransferLimitTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/DailyTransferLimitTableHeaderAtom.vue';
import IBANNumberTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/IBANNumberTableHeaderAtom.vue';
import SingleTransferLimitTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/SingleTransferLimitTableHeaderAtom.vue';
import SingleTransferLimitTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/SingleTransferLimitTableHeaderAtom.vue';
import IsActiveTableHeaderAtom from '../../../atoms/bankaccounts/textelements/tableaheaders/IsActiveTableHeaderAtom.vue';
</script>

<template>
    <tr class="table table-light">
        <IBANNumberTableHeaderAtom />
        <BankAccountTypeTableHeaderAtom />
        <BalanceTableHeaderAtom />
        <AbsoluteLimitTableHeaderAtom />
        <DailyTransferLimitTableHeaderAtom />
        <SingleTransferLimitTableHeaderAtom />
        <IsActiveTableHeaderAtom />
        <ActionsTableHeaderAtom />
    </tr>
</template>