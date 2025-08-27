<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link>
      <router-link to="/usuarios" :class="{ 'router-link-active': isUsuariosSectionActive }">Usuários</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    isUsuariosSectionActive() {
      // Retorna 'true' se a rota atual começar com '/usuarios' ou '/cadastro'
      return this.$route.path.startsWith('/usuarios') || this.$route.path.startsWith('/cadastro');
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

nav {
  padding: 30px;
  display: flex;
  justify-content: flex-start;
  gap: 20px;
  background-color: #e3e6e8;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
  padding: 10px 15px;
  border-radius: 5px;
  transition: background-color 0.3s;
}

nav a.router-link-exact-active {
  color: white;
  background-color: #42b983;
}

/* Adicionamos o mesmo estilo para a classe que será aplicada dinamicamente */
nav a.router-link-active {
  color: white;
  background-color: #42b983;
}
</style>