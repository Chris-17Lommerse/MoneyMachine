<script setup>
    const props = defineProps({
        text: {
            type: String,
        }
    })
</script>

<template>
    <div class="mb-3">
        <button type="submit" class="btn btn-primary w-100">{{ text }}</button>
    </div>
</template>
