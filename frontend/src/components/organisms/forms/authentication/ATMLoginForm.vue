<script setup>
    import { ref } from 'vue'
    import axios from '@/utils/axios.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import { useErrorHandlingStore } from '@/stores/errorHandlingStore.js'
    import router from '@/router/router.js'

    import AuthsubmitBtn from '@/components/atoms/buttons/AuthSubmitBtn.vue'
    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    
    const authStore = useAuthStore()
    const errorHandlingStore = useErrorHandlingStore()

    const email = ref('')
    const password = ref('')
    const errorAlertRef = ref(null)

    async function handleLogin(e) {
        try {
            e.preventDefault()

            const response = await axios.post('/users/login', {
                'email': email.value,
                'password': password.value,
                'loginType': 'ATM'
            })

            authStore.setAtmAuthToken(response.data.accessToken)
            errorHandlingStore.successMessage = 'Successfully logged in.'
            router.push('/atm/select-bank-account')
        }
        catch (ex) {
            if (ex.response){
                errorAlertRef.value.displayErrorMessage(ex.response.data.details)
            }
            else{
                errorAlertRef.value.displayErrorMessage(ex.message)
            }
        }
    }
</script>

<template>
    <ErrorAlert ref="errorAlertRef" />
    <SuccessAlert />

    <form @submit="handleLogin">    
        <BaseFormField labelName="Email" type="email" id="email" placeholder="Enter your email address" v-model="email"/>
        <BaseFormField labelName="Password" type="password" id="password" placeholder="Enter your password" v-model="password"/>
        <AuthsubmitBtn text="Login" />
  </form>
</template>