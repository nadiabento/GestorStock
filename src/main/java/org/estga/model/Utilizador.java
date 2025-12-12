package org.estga.model;

/**
 * Representa a entidade Utilizador (Funcionário), mapeada à tabela 'utilizador'.
 */
public class Utilizador {
    private int idUtilizador;
    private String username;
    private String perfil;

    public Utilizador() {
    }

    public Utilizador(int idUtilizador, String username, String perfil) {
        this.idUtilizador = idUtilizador;
        this.username = username;
        this.perfil = perfil;
    }

    // --- Getters e Setters ---

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    // Método útil para exibição em JComboBox (o nome do funcionário/utilizador)
    @Override
    public String toString() {
        return username;
    }
}