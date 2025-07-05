<template>
  <div class="user-management">
    <div class="page-header">
      <h1>Gestion des utilisateurs</h1>
      <button @click="showAddForm" class="btn btn-primary">
        + Ajouter un utilisateur
      </button>
    </div>

    <div v-if="showForm" class="form-overlay">
      <UserForm
          :user="selectedUser"
          :available-roles="roles"
          :submitting="submitting"
          @submit-user="handleSubmitUser"
          @close-form="hideForm"
      />
    </div>

    <UserList
        :users="users"
        :loading="loading"
        :error="error"
        @edit-user="editUser"
        @delete-user="deleteUser"
    />
  </div>
</template>

<script>
import UserList from '../components/UserList.vue'
import UserForm from '../components/UserForm.vue'
import { userService } from '../services/userService'
import { roleService } from '../services/roleService'

export default {
  name: 'UserManagement',
  components: {
    UserList,
    UserForm
  },
  data() {
    return {
      users: [],
      roles: [],
      loading: true,
      error: null,
      showForm: false,
      selectedUser: null,
      submitting: false
    }
  },
  async mounted() {
    await this.loadData()
  },
  methods: {
    async loadData() {
      try {
        this.loading = true
        this.error = null

        const [usersResponse, rolesResponse] = await Promise.all([
          userService.getUsers(),
          roleService.getRoles()
        ])

        this.users = usersResponse
        this.roles = rolesResponse
      } catch (error) {
        this.error = 'Erreur lors du chargement des données'
        console.error('Error loading data:', error)
      } finally {
        this.loading = false
      }
    },

    showAddForm() {
      this.selectedUser = null
      this.showForm = true
    },

    editUser(user) {
      this.selectedUser = user
      this.showForm = true
    },

    hideForm() {
      this.showForm = false
      this.selectedUser = null
    },

    async handleSubmitUser(userData) {
      try {
        this.submitting = true

        if (this.selectedUser && this.selectedUser.id) {
          await userService.updateUser(this.selectedUser.id, userData)
        } else {
          await userService.createUser(userData)
        }

        await this.loadData()
        this.hideForm()
      } catch (error) {
        console.error('Error saving user:', error)
        alert('Erreur lors de la sauvegarde de l\'utilisateur')
      } finally {
        this.submitting = false
      }
    },

    async deleteUser(userId) {
      try {
        await userService.deleteUser(userId)
        await this.loadData()
      } catch (error) {
        console.error('Error deleting user:', error)
        alert('Erreur lors de la suppression de l\'utilisateur')
      }
    }
  }
}
</script>

<style scoped>
.user-management {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-header h1 {
  color: #2c3e50;
  margin: 0;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
</style>