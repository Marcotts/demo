import api from './api'

export const roleService = {
    async getRoles() {
        const response = await api.get('/roles')
        return response.data
    },

    async getRoleById(id) {
        const response = await api.get(`/roles/${id}`)
        return response.data
    },

    async createRole(roleData) {
        const response = await api.post('/roles', roleData)
        return response.data
    },

    async updateRole(id, roleData) {
        const response = await api.put(`/roles/${id}`, roleData)
        return response.data
    },

    async deleteRole(id) {
        const response = await api.delete(`/roles/${id}`)
        return response.data
    }
}