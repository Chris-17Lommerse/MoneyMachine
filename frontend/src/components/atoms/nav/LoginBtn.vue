<script setup>
    import { computed } from 'vue'

    const props = defineProps({
        isAtm: {
            type: Boolean,
            default: false
        }
    })

    const to = computed(() => {
        if (props.isAtm) {
            return "/atm/login"
        }
        
        return "/login"
    })

    const text = computed(() => {
        if (props.isAtm) {
            return "Login ATM"
        }
        
        return "Login"
    })
</script>

<template>
    <RouterLink :to="to" class="btn btn-success text-light">{{ text }}</RouterLink>
</template>