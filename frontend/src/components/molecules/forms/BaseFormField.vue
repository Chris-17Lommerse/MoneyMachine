<script setup>
    import { computed } from 'vue'
    import BaseInput from '@/components/atoms/forms/BaseInput.vue'
    import FormLabel from '@/components/atoms/forms/FormLabel.vue'

    const props = defineProps({
        labelName: {
            type: String
        },
        type: {
            type: String,
            default: 'text'
        },
        step: {
            type: String,
        },
        id: {
            type: String,
        },
        placeholder: {
            type: String,
        },
        modelValue: {
            type: String,
        },
        isRequired: {
            type: Boolean,
            default: true
        }
    })

    const emit = defineEmits(['update:modelValue'])

    const value = computed({
        get: () => props.modelValue,
        set: (val) => emit('update:modelValue', val)
    })
</script>

<template>
    <div class="mb-3">
        <FormLabel :labelName="props.labelName" :id="props.id" />
        <BaseInput v-model="value" :type="props.type" :min="props.min" :step="props.step" :id="props.id" :placeholder="props.placeholder" :isRequired="isRequired" />
    </div>
</template>