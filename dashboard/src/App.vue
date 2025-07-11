<script setup>
import DashboardPage from './components/DashboardPage.vue'
</script>

<template>
  <div class="app">
    <aside class="sidebar">
      <h2>🧭 Menu</h2>
      <ul>
        <li>Dashboard</li>
        <li>Users</li>
        <li>Settings</li>
      </ul>
    </aside>

    <main class="content">
      <DashboardPage />
    </main>
  </div>
</template>

<style scoped>
.app {
  display: flex;
  height: 100vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.sidebar {
  width: 220px;
  background-color: #2c3e50;
  color: white;
  padding: 20px;
}

.sidebar h2 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  margin: 10px 0;
  cursor: pointer;
  transition: color 0.3s;
}

.sidebar li:hover {
  color: #00b894;
}

.content {
  flex: 1;
  padding: 30px;
  background-color: #f8f9fa;
}
</style>
