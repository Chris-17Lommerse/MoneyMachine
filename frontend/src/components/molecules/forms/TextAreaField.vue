<script setup>
    import { computed } from 'vue'
    import TextAreaInput from '@/components/atoms/forms/TextAreaInput.vue'
    import FormLabel from '@/components/atoms/forms/FormLabel.vue'

    const props = defineProps({
        labelName: {
            type: String
        },
        id: {
            type: String,
        },
        name: {
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
        <TextAreaInput v-model="value" :id="props.id" :name="props.name" :placeholder="props.placeholder" :isRequired="isRequired" />
    </div>
</template>