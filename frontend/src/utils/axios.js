import axios from 'axios'
import { useAuthStore } from '@/stores/authStore.js'
import { useErrorHandlingStore } from '@/stores/errorHandlingStore'
import router from '@/router/router.js'

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
})

apiClient.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore()

        const currentPath = router.currentRoute.value.path
        let authToken = null

        if (currentPath.startsWith('/atm')) {
            authToken = authStore.atmAuthToken
        }
        else{
            authToken = authStore.websiteAuthToken
        }

        if (authToken) {
            config.headers.Authorization = `Bearer ${authToken}`
        }

        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

apiClient.interceptors.response.use(
    response => {
        return response
    },
    error => {
        const authStore = useAuthStore()
        const errorHandlingStore = useErrorHandlingStore()

        if (error.response && error.response.status === 401 && error.response.data && error.response.data.errorType == "INVALID_AUTH_TOKEN") {
            
            const currentPath = router.currentRoute.value.path
            errorHandlingStore.errorMessage = 'You must login again for the following reason: ' + error.response.data.details

            if (currentPath.startsWith('/atm')) {
                authStore.setAtmAuthToken(null)
                router.push('/atm/login')
            }
            else {
                authStore.setWebsiteAuthToken(null)
                router.push('/login')
            }
        }

        return Promise.reject(error)
    }
)

export default apiClient