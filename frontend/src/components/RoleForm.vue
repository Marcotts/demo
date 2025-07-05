<template>
  <div class="role-form">
    <div class="form-header">
      <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} un rôle</h3>
      <button @click="closeForm" class="btn-close">×</button>
    </div>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Nom du rôle *</label>
        <input
            id="name"
            v-model="formData.name"
            type="text"
            required
            :class="{ 'error': errors.name }"
        >
        <span v-if="errors.name" class="error-message">{{ errors.name }}</span>
      </div>

      <div class="form-group">
        <label for="description">Description</label>
        <textarea
            id="description"
            v-model="formData.description"
            rows="3"
            placeholder="Description du rôle..."
        ></textarea>
      </div>

      <div class="form-group">
        <label>Permissions</label>
        <div class="permissions-grid">
          <div
              v-for="permission in availablePermissions"
              :key="permission"
              class="permission-item"
          >
            <input
                :id="permission"
                type="checkbox"
                :value="permission"
                v-model="formData.permissions"
            >
            <label :for="permission">{{ permission }}</label>
          </div>
        </div>
      </div>

      <div class="form-actions">
        <button type="button" @click="closeForm" class="btn btn-secondary">
          Annuler
        </button>
        <button type="submit" class="btn btn-primary" :disabled="submitting">
          {{ submitting ? 'Enregistrement...' : (isEditing ? 'Modifier' : 'Ajouter') }}
        </button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: 'RoleForm',
  props: {
    role: {
      type: Object,
      default: null
    },
    submitting: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      formData: {
        name: '',
        description: '',
        permissions: []
      },
      errors: {},
      availablePermissions: [
        'READ_USERS',
        'WRITE_USERS',
        'DELETE_USERS',
        'READ_ROLES',
        'WRITE_ROLES',
        'DELETE_ROLES',
        'ADMIN_ACCESS',
        'SYSTEM_CONFIG'
      ]
    }
  },
  computed: {
    isEditing() {
      return this.role && this.role.id
    }
  },
  watch: {
    role: {
      handler(newRole) {
        if (newRole) {
          this.formData = {
            ...newRole,
            permissions: newRole.permissions || []
          }
        } else {
          this.resetForm()
        }
      },
      immediate: true
    }
  },
  methods: {
    submitForm() {
      this.errors = {}

      if (this.validateForm()) {
        this.$emit('submit-role', { ...this.formData })
      }
    },

    validateForm() {
      let isValid = true

      if (!this.formData.name.trim()) {
        this.errors.name = 'Le nom du rôle est requis'
        isValid = false
      }

      return isValid
    },

    resetForm() {
      this.formData = {
        name: '',
        description: '',
        permissions: []
      }
      this.errors = {}
    },

    closeForm() {
      this.$emit('close-form')
    }
  }
}
</script>

<style scoped>
.role-form {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  max-width: 600px;
  margin: 0 auto;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.form-header h3 {
  margin: 0;
  color: #2c3e50;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #7f8c8d;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close:hover {
  background-color: #f8f9fa;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #3498db;
}

.form-group input.error {
  border-color: #e74c3c;
}

.error-message {
  color: #e74c3c;
  font-size: 0.8rem;
  margin-top: 0.25rem;
  display: block;
}

.permissions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.permission-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.permission-item:hover {
  background-color: #f8f9fa;
}

.permission-item input[type="checkbox"] {
  width: auto;
  margin: 0;
}

.permission-item label {
  margin: 0;
  font-weight: normal;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1.5rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #2980b9;
}

.btn-primary:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
}
</style>