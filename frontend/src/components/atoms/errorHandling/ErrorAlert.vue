<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore.js"

    const errorHandlingStore = useErrorHandlingStore();

    const displayer = ref('d-none')
    const errorMessage = ref('')

    function displayErrorMessage(message){
        displayer.value = ''
        errorMessage.value = message
    }

    function handleDismissClick(){
        displayer.value = 'd-none'
    }

    onMounted(() => {

        if (errorHandlingStore.errorMessage) {
            displayErrorMessage(errorHandlingStore.errorMessage)
            errorHandlingStore.errorMessage = null
        }
    })

    defineExpose({
        displayErrorMessage
    })
</script>

<template>
    <div :class="'alert alert-danger ' + displayer" role="alert">
        An error has occurred: {{ errorMessage }}
        <button @click="handleDismissClick" type="button" class="close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</template>
