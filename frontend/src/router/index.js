import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomeView.vue'
import CadastroPage from '../views/Cadastro.vue'
import UsuariosPage from '../views/Usuarios.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage
  },
  {
    path: '/cadastro/:id?', // Alterado para aceitar ID opcional
    name: 'cadastro',
    component: CadastroPage
  },
  {
    path: '/usuarios',
    name: 'usuarios',
    component: UsuariosPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router