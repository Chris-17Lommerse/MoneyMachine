import { defineStore } from 'pinia'
import { jwtDecode } from 'jwt-decode'
import { ref } from 'vue'
import axios from '@/utils/axios.js'

const localStorageAtmAuthTokenKey = 'money_machine_atm_auth_token'

export const useAuthStore = defineStore('auth', () => {

    let atmAuthToken = ref(getAuthTokenFromLocalStorage(localStorageAtmAuthTokenKey))
    let atmDecodedAuthToken = ref(getDecodedAuthToken(atmAuthToken.value))

    function getAuthTokenFromLocalStorage(localStorageAuthTokenKey){

        const localStorageAuthToken = localStorage.getItem(localStorageAuthTokenKey)

        if (localStorageAuthToken){
            return localStorageAuthToken
        }

        return null
    }
    
    function setAuthTokenByVariables(authToken, authTokenVariable, decodedAuthTokenVariable, localStorageAuthTokenKey) {
        
        authTokenVariable.value = authToken;
        decodedAuthTokenVariable.value = getDecodedAuthToken(authToken)

        if (authToken) {
            localStorage.setItem(localStorageAuthTokenKey, authToken)
        } 
        else {
            localStorage.removeItem(localStorageAuthTokenKey)
        }
    }

    function setAtmAuthToken(authToken) {
        setAuthTokenByVariables(authToken, atmAuthToken, atmDecodedAuthToken, localStorageAtmAuthTokenKey)
    }

    function getDecodedAuthToken(authToken) {

        if (authToken !== null) {
            return jwtDecode(authToken)
        }
        
        return null
    }

    return {atmAuthToken, atmDecodedAuthToken, setAtmAuthToken}
})