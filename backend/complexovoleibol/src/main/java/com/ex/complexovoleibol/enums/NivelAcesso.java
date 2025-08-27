package com.ex.complexovoleibol.enums;

public enum NivelAcesso {
    ADMINISTRADOR_SISTEMA("Administrador do Sistema"),
    GESTOR_LOCAL("Gestor Local"),
    AUXILIADOR_GESTAO("Auxiliador de Gestão");

    private final String descricao;

    NivelAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}