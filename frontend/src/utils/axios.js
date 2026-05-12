import axios from 'axios';
import { useAuthStore } from '@/stores/authStore.js'
import { useErrorHandlingStore } from '@/stores/errorHandlingStore'
import router from '@/router/router.js'

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

apiClient.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore()
        let atmAuthToken = authStore.atmAuthToken

        if (atmAuthToken) {
            config.headers.Authorization = `Bearer ${atmAuthToken}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

apiClient.interceptors.response.use(
    response => {
        return response
    },
    error => {
        const authStore = useAuthStore()
        const errorHandlingStore = useErrorHandlingStore()

        if (error.response && error.response.status === 401 && error.response.data && error.response.data.errorType == "INVALID_AUTH_TOKEN") {
            authStore.setAtmAuthToken(null)
            errorHandlingStore.setErrorMessage('You must login again for the following reason: ' + error.response.data.details)
            router.push('/atm/login')
        }

        return Promise.reject(error);
    }
);

export default apiClient;