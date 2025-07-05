<template>
  <div class="role-list">
    <div class="list-header">
      <h3>Liste des rôles</h3>
      <div class="search-container">
        <input
            v-model="searchTerm"
            type="text"
            placeholder="Rechercher un rôle..."
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
      <table class="roles-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Nom</th>
          <th>Description</th>
          <th>Permissions</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="role in filteredRoles" :key="role.id || role.ID">
          <td>{{ role.id || role.ID }}</td>
          <td>{{ role.name || role.NAME }}</td>
          <td>{{ (role.description || role.DESCRIPTION) || 'N/A' }}</td>
          <td>
            <div v-if="(role.permissions || role.PERMISSIONS) && (role.permissions || role.PERMISSIONS).length > 0" class="permissions">
                <span
                    v-for="permission in (role.permissions || role.PERMISSIONS)"
                    :key="permission"
                    class="permission-badge"
                >
                  {{ permission }}
                </span>
            </div>
            <span v-else class="no-permissions">Aucune permission</span>
          </td>
          <td class="actions">
            <button
                @click="editRole(role)"
                class="btn btn-sm btn-primary"
                title="Modifier"
            >
              ✏️
            </button>
            <button
                @click="deleteRole(role.id || role.ID)"
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
  name: 'RoleList',
  props: {
    roles: {
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
    filteredRoles() {
      if (!this.searchTerm) return this.roles

      return this.roles.filter(role =>
          ((role.name && role.name.toLowerCase().includes(this.searchTerm.toLowerCase())) ||
           (role.NAME && role.NAME.toLowerCase().includes(this.searchTerm.toLowerCase()))) ||
          ((role.description && role.description.toLowerCase().includes(this.searchTerm.toLowerCase())) ||
           (role.DESCRIPTION && role.DESCRIPTION.toLowerCase().includes(this.searchTerm.toLowerCase())))
      )
    }
  },
  methods: {
    editRole(role) {
      this.$emit('edit-role', role)
    },
    deleteRole(roleId) {
      if (confirm('Êtes-vous sûr de vouloir supprimer ce rôle ?')) {
        this.$emit('delete-role', roleId)
      }
    }
  }
}
</script>

<style scoped>
.role-list {
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

.roles-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.roles-table th,
.roles-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.roles-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.roles-table tr:hover {
  background-color: #f8f9fa;
}

.permissions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
}

.permission-badge {
  background-color: #3498db;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 500;
}

.no-permissions {
  color: #7f8c8d;
  font-style: italic;
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