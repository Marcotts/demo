<template>
  <div class="user-form">
    <div class="form-header">
      <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} un utilisateur</h3>
      <button @click="closeForm" class="btn-close">×</button>
    </div>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="username">Nom d'utilisateur *</label>
        <input
            id="username"
            v-model="formData.username"
            type="text"
            required
            :class="{ 'error': errors.username }"
        >
        <span v-if="errors.username" class="error-message">{{ errors.username }}</span>
      </div>

      <div class="form-group">
        <label for="email">Email *</label>
        <input
            id="email"
            v-model="formData.email"
            type="email"
            required
            :class="{ 'error': errors.email }"
        >
        <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
      </div>

      <div class="form-group">
        <label for="firstName">Prénom</label>
        <input
            id="firstName"
            v-model="formData.firstName"
            type="text"
        >
      </div>

      <div class="form-group">
        <label for="lastName">Nom</label>
        <input
            id="lastName"
            v-model="formData.lastName"
            type="text"
        >
      </div>

      <div class="form-group">
        <label for="role">Rôle</label>
        <select id="role" v-model="formData.role">
          <option value="">Sélectionner un rôle</option>
          <option v-for="role in availableRoles" :key="role.id" :value="role.name">
            {{ role.name }}
          </option>
        </select>
      </div>

      <div v-if="!isEditing" class="form-group">
        <label for="password">Mot de passe *</label>
        <input
            id="password"
            v-model="formData.password"
            type="password"
            :required="!isEditing"
            :class="{ 'error': errors.password }"
        >
        <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
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
  name: 'UserForm',
  props: {
    user: {
      type: Object,
      default: null
    },
    availableRoles: {
      type: Array,
      default: () => []
    },
    submitting: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      formData: {
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        role: '',
        password: ''
      },
      errors: {}
    }
  },
  computed: {
    isEditing() {
      return this.user && this.user.id
    }
  },
  watch: {
    user: {
      handler(newUser) {
        if (newUser) {
          this.formData = { ...newUser }
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
        const userData = { ...this.formData }
        if (this.isEditing) {
          delete userData.password
        }

        this.$emit('submit-user', userData)
      }
    },

    validateForm() {
      let isValid = true

      if (!this.formData.username.trim()) {
        this.errors.username = 'Le nom d\'utilisateur est requis'
        isValid = false
      }

      if (!this.formData.email.trim()) {
        this.errors.email = 'L\'email est requis'
        isValid = false
      } else if (!this.isValidEmail(this.formData.email)) {
        this.errors.email = 'L\'email n\'est pas valide'
        isValid = false
      }

      if (!this.isEditing && !this.formData.password.trim()) {
        this.errors.password = 'Le mot de passe est requis'
        isValid = false
      }

      return isValid
    },

    isValidEmail(email) {
      const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return re.test(email)
    },

    resetForm() {
      this.formData = {
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        role: '',
        password: ''
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
.user-form {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  max-width: 500px;
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
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #3498db;
}

.form-group input.error,
.form-group select.error {
  border-color: #e74c3c;
}

.error-message {
  color: #e74c3c;
  font-size: 0.8rem;
  margin-top: 0.25rem;
  display: block;
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