package com.ex.complexovoleibol.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "clientes")
public class Cliente {
    
    @Transient
    public static final String SEQUENCE_NAME = "clientes_sequence";

    @Id
    private long id;
    
    private String nome;
    private String email;
    private String cpfCnpj;
    private String endereco;
    private String cep;
    private String foto;
    private float valorMensalidade;
    private Date dataVencimento;
    private String estadoPagamento;
    private String nivelAcesso;
    private String empresa;

     // NOVO CAMPO para a exclusão lógica
    private boolean ativo = true;

    public Cliente() {}

    // getters and setters...
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    
    public float getValorMensalidade() { return valorMensalidade; }
    public void setValorMensalidade(float valorMensalidade) { this.valorMensalidade = valorMensalidade; }
    
    public Date getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(Date dataVencimento) { this.dataVencimento = dataVencimento; }
    
    public String getEstadoPagamento() { return estadoPagamento; }
    public void setEstadoPagamento(String estadoPagamento) { this.estadoPagamento = estadoPagamento; }
    
    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }
    
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    // NOVO: Getter e Setter para o campo 'ativo'
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
}