package org.estga.model;

import java.math.BigDecimal;

/**
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
    private int stockMaximo; // opcional, para uso futuro
    private int idFornecedor;
    private int stockAtual;

    /**
     * Construtor vazio.
     */
    public Produto() {
        // Construtor vazio
    }

    /**
     * Construtor completo.
     *
     * @param idProduto Identificador do produto (PK)
     * @param nome Nome comercial do produto
     * @param descricao Descrição detalhada do produto
     * @param precoUnitario Preço unitário (BigDecimal) em euros
     * @param stockMinimo Número mínimo de unidades que dispara alerta
     * @param stockMaximo Número máximo de unidades (opcional)
     * @param idFornecedor Identificador do fornecedor associado (pode ser 0/NULL)
     * @param stockAtual Stock actual disponível
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

    // Getters e Setters

    /**
     * Obtém o identificador do produto.
     *
     * @return id do produto
     */
    public int getIdProduto() {
        return idProduto;
    }

    /**
     * Define o identificador do produto.
     *
     * @param idProduto id do produto
     */
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * Obtém o nome do produto.
     *
     * @return nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do produto.
     *
     * @param nome nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição do produto.
     *
     * @return descrição do produto
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do produto.
     *
     * @param descricao descrição do produto
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o preço unitário do produto.
     *
     * @return preço unitário (BigDecimal)
     */
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    /**
     * Define o preço unitário do produto.
     *
     * @param precoUnitario preço unitário (BigDecimal)
     */
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    /**
     * Obtém o stock mínimo configurado para o produto.
     *
     * @return stock mínimo
     */
    public int getStockMinimo() {
        return stockMinimo;
    }

    /**
     * Define o stock mínimo do produto.
     *
     * @param stockMinimo stock mínimo
     */
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /**
     * Obtém o stock máximo configurado para o produto.
     *
     * @return stock máximo
     */
    public int getStockMaximo() {
        return stockMaximo;
    }

    /**
     * Define o stock máximo do produto.
     *
     * @param stockMaximo stock máximo
     */
    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    /**
     * Obtém o identificador do fornecedor associado.
     *
     * @return id do fornecedor
     */
    public int getIdFornecedor() {
        return idFornecedor;
    }

    /**
     * Define o identificador do fornecedor associado.
     *
     * @param idFornecedor id do fornecedor
     */
    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    /**
     * Obtém o stock actual do produto.
     *
     * @return stock actual
     */
    public int getStockAtual() {
        return stockAtual;
    }

    /**
     * Define o stock actual do produto.
     *
     * @param stockAtual stock actual
     */
    public void setStockAtual(int stockAtual) {
        this.stockAtual = stockAtual;
    }

    /**
     * Retorna o nome do produto para ser exibido corretamente nos JComboBox.
     *
     * @return nome do produto
     */
    @Override
    public String toString() {
        return nome;
    }
}