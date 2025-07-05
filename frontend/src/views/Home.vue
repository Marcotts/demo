<template>
  <div class="home">
    <div class="hero-section">
      <h1>Bienvenue dans l'interface d'administration</h1>
      <p>Gérez vos utilisateurs et rôles facilement</p>
    </div>

    <div class="quick-actions">
      <h2>Actions rapides</h2>
      <div class="action-cards">
        <div class="card">
          <h3>👥 Utilisateurs</h3>
          <p>Gérer les comptes utilisateurs</p>
          <router-link to="/users" class="btn btn-primary">
            Gérer les utilisateurs
          </router-link>
        </div>

        <div class="card">
          <h3>🔐 Rôles</h3>
          <p>Configurer les rôles et permissions</p>
          <router-link to="/roles" class="btn btn-primary">
            Gérer les rôles
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage'  // Changé de 'Home' à 'HomePage'
}
</script>

<style scoped>
.home {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.hero-section {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 10px;
}

.hero-section h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.hero-section p {
  font-size: 1.2rem;
  opacity: 0.9;
}

.quick-actions h2 {
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.action-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.card {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
}

.card h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
}

.card p {
  color: #7f8c8d;
  margin-bottom: 1.5rem;
}

.btn {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  text-decoration: none;
  border-radius: 5px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}
</style>