import { defineStore } from 'pinia'
import { jwtDecode } from 'jwt-decode'
import { ref } from 'vue'
import axios from '@/utils/axios.js'

const localStorageAuthKey = 'money_machine_auth_token'

export const useAuthStore = defineStore('auth', () => {

    let authToken = ref(getAuthTokenFromLocalStorage())
    let decodedAuthToken = ref(getDecodedAuthToken())

    function getAuthTokenFromLocalStorage(){

        const localStorageAuthToken = localStorage.getItem(localStorageAuthKey)

        if (localStorageAuthToken){
            return localStorageAuthToken
        }

        return null
    }
    
    function setAuthToken(token) {

        authToken.value = token;
        decodedAuthToken.value = getDecodedAuthToken()

        if (token) {
            localStorage.setItem(localStorageAuthKey, token)
        } 
        else {
            localStorage.removeItem(localStorageAuthKey)
        }
    }

    function getDecodedAuthToken() {

        if (authToken.value !== null) {
            return jwtDecode(authToken.value)
        }
        
        return null
    }

    return {authToken, decodedAuthToken, setAuthToken}
})