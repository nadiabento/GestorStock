package org.estga.model;

public class Utilizador {

    private int idUtilizador;
    private String username;
    private String password;
    private String perfil;

    // 1. Construtor Vazio
    public Utilizador() {}

    // 2. Construtor Completo
    public Utilizador(int idUtilizador, String username, String password, String perfil) {
        this.idUtilizador = idUtilizador;
        this.username = username;
        this.password = password;
        this.perfil = perfil;
    }

    // Getters e Setters
    public int getIdUtilizador() { return idUtilizador; }
    public void setIdUtilizador(int idUtilizador) { this.idUtilizador = idUtilizador; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    // toString() para a ComboBox mostrar o nome bonito
    @Override
    public String toString() {
        return username + " (" + perfil + ")";
    }
}