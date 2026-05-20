<script setup>
    import { ref } from 'vue'
    import axios from '@/utils/axios.js'
    import { useAuthStore } from '@/stores/authStore.js'
    import AuthsubmitBtn from '@/components/atoms/buttons/AuthSubmitBtn.vue'
    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'
    import SuccessAlert from '@/components/atoms/errorHandling/SuccessAlert.vue'
    import ErrorAlert from '@/components/atoms/errorHandling/ErrorAlert.vue'
    
    const authStore = useAuthStore();

    const email = ref('')
    const password = ref('')
    const currentErrorAlert = ref(null)
    const currentSuccessAlert = ref(null)

    async function handleLogin(e) {
        try {
            e.preventDefault()

            const form = new FormData()
            form.append('email', email.value)
            form.append('password', password.value)
            form.append('loginType', 'WEBSITE')

            const response = await axios.post('/users/login', form)

            authStore.setWebsiteAuthToken(response.data.accessToken)
            currentSuccessAlert.value.displaySuccessMessage('Successfully logged in.')
        }
        catch (ex){
            if (ex.response){
                currentErrorAlert.value.displayErrorMessage(ex.response.data.message)
            }
        }
    }
</script>

<template>
    <form @submit="handleLogin">
        <ErrorAlert ref="currentErrorAlert" />
        <SuccessAlert ref="currentSuccessAlert" />
        <BaseFormField labelName="Email" type="email" id="email" name="email" placeholder="Enter your email address" v-model="email"/>
        <BaseFormField labelName="Password" type="password" id="password" name="password" placeholder="Enter your password" v-model="password"/>
        <AuthsubmitBtn text="Login" />
  </form>
</template>