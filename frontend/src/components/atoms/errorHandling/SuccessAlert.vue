<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore.js"

    const errorHandlingStore = useErrorHandlingStore();

    const displayer = ref('d-none')
    const successMessage = ref('')

    function displaySuccessMessage(message){
        displayer.value = ''
        successMessage.value = message
    }

    function handleDismissClick(){
        displayer.value = 'd-none'
    }

    onMounted(() => {

        if (errorHandlingStore.successMessage) {
            displaySuccessMessage(errorHandlingStore.successMessage)
            errorHandlingStore.successMessage = null
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
