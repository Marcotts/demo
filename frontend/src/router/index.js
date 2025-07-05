import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import UserManagement from '../views/UserManagement.vue'
import RoleManagement from '../views/RoleManagement.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/users',
        name: 'UserManagement',
        component: UserManagement
    },
    {
        path: '/roles',
        name: 'RoleManagement',
        component: RoleManagement
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router