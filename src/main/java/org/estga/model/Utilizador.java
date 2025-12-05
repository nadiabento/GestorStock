package org.estga.model;

/**
 * Representa a entidade Utilizador (conta no sistema).
 * Comentários em Português (PT‑PT) adicionados para documentação.
 */
public class Utilizador {
    private int idUtilizador;
    private String username;
    private String perfil;

    public Utilizador() { }

    public Utilizador(int idUtilizador, String username, String perfil) {
        this.idUtilizador = idUtilizador;
        this.username = username;
        this.perfil = perfil;
    }

    public int getIdUtilizador() { return idUtilizador; }
    public void setIdUtilizador(int idUtilizador) { this.idUtilizador = idUtilizador; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    @Override
    public String toString() { return username; }
}

