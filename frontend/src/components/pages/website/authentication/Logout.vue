<script setup>
    import router from '@/router/router.js'
    import { useAuthStore } from "@/stores/authStore.js"
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore"

    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    if (authStore.websiteAuthToken !== null) {
        authStore.setWebsiteAuthToken(null)
        errorHandlingStore.successMessage = "Successfully logged out of your account."
    }
    else {
        errorHandlingStore.errorMessage = "You already are logged out."
    }

    router.push('/login')
</script>

