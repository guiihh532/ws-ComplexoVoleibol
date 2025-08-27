<template>
  </template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'LoginPage',
  data() {
    return {
      cpfCnpj: '',
      password: '',
      errorMessage: ''
    };
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post('http://localhost:8080/api/login', {
          cpfCnpj: this.cpfCnpj,
          senha: this.password
        });

        if (response.status === 200) {
          // Salva os dados do cliente no localStorage
          localStorage.setItem('clienteLogado', JSON.stringify(response.data));
          
          this.errorMessage = '';
          alert('Login bem-sucedido!');
          this.router.push('/usuarios');
        }
      } catch (error) {
        this.errorMessage = 'CPF ou senha inválidos. Tente novamente.';
        console.error("Erro no login:", error);
      }
    }
  }
};
</script>

<style scoped>
/* ... (Mesmo CSS) ... */
</style>