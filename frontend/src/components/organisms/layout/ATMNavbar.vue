<script setup>
    import NavBrand from '@/components/atoms/nav/NavBrand.vue'
    import NavToggler from '@/components/atoms/nav/NavToggler.vue'
    import NavMenu from '@/components/molecules/nav/NavMenu.vue'
    import UserInfo from '@/components/molecules/nav/UserInfo.vue'
</script>

<template>
    <nav class="navbar navbar-expand-xl navbar-light bg-light shadow mb-4">
        <NavBrand />
        <NavToggler />

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <NavMenu />
            <UserInfo />
        </div>
    </nav>
</template>