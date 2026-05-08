<script setup>
    import router from '@/router/router.js'
    import { useAuthStore } from "@/stores/authStore.js"
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore"

    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    authStore.setAuthToken(null)
    errorHandlingStore.setSuccessMessage("Successfully logged out of your account.")

    router.push('/atm/login')
</script>

