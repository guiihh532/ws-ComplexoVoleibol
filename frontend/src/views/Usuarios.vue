<template>
  <div class="usuarios-page-container">
    <div class="header">
      <h2>Lista de Clientes</h2>
      <router-link to="/cadastro" class="cadastro-btn">
        Cadastrar Novo
      </router-link>
    </div>
    
    <div class="usuarios-list-container">
      <p v-if="loading">Carregando usuários...</p>
      <p v-else-if="error" class="error-message">Erro ao carregar os dados. Por favor, tente novamente mais tarde.</p>
      <p v-else-if="clientes.length === 0" class="no-data-message">Nenhum cliente cadastrado ainda.</p>
      
      <div v-else class="clientes-grid">
        <div v-for="cliente in clientes" :key="cliente.id" class="cliente-card">
          <div class="card-content">
            <h3>{{ cliente.nome }}</h3>
            <p><strong>Email:</strong> {{ cliente.email }}</p>
            <p><strong>CPF/CNPJ:</strong> {{ cliente.cpfCnpj }}</p>
            <p><strong>Empresa:</strong> {{ cliente.empresa }}</p>
            <p><strong>Nível de Acesso:</strong> {{ formatarNivel(cliente.nivelAcesso) }}</p>
          </div>
          <div class="card-actions">
            <router-link :to="{ path: `/cadastro/${cliente.id}` }" class="edit-icon-btn">
              <i class="fas fa-pencil-alt"></i> 
            </router-link>
            <button @click="abrirModalConfirmacao(cliente)" class="delete-icon-btn">
              <i class="fas fa-trash-alt"></i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal-content">
        <h4>Confirmação de Exclusão</h4>
        <p>Para confirmar a exclusão do usuário <strong>"{{ clienteParaExcluir.nome }}"</strong>, digite o nome dele abaixo:</p>
        <input type="text" v-model="nomeConfirmacao" placeholder="Digite o nome completo" class="confirmation-input">
        <div class="modal-buttons">
          <button @click="fecharModalConfirmacao" class="modal-cancel-btn">Cancelar</button>
          <button @click="deletarCliente" :disabled="nomeConfirmacao !== clienteParaExcluir.nome" class="modal-confirm-btn">
            Confirmar Exclusão
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UsuariosPage',
  data() {
    return {
      clientes: [],
      loading: true,
      error: false,
      showDeleteModal: false,
      clienteParaExcluir: null,
      nomeConfirmacao: '',
    };
  },
  methods: {
    async fetchClientes() {
      try {
        const response = await axios.get('http://localhost:8080/api/clientes');
        this.clientes = response.data;
        this.loading = false;
      } catch (e) {
        console.error("Erro ao buscar clientes:", e);
        this.loading = false;
        this.error = true;
      }
    },
    abrirModalConfirmacao(cliente) {
      this.clienteParaExcluir = cliente;
      this.showDeleteModal = true;
    },
    fecharModalConfirmacao() {
      this.showDeleteModal = false;
      this.clienteParaExcluir = null;
      this.nomeConfirmacao = '';
    },
    async deletarCliente() {
      if (this.nomeConfirmacao === this.clienteParaExcluir.nome) {
        try {
          // A URL de exclusão agora usa um ID numérico
          await axios.delete(`http://localhost:8080/api/clientes/${this.clienteParaExcluir.id}`);
          this.clientes = this.clientes.filter(cliente => cliente.id !== this.clienteParaExcluir.id);
          console.log("Cliente excluído com sucesso!");
          this.fecharModalConfirmacao();
        } catch (e) {
          console.error("Erro ao excluir cliente:", e);
          alert("Erro ao excluir cliente. Por favor, tente novamente.");
          this.fecharModalConfirmacao();
        }
      } else {
        alert("O nome digitado não corresponde. A exclusão foi cancelada.");
      }
    },
    formatarNivel(nivel) {
      if (!nivel) return 'Não Definido';
      
      const niveis = {
        ADMINISTRADOR_SISTEMA: 'Administrador do Sistema',
        GESTOR_LOCAL: 'Gestor Local',
        AUXILIADOR_GESTAO: 'Auxiliador de Gestão'
      };
      
      return niveis[nivel] || nivel;
    }
  },
  created() {
    this.fetchClientes();
  }
};
</script>

<style scoped>
/* (O CSS não foi alterado, pode manter o que você já tinha) */

.usuarios-page-container {
  padding: 2rem;
  background-color: #f0f2f5;
  min-height: 80vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.header h2 {
  font-size: 2rem;
  color: #333;
}

.cadastro-btn {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.cadastro-btn:hover {
  background-color: #358a66;
}

.usuarios-list-container {
  display: flex;
  flex-direction: column;
}

.clientes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.cliente-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.cliente-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.card-content h3 {
  margin-top: 0;
  color: #2c3e50;
}

.card-content p {
  margin: 5px 0;
  color: #555;
}

.card-actions {
  margin-top: 15px;
  text-align: right;
}

.edit-icon-btn, .delete-icon-btn {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  text-decoration: none;
  font-size: 16px;
  transition: transform 0.2s;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  border: none;
  cursor: pointer;
}

.edit-icon-btn {
  background-color: #007bff;
  color: white;
  margin-right: 8px;
}

.edit-icon-btn:hover {
  background-color: #0056b3;
  transform: scale(1.1);
}

.delete-icon-btn {
  background-color: #dc3545;
  color: white;
}

.delete-icon-btn:hover {
  background-color: #c82333;
  transform: scale(1.1);
}

.error-message {
  color: #f44336;
  text-align: center;
  font-weight: bold;
}

.no-data-message {
  color: #777;
  text-align: center;
  font-style: italic;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  width: 90%;
  max-width: 400px;
  text-align: center;
  transform: scale(0.95);
  animation: scaleIn 0.2s forwards;
}

@keyframes scaleIn {
  to { transform: scale(1); }
}

.modal-content h4 {
  margin-top: 0;
  font-size: 1.5rem;
  color: #333;
}

.modal-content p {
  margin: 15px 0;
  color: #555;
  line-height: 1.4;
}

.confirmation-input {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.modal-confirm-btn, .modal-cancel-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s, opacity 0.2s;
}

.modal-cancel-btn {
  background-color: #ccc;
  color: #333;
}

.modal-confirm-btn {
  background-color: #dc3545;
  color: white;
}

.modal-confirm-btn:disabled {
  background-color: #e0a3a8;
  cursor: not-allowed;
  opacity: 0.6;
}

</style>