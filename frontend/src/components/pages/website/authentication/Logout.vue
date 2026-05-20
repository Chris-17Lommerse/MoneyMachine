<script setup>
    import router from '@/router/router.js'
    import { useAuthStore } from "@/stores/authStore.js"
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore"

    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    if (authStore.atmAuthToken !== null) {
        authStore.setWebsiteAuthToken(null)
        errorHandlingStore.setSuccessMessage("Successfully logged out of your account.")
    }
    else {
        errorHandlingStore.setErrorMessage("You already are logged out.")
    }

    router.push('/login')
</script>

