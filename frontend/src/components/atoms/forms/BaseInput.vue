<script setup>
    const props = defineProps({
        type: {
            type: String,
            default: 'text'
        },
        id: {
            type: String,
        },
        min: {
            type: String,
        },
        step: {
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

    function onBaseInputChange(e){
        emit('update:modelValue', e.target.value)
    }
</script>

<template>
    <input class="form-control" :type="props.type" :min="props.min" :step="props.step" :id="props.id" :placeholder="props.placeholder" :value="props.modelValue" @change="onBaseInputChange" :required="props.isRequired"/>
</template>