<script setup>
    import { onMounted, ref, computed } from 'vue'
    import { useAuthStore } from '@/stores/authStore.js'

    import NavBrand from '@/components/atoms/nav/NavBrand.vue'
    import NavToggler from '@/components/atoms/nav/NavToggler.vue'
    import NavMenu from '@/components/molecules/nav/NavMenu.vue'
    import UserInfo from '@/components/molecules/nav/UserInfo.vue'

    const authStore = useAuthStore()
    const atmDecodedAuthToken = ref(null)

    onMounted(async () => {
        atmDecodedAuthToken.value = authStore.atmDecodedAuthToken
    })
</script>

<template>
    <nav class="navbar navbar-expand-xl navbar-light bg-light shadow mb-4">
        <NavBrand />
        <NavToggler />

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <NavMenu />
            <UserInfo :email="atmDecodedAuthToken?.email" />
        </div>
    </nav>
</template>