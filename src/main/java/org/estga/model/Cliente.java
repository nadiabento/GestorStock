package org.estga.model;

public class Cliente {
    private int idCliente;
    private String nome;

    public Cliente(int idCliente, String nome) {
        this.idCliente = idCliente;
        this.nome = nome;
    }
    public String getNome() { return nome; }
    public int getIdCliente() { return idCliente; }
    @Override public String toString() { return nome; }
}