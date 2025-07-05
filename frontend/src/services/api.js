import axios from 'axios'

const api = axios.create({
    baseURL: process.env.VUE_APP_API_URL || '/api',  // ✅ Utilise les variables d'environnement
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Intercepteur pour gérer les erreurs
api.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error('API Error:', error.response?.data || error.message)
        return Promise.reject(error)
    }
)

export default api