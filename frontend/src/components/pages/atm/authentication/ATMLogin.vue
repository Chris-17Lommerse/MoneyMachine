<script setup>
    import ATMLoginForm from '@/components/organisms/forms/authentication/ATMLoginForm.vue';
</script>

<template>
    <section class="container d-flex justify-content-center align-items-center vh-100">
        <div class="card shadow-sm authentication-box">
            <div class="card-body">
                <h3 class="card-title text-center mb-4">ATM Login</h3>
                <ATMLoginForm />
            </div>
        </div>
    </section>
</template>