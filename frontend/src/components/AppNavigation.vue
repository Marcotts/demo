<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <router-link to="/" class="brand-link">
        <h1>Administration</h1>
      </router-link>
    </div>

    <div class="navbar-menu">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link to="/" class="nav-link" exact-active-class="active">
            <i class="icon-home"></i>
            Accueil
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/users" class="nav-link" active-class="active">
            <i class="icon-users"></i>
            Utilisateurs
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/roles" class="nav-link" active-class="active">
            <i class="icon-roles"></i>
            Rôles
          </router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'AppNavigation'
}
</script>

<style scoped>
.navbar {
  background-color: #2c3e50;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.navbar-brand .brand-link {
  text-decoration: none;
  color: white;
}

.navbar-brand h1 {
  margin: 0;
  color: #ecf0f1;
  font-size: 1.5rem;
}

.navbar-nav {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 1rem;
}

.nav-item {
  margin: 0;
}

.nav-link {
  color: #bdc3c7;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.nav-link:hover {
  background-color: #34495e;
  color: #ecf0f1;
}

.nav-link.active {
  background-color: #3498db;
  color: white;
}

.icon-home::before { content: '🏠'; }
.icon-users::before { content: '👥'; }
.icon-roles::before { content: '🔐'; }
</style>