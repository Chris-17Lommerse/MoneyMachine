<script setup>
    import NavBrand from '@/components/atoms/nav/NavBrand.vue'
    import NavToggler from '@/components/atoms/nav/NavToggler.vue'
    import UserInfo from '@/components/molecules/nav/UserInfo.vue';
    import ATMNavMenu from '@/components/molecules/nav/ATMNavMenu.vue';
</script>

<template>
    <nav class="navbar navbar-expand-xl navbar-light bg-light shadow mb-4">
        <NavBrand text="MoneyMachine ATM" />
        <NavToggler />

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ATMNavMenu />
            <UserInfo :isAtm="true" />
        </div>
    </nav>
</template>