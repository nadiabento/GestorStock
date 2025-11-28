package org.estga.model;

import java.math.BigDecimal;

/**
 * Representa a entidade Produto, mapeada à tabela 'produto' na base de dados.
 * Inclui o stockAtual, que é um campo agregado da tabela 'stock'.
 */
public class Produto {
    private int idProduto;
    private String nome;
    private String descricao;
    private BigDecimal precoUnitario;
    private int stockMinimo;
    private int idFornecedor;
    // stockAtual é um campo agregado necessário para a validação na View
    private int stockAtual;

    public Produto() {
        // Construtor vazio
    }

    public Produto(int idProduto, String nome, String descricao, BigDecimal precoUnitario, int stockMinimo, int idFornecedor, int stockAtual) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.stockMinimo = stockMinimo;
        this.idFornecedor = idFornecedor;
        this.stockAtual = stockAtual;
    }

    // --- Getters e Setters ---

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public int getStockAtual() {
        return stockAtual;
    }

    public void setStockAtual(int stockAtual) {
        this.stockAtual = stockAtual;
    }

    /**
     * Retorna o nome do produto para ser exibido corretamente nos JComboBox.
     */
    @Override
    public String toString() {
        return nome;
    }
}