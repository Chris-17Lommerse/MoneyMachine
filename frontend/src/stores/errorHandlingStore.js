import { defineStore } from "pinia"

export const useErrorHandlingStore = defineStore('errorHandling', () => {

    let errorMessage = null
    let successMessage = null

    function getErrorMessage(){
        const value = errorMessage;
        errorMessage = null

        return value
    }

    function getSuccessMessage(){
        const value = successMessage;
        successMessage = null

        return value
    }

    function setErrorMessage(newValue){
        errorMessage = newValue
    }

    function setSuccessMessage(newValue){
        successMessage = newValue
    }

    return {getErrorMessage, getSuccessMessage, setErrorMessage, setSuccessMessage}
})