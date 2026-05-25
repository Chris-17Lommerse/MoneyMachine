<script setup>
    import { onMounted, ref, watch } from 'vue'
    import { useErrorHandlingStore } from "@/stores/errorHandlingStore.js"

    const errorHandlingStore = useErrorHandlingStore();

    const displayer = ref('d-none')
    const errorMessage = ref('')
    let stopWatchingError;

    function shutdown(){
        if (stopWatchingError !== undefined) {
            stopWatchingError()
        }
    }

    function displayErrorMessage(){
        displayer.value = ''
        errorMessage.value = errorHandlingStore.errorMessage
        errorHandlingStore.errorMessage = null
    }

    function handleDismissClick(){
        displayer.value = 'd-none'
    }

    onMounted(() => {

        if (errorHandlingStore.errorMessage) {
            displayErrorMessage()
        }

        stopWatchingError = watch(
            () => errorHandlingStore.errorMessage,
            (newValue) => {
                if (!newValue) {
                    return
                }
                
                displayErrorMessage()
            }
        )
    })

    defineExpose({
        shutdown
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
