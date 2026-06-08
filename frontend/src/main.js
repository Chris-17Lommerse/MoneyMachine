import { createApp } from 'vue'
import router from './router/router'
import './style.css'
import App from './App.vue'
import { createPinia } from 'pinia'

const app = createApp(App);
app.use(createPinia())
app.use(router);
app.mount('#app');
console.log("API URL:" + import.meta.env.VITE_API_URL);
