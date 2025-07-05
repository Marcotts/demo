<template>
  <div class="role-management">
    <div class="page-header">
      <h1>Gestion des rôles</h1>
      <button @click="showAddForm" class="btn btn-primary">
        + Ajouter un rôle
      </button>
    </div>

    <div v-if="showForm" class="form-overlay">
      <RoleForm
          :role="selectedRole"
          :submitting="submitting"
          @submit-role="handleSubmitRole"
          @close-form="hideForm"
      />
    </div>

    <RoleList
        :roles="roles"
        :loading="loading"
        :error="error"
        @edit-role="editRole"
        @delete-role="deleteRole"
    />
  </div>
</template>

<script>
import RoleList from '../components/RoleList.vue'
import RoleForm from '../components/RoleForm.vue'
import { roleService } from '../services/roleService'

export default {
  name: 'RoleManagement',
  components: {
    RoleList,
    RoleForm
  },
  data() {
    return {
      roles: [],
      loading: true,
      error: null,
      showForm: false,
      selectedRole: null,
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

        this.roles = await roleService.getRoles()
      } catch (error) {
        this.error = 'Erreur lors du chargement des rôles'
        console.error('Error loading roles:', error)
      } finally {
        this.loading = false
      }
    },

    showAddForm() {
      this.selectedRole = null
      this.showForm = true
    },

    editRole(role) {
      this.selectedRole = role
      this.showForm = true
    },

    hideForm() {
      this.showForm = false
      this.selectedRole = null
    },

    async handleSubmitRole(roleData) {
      try {
        this.submitting = true

        if (this.selectedRole && this.selectedRole.id) {
          await roleService.updateRole(this.selectedRole.id, roleData)
        } else {
          await roleService.createRole(roleData)
        }

        await this.loadData()
        this.hideForm()
      } catch (error) {
        console.error('Error saving role:', error)
        alert('Erreur lors de la sauvegarde du rôle')
      } finally {
        this.submitting = false
      }
    },

    async deleteRole(roleId) {
      try {
        await roleService.deleteRole(roleId)
        await this.loadData()
      } catch (error) {
        console.error('Error deleting role:', error)
        alert('Erreur lors de la suppression du rôle')
      }
    }
  }
}
</script>

<style scoped>
.role-management {
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