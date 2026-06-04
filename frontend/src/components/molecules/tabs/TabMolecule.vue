<script setup>
import BankAccountsTabElementAtom from "@/atoms/tabelements/BankAccountsTabElementAtom.vue";
import TransactionOverviewTabElementAtom from "@/atoms/tabelements/TransactionOverviewTabElementAtom.vue";
import UsersWithoutBankAccountTabElementAtom from "@/atoms/tabelements/UsersWithoutBankAccountTabElementAtom.vue";

</script>

<template>
<ul class="nav nav-underline">
    <BankAccountsTabElementAtom />
    <UsersWithoutBankAccountTabElementAtom />
    <TransactionOverviewTabElementAtom />
</ul>
</template>