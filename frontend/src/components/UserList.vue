<template>
  <div class="user-list">
    <div class="list-header">
      <h3>Liste des utilisateurs</h3>
      <div class="search-container">
        <input
            v-model="searchTerm"
            type="text"
            placeholder="Rechercher un utilisateur..."
            class="search-input"
        >
      </div>
    </div>

    <div v-if="loading" class="loading">
      Chargement...
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else class="table-container">
      <table class="users-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Nom</th>
          <th>Email</th>
          <th>Rôle</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in filteredUsers" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name || user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role || 'N/A' }}</td>
          <td class="actions">
            <button
                @click="editUser(user)"
                class="btn btn-sm btn-primary"
                title="Modifier"
            >
              ✏️
            </button>
            <button
                @click="deleteUser(user.id)"
                class="btn btn-sm btn-danger"
                title="Supprimer"
            >
              🗑️
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserList',
  props: {
    users: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    },
    error: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      searchTerm: ''
    }
  },
  computed: {
    filteredUsers() {
      if (!this.searchTerm) return this.users

      return this.users.filter(user =>
          (user.name && user.name.toLowerCase().includes(this.searchTerm.toLowerCase())) ||
          (user.username && user.username.toLowerCase().includes(this.searchTerm.toLowerCase())) ||
          (user.email && user.email.toLowerCase().includes(this.searchTerm.toLowerCase()))
      )
    }
  },
  methods: {
    editUser(user) {
      this.$emit('edit-user', user)
    },
    deleteUser(userId) {
      if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
        this.$emit('delete-user', userId)
      }
    }
  }
}
</script>

<style scoped>
.user-list {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.list-header h3 {
  margin: 0;
  color: #2c3e50;
}

.search-container {
  width: 300px;
}

.search-input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.search-input:focus {
  outline: none;
  border-color: #3498db;
}

.loading, .error {
  text-align: center;
  padding: 2rem;
}

.error {
  color: #e74c3c;
}

.table-container {
  overflow-x: auto;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.users-table th,
.users-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.users-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.users-table tr:hover {
  background-color: #f8f9fa;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn {
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: all 0.3s ease;
}

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background-color: #c0392b;
}
</style>