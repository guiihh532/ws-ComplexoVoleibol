<template>
  <div class="cadastro-container">
    <div class="form-card">
      <h2>{{ isEditMode ? 'Editar Usuário' : 'Cadastro de Usuário' }}</h2>
      <form @submit.prevent="saveClient">
        <div class="form-group">
          <label for="nome">Nome:</label>
          <input type="text" id="nome" v-model="form.nome" required>
        </div>
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" id="email" v-model="form.email" required>
        </div>
        <div class="form-group">
          <label for="cpfCnpj">CPF/CNPJ:</label>
          <input type="text" id="cpfCnpj" v-model="form.cpfCnpj" @input="formatCpfCnpj" required>
        </div>
        <div class="form-group">
          <label for="endereco">Endereço:</label>
          <input type="text" id="endereco" v-model="form.endereco">
        </div>
        <div class="form-group">
          <label for="cep">CEP:</label>
          <input type="text" id="cep" v-model="form.cep" @input="formatCep" required>
        </div>
        <div class="form-group">
          <label for="foto">Foto:</label>
          <input type="file" id="foto" @change="onFileChange">
        </div>

        <div class="compact-fields-row">
          <div class="form-group compact">
            <label for="valorMensalidade">Valor Mensalidade:</label>
            <div class="currency-input-container">
              <input type="number" id="valorMensalidade" v-model="form.valorMensalidade" min="100" max="1000" required>
              <span class="currency-symbol">R$</span>
            </div>
          </div>
          <div class="form-group compact">
            <label for="dataVencimento">Data de Vencimento:</label>
            <input type="date" id="dataVencimento" v-model="form.dataVencimento" required>
          </div>
          <div class="form-group compact">
            <label for="estadoPagamento">Estado do Pagamento:</label>
            <select id="estadoPagamento" v-model="form.estadoPagamento">
              <option value="pago">Pago</option>
              <option value="emDebito">Em Débito</option>
              <option value="vencido">Vencido</option>
            </select>
          </div>
        </div>
        
        <div class="form-group">
          <label for="nivelAcesso">Nível de Acesso:</label>
          <select id="nivelAcesso" v-model="form.nivelAcesso" required>
            <option value="ADMINISTRADOR_SISTEMA">Administrador do Sistema</option>
            <option value="GESTOR_LOCAL">Gestor Local</option>
            <option value="AUXILIADOR_GESTAO">Auxiliador de Gestão</option>
          </select>
        </div>

        <div class="form-group">
          <label for="empresa">Empresa:</label>
          <input type="text" id="empresa" v-model="form.empresa">
        </div>

        <div class="form-actions">
          <button 
            v-if="canChangePassword" 
            type="button" 
            @click="showPasswordModal = true" 
            class="password-change-btn"
          >
            Alterar Senha
          </button>
          <div class="right-buttons">
            <button type="button" @click="resetForm" class="reset-btn">Zerar</button>
            <button type="submit" class="submit-btn">{{ isEditMode ? 'Salvar Alterações' : 'Salvar' }}</button>
          </div>
        </div>
      </form>
      
      <div v-if="successMessage" class="popup">
        <p>{{ successMessage }}</p>
      </div>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>

    <div v-if="showPasswordModal" class="modal-overlay">
      <div class="modal-content">
        <h4>Alterar Senha</h4>
        <p>Digite a nova senha para o usuário.</p>
        <input type="password" v-model="newPassword" placeholder="Nova Senha" class="password-input">
        <input type="password" v-model="confirmPassword" placeholder="Confirmar Senha" class="password-input">
        
        <div class="modal-buttons">
          <button @click="showPasswordModal = false" class="modal-cancel-btn">Cancelar</button>
          <button @click="updatePassword" :disabled="!newPassword || newPassword !== confirmPassword" class="modal-confirm-btn">
            Confirmar
          </button>
        </div>
        <p v-if="passwordErrorMessage" class="error-message">{{ passwordErrorMessage }}</p>
        <p v-if="passwordSuccessMessage" class="success-message">{{ passwordSuccessMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'CadastroPage',
  data() {
    return {
      isEditMode: false,
      clientId: null,
      clienteLogado: {}, // Armazena os dados do usuário logado
      form: {
        nome: '',
        email: '',
        cpfCnpj: '',
        endereco: '',
        cep: '',
        foto: null,
        valorMensalidade: 100,
        dataVencimento: '',
        estadoPagamento: 'emDebito',
        nivelAcesso: 'AUXILIADOR_GESTAO',
        empresa: ''
      },
      successMessage: '',
      errorMessage: '',
      showPasswordModal: false,
      newPassword: '',
      confirmPassword: '',
      passwordErrorMessage: '',
      passwordSuccessMessage: ''
    };
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  computed: {
    canChangePassword() {
        if (!this.clienteLogado) return false;
        
        const isSelf = this.clientId && String(this.clientId) === String(this.clienteLogado.id);
        const isAdmin = this.clienteLogado.nivelAcesso === 'ADMINISTRADOR_SISTEMA';

        return this.isEditMode && (isAdmin || isSelf);
    }
  },
  async created() {
    const routeId = this.$route.params.id;
    if (routeId) {
      this.isEditMode = true;
      this.clientId = routeId;
      await this.fetchClientData();
    }
    const clienteLogadoString = localStorage.getItem('clienteLogado');
    if (clienteLogadoString) {
        this.clienteLogado = JSON.parse(clienteLogadoString);
    }
  },
  methods: {
    async fetchClientData() {
      try {
        const response = await axios.get(`http://localhost:8080/api/clientes/${this.clientId}`);
        const cliente = response.data;
        
        this.form.nome = cliente.nome;
        this.form.email = cliente.email;
        this.form.cpfCnpj = cliente.cpfCnpj;
        this.form.endereco = cliente.endereco;
        this.form.cep = cliente.cep;
        this.form.valorMensalidade = cliente.valorMensalidade;
        
        if (cliente.dataVencimento) {
          const date = new Date(cliente.dataVencimento);
          const year = date.getFullYear();
          const month = (date.getMonth() + 1).toString().padStart(2, '0');
          const day = date.getDate().toString().padStart(2, '0');
          this.form.dataVencimento = `${year}-${month}-${day}`;
        }
        
        this.form.estadoPagamento = cliente.estadoPagamento;
        this.form.nivelAcesso = cliente.nivelAcesso;
        this.form.empresa = cliente.empresa;
      } catch (error) {
        console.error("Erro ao buscar dados do cliente:", error);
        this.errorMessage = 'Erro ao carregar dados do cliente.';
      }
    },
    async saveClient() {
      try {
        let response;
        if (this.isEditMode) {
          response = await axios.put(`http://localhost:8080/api/clientes/${this.clientId}`, this.form);
          this.successMessage = 'Usuário atualizado com sucesso!';
        } else {
          response = await axios.post('http://localhost:8080/api/clientes', this.form);
          this.successMessage = 'Usuário cadastrado com sucesso!';
        }
        
        this.errorMessage = '';
        
        if (response.status === 201 || response.status === 200) {
          setTimeout(() => {
            this.successMessage = '';
            this.router.push('/usuarios');
          }, 2000);
        }
      } catch (error) {
        if (error.response && error.response.status === 409) {
          this.errorMessage = 'Erro: CPF/CNPJ já cadastrado.';
          this.successMessage = '';
        } else {
          this.errorMessage = 'Erro ao salvar cliente.';
          this.successMessage = '';
        }
        console.error("Erro na requisição:", error);
      }
    },
    async updatePassword() {
      if (this.newPassword !== this.confirmPassword) {
        this.passwordErrorMessage = 'As senhas não coincidem!';
        return;
      }
      
      try {
        const payload = {
            senha: this.newPassword,
            nivelAcesso: this.clienteLogado.nivelAcesso,
            idUsuarioLogado: this.clienteLogado.id
        };

        const response = await axios.put(`http://localhost:8080/api/clientes/${this.clientId}/senha`, payload);
        this.passwordSuccessMessage = response.data;
        this.passwordErrorMessage = '';
        this.newPassword = '';
        this.confirmPassword = '';
        
        setTimeout(() => {
            this.showPasswordModal = false;
            this.passwordSuccessMessage = '';
        }, 2000);

      } catch (error) {
        this.passwordErrorMessage = 'Erro ao atualizar a senha. Acesso negado.';
        this.passwordSuccessMessage = '';
        console.error("Erro ao atualizar a senha:", error.response.data);
      }
    },
    resetForm() {
      this.form.nome = '';
      this.form.email = '';
      this.form.cpfCnpj = '';
      this.form.endereco = '';
      this.form.cep = '';
      this.form.foto = null;
      this.form.valorMensalidade = 100;
      this.form.dataVencimento = '';
      this.form.estadoPagamento = 'emDebito';
      this.form.nivelAcesso = 'AUXILIADOR_GESTAO';
      this.form.empresa = '';
    },
    onFileChange(e) {
      this.form.foto = e.target.files[0];
    },
    formatCpfCnpj() {
      let value = this.form.cpfCnpj.replace(/\D/g, '');
      if (value.length <= 11) {
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
      } else {
        value = value.replace(/^(\d{2})(\d)/, '$1.$2');
        value = value.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
        value = value.replace(/\.(\d{3})(\d)/, '.$1/$2');
        value = value.replace(/(\d{4})(\d)/, '$1-$2');
      }
      this.form.cpfCnpj = value;
    },
    formatCep() {
      let value = this.form.cep.replace(/\D/g, '');
      value = value.replace(/^(\d{5})(\d)/, '$1-$2');
      this.form.cep = value;
    }
  }
}
</script>

<style scoped>
.cadastro-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  background-color: #f0f2f5;
  padding: 20px;
}

.form-card {
  background-color: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
  text-align: left;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #555;
}

input[type="text"], 
input[type="email"], 
input[type="number"], 
input[type="date"], 
select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
  font-size: 16px;
  transition: border-color 0.3s;
}

input:focus, select:focus {
  border-color: #42b983;
  outline: none;
}

.compact-fields-row {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-top: -10px;
  margin-bottom: 20px;
}

.form-group.compact {
  flex: 1;
}

.form-actions {
  display: flex;
  justify-content: space-between; /* Alinha os botões em lados opostos */
  align-items: center;
  margin-top: 30px;
}

.right-buttons {
  display: flex;
  gap: 15px;
}

.submit-btn, .reset-btn {
  padding: 12px 25px;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.submit-btn {
  background-color: #42b983;
}

.submit-btn:hover {
  background-color: #358a66;
}

.reset-btn {
  background-color: #f44336;
}

.reset-btn:hover {
  background-color: #d32f2f;
}

.password-change-btn {
  padding: 8px 15px; /* Tamanho menor */
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px; /* Fonte menor */
  transition: background-color 0.3s;
}

.password-change-btn:hover {
  background-color: #0056b3;
}

.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #42b983;
  color: white;
  padding: 20px 40px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  text-align: center;
  animation: fadeIn 0.5s;
}

.error-message {
  margin-top: 20px;
  color: #f44336;
  font-weight: bold;
  text-align: center;
}

.success-message {
  margin-top: 20px;
  color: #42b983;
  font-weight: bold;
  text-align: center;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translate(-50%, -60%); }
  to { opacity: 1; transform: translate(-50%, -50%); }
}

.currency-input-container {
  position: relative;
}

.currency-input-container input {
  padding-right: 45px;
}

.currency-symbol {
  position: absolute;
  top: 50%;
  right: 15px;
  transform: translateY(-50%);
  color: #555;
  font-weight: bold;
  font-size: 16px;
}

/* Estilos do Modal */
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
}

.password-input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
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
  background-color: #42b983;
  color: white;
}

.modal-confirm-btn:disabled {
  background-color: #a5d6a7;
  cursor: not-allowed;
  opacity: 0.6;
}
</style>