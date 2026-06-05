<script setup>
    import FormLabel from '@/components/atoms/forms/FormLabel.vue'
    import SubmitBtn from '@/components/atoms/buttons/SubmitBtn.vue'

    defineProps({
        vModel: {
            type: Object,
            required: true
        },
        bankAccounts: {
            type: Array,
            required: false
        }
    })
</script>

<template>
    <form>
        <div class="mb-3">
            <FormLabel id="bankAccount" labelName="Select a bank account" />
            <select v-model="vModel.selectedBankAccountIban" id="bankAccount" class="custom-select">
                <option value="" disabled>Select an account</option>
                <option v-for="bankAccount in bankAccounts" :value="bankAccount.iban">{{ bankAccount.iban }} | {{ bankAccount.bankAccountType }}</option>
            </select>
        </div>

        <SubmitBtn text="Select bank account"/>
    </form>
</template>
