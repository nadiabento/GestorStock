package org.estga.model;

import java.time.LocalDate;

public class Movimento {
    private int id;
    private LocalDate dataMovimento;
    private String tipo; // "Entrada" ou "Saida"
    private int idProduto;
    private int quantidade;
    private int idUtilizador; // Quem registou

    // Construtor Completo (pode criar outro para inserção sem 'id')
    public Movimento(int id, LocalDate dataMovimento, String tipo, int idProduto, int quantidade, int idUtilizador) {
        this.id = id;
        this.dataMovimento = dataMovimento;
        this.tipo = tipo;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.idUtilizador = idUtilizador;
    }

    // Construtor para NOVO Movimento (sem ID)
    public Movimento(LocalDate dataMovimento, String tipo, int idProduto, int quantidade, int idUtilizador) {
        this.dataMovimento = dataMovimento;
        this.tipo = tipo;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.idUtilizador = idUtilizador;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDate dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }
}