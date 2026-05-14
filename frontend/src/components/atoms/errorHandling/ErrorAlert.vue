<script setup>
    import { onMounted, ref } from 'vue'
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore.js"

    const errorHandlingStore = useErrorHandlingStore();

    const displayer = ref('d-none')
    const errorMessage = ref('')

    function displayErrorMessage(newErrorMessage){
        displayer.value = ''
        errorMessage.value = newErrorMessage
    }

    function handleDismissClick(){
        displayer.value = 'd-none'
    }

    onMounted(() => {

        const newErrorMessage = errorHandlingStore.getErrorMessage()

        if (newErrorMessage !== null) {
            displayErrorMessage(newErrorMessage)
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
