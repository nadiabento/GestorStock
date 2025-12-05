package org.estga.model;

/**
 * Representa a entidade Fornecedor, mapeada à tabela 'fornecedor'.
 */
public class Fornecedor {
    private int idFornecedor;
    private String nome;
    private String nif;
    private String contacto;

    public Fornecedor() {
    }

    public Fornecedor(int idFornecedor, String nome, String nif, String contacto) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.nif = nif;
        this.contacto = contacto;
    }

    // --- Getters e Setters ---

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    // Método útil para exibição em JComboBox
    @Override
    public String toString() {
        return nome;
    }
}