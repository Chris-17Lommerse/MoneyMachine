<script setup>
    import AuthsubmitBtn from '@/components/atoms/buttons/AuthSubmitBtn.vue'
    import BaseFormField from '@/components/molecules/forms/BaseFormField.vue'

    defineProps({
        vModel: {
            type: Object,
            required: true
        }
    });
</script>

<template>
    <form>    
        <BaseFormField labelName="Email" type="email" id="email" placeholder="Enter your email address" v-model="vModel.email" />
        <BaseFormField labelName="Password" type="password" id="password" placeholder="Enter your password" v-model="vModel.password" />
        <AuthsubmitBtn text="Login" />
  </form>
</template>