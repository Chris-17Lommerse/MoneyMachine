<script setup>
    const props = defineProps({
        buttonText: {
            type: String,
        }
    })
</script>

<template>
    <div class="mb-3">
        <button type="submit" class="btn btn-primary w-100">
            {{ props.buttonText }}
        </button>
    </div>
</template>
