package org.estga.model;

/**
 * Representa a entidade Utilizador, mapeada à tabela 'utilizador' ou base de dados.
 */
public class Utilizador {
    
    // Identificador do utilizador (chave primária)
    private int idUtilizador;
    // Nome do utilizador
    private String nome;
    // Email do utilizador
    private String email;
    // Password do utilizador (Hash)
    private String password;
    // Nível de acesso (e.g., 1 - Administrador, 2 - Normal)
    private int nivelAcesso;

    /**
     * Construtor vazio
     */
    public Utilizador() {
    }

    /**
     * Construtor completo
     */
    public Utilizador(int idUtilizador, String nome, String email, String password, int nivelAcesso) {
        this.idUtilizador = idUtilizador;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.nivelAcesso = nivelAcesso;
    }

    // Getters e Setters
    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(int nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
    
    /**
     * Retorna o nome do utilizador para ser exibido (pode ser usado em logs ou JComboBox)
     */
    @Override
    public String toString() {
        return nome;
    }
}