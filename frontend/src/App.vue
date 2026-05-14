<script setup>
import HelloWorld from './components/HelloWorld.vue'
import UsersWithoutBankAccountPage from './components/pages/UsersWithoutBankAccountPage.vue';
</script>

<template>
    <router-view></router-view>
  <UsersWithoutBankAccountPage />
</template>
