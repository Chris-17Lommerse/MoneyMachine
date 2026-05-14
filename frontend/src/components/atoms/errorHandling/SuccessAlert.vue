<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore.js"

    const errorHandlingStore = useErrorHandlingStore();

    const displayer = ref('d-none')
    const successMessage = ref('')

    function displaySuccessMessage(newSuccessMessage){
        displayer.value = ''
        successMessage.value = newSuccessMessage
    }

    function handleDismissClick(){
        displayer.value = 'd-none'
    }

    onMounted(() => {

        const newSuccessMessage = errorHandlingStore.getSuccessMessage()

        if (newSuccessMessage !== null) {
            displaySuccessMessage(newSuccessMessage)
        }
    })

    defineExpose({
        displaySuccessMessage
    })
</script>

<template>
    <div :class="'alert alert-success ' + displayer" role="alert">
        {{ successMessage }}
        <button @click="handleDismissClick" type="button" class="close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</template>
