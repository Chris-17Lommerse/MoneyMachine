<script setup>
const props = defineProps({
    user: {
        type: Object,
        required: true
    }
})
</script>

<template>
    <td>
        <h4>{{ user.bsn }}</h4>
    </td>
</template>