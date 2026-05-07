<script setup>
    import { computed } from 'vue'
    import FormLabel from '@/components/atoms/forms/FormLabel.vue'
    import FileInput from '@/components/atoms/forms/FileInput.vue'

    const props = defineProps({
        labelName: {
            type: String
        },
        id: {
            type: String
        },
        name: {
            type: String
        },
        accept: {
            type: String
        },
        modelValue: {
            type: File
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
        <FileInput v-model="value" :id="props.id" :name="props.name" :accept="props.accept" :isRequired="isRequired" />
    </div>
</template>