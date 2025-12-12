package org.estga.model;

import java.math.BigDecimal;

/**
 * Representa um produto do inventário.
 * Representa um produto do inventário.
 *
 * Esta classe funciona como um JavaBean que mapeia a entidade PRODUTO na base de dados.
 * Contém atributos essenciais para o controlo de inventário, nomeadamente o stock
 * atual e o stock mínimo que dispara alertas.
 *
 * @author Jéssica Pereira
 * @since 1.0
 */
public class Produto {
    private int idProduto;
    private String nome;
    private String descricao;
    private BigDecimal precoUnitario;
    private int stockMinimo;
    private int stockMaximo; // Campo adicionado para controlo
    private int idFornecedor;
    private int stockAtual; // Campo agregado da tabela 'stock'

    /**
     * Construtor vazio.
     */
    public Produto() {
        // Construtor vazio
    }

    /**
     * Construtor COMPLETO para mapeamento de DB (8 argumentos).
     *
     * idProduto: Identificador do produto (PK)
     * nome: Nome comercial do produto
     * descricao: Descrição detalhada do produto
     * precoUnitario: Preço unitário (BigDecimal) em euros
     * stockMinimo: Número mínimo de unidades
     * stockMaximo: Número máximo de unidades
     * idFornecedor: Identificador do fornecedor associado
     * stockAtual: Stock actual disponível
     */
    public Produto(int idProduto, String nome, String descricao, BigDecimal precoUnitario, int stockMinimo, int stockMaximo, int idFornecedor, int stockAtual) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.idFornecedor = idFornecedor;
        this.stockAtual = stockAtual;
    }

    /**
     * Construtor Padrão (7 argumentos).
     * Assume stockMaximo = 0.
     */
    public Produto(int idProduto, String nome, String descricao, BigDecimal precoUnitario, int stockMinimo, int idFornecedor, int stockAtual) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.stockMinimo = stockMinimo;
        this.idFornecedor = idFornecedor;
        this.stockAtual = stockAtual;
        this.stockMaximo = 0; // Define o valor padrão
    }


    // Getters e Setters

    /** Obtém o identificador do produto. */
    public int getIdProduto() {
        return idProduto;
    }

    /** Define o identificador do produto. */
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    /** Obtém o nome do produto. */
    public String getNome() {
        return nome;
    }

    /** Define o nome do produto. */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** Obtém a descrição do produto. */
    public String getDescricao() {
        return descricao;
    }

    /** Define a descrição do produto. */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /** Obtém o preço unitário do produto. */
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    /** Define o preço unitário do produto. */
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    /** Obtém o stock mínimo configurado. */
    public int getStockMinimo() {
        return stockMinimo;
    }

    /** Define o stock mínimo. */
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /** Obtém o stock máximo configurado. */
    public int getStockMaximo() {
        return stockMaximo;
    }

    /** Define o stock máximo. */
    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    /** Obtém o identificador do fornecedor. */
    public int getIdFornecedor() {
        return idFornecedor;
    }

    /** Define o identificador do fornecedor. */
    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    /** Obtém o stock actual do produto. */
    public int getStockAtual() {
        return stockAtual;
    }

    /** Define o stock actual do produto. */
    public void setStockAtual(int stockAtual) {
        this.stockAtual = stockAtual;
    }

    /**
     * Retorna o nome do produto para ser exibido corretamente nos JComboBox (ex: no Registo de Entrada).
     *
     * @return nome do produto
     */
    @Override
    public String toString() {
        return nome;
    }
}