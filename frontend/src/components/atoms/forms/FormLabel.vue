<script setup>
    const props = defineProps({
        id: {
            type: String,
        },
        labelName: {
            type: String,
        },
    })
</script>

<template>
    <label :for="props.id" class="form-label">{{ props.labelName }}</label>
</template>
