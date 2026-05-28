<script setup>
    import ATMActionOptionBtn from '@/components/atoms/buttons/ATMActionOptionBtn.vue'

    const props = defineProps({
        iban: {
            type: String,
            required: true
        },
    })
</script>

<template>
    <div class="mt-2">
        <ATMActionOptionBtn :to="'/atm/withdraw/' + props.iban" text="Withdraw" />
        <ATMActionOptionBtn :to="'/atm/deposit/' + props.iban" text="Deposit" />
    </div>
</template>