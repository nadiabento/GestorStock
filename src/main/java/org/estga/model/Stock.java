package org.estga.model;

import java.sql.Timestamp;

/**
 * Representa a entidade Stock (Saldo), mapeada à tabela 'stock'.
 */
public class Stock {
    private int idStock;
    private int idProduto;
    private int quantidade;
    private Timestamp ultimaActualizacao;

    public Stock() {
        // Construtor vazio
    }

    public Stock(int idStock, int idProduto, int quantidade, Timestamp ultimaActualizacao) {
        this.idStock = idStock;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.ultimaActualizacao = ultimaActualizacao;
    }

    // --- Getters e Setters ---

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
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

    public Timestamp getUltimaActualizacao() {
        return ultimaActualizacao;
    }

    public void setUltimaActualizacao(Timestamp ultimaActualizacao) {
        this.ultimaActualizacao = ultimaActualizacao;
    }
}