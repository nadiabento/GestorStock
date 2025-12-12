package org.estga.model;

/**
 * Representa a entidade Utilizador, mapeada à tabela 'utilizador' ou base de dados.
 *
 * Esta classe funciona como um JavaBean com os dados básicos necessários para
 * autenticação e autorização no sistema (e.g., nome, email, password hash e nível de acesso).
 *
 * @author Jéssica Pereira
 * @since 1.0
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
     * Construtor completo.
     *
     * @param idUtilizador Identificador do utilizador (PK)
     * @param nome Nome completo do utilizador
     * @param email Email do utilizador (login)
     * @param password Hash da password
     * @param nivelAcesso Nível de acesso/perm
     */
    public Utilizador(int idUtilizador, String nome, String email, String password, int nivelAcesso) {
        this.idUtilizador = idUtilizador;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.nivelAcesso = nivelAcesso;
    }

    // Getters e Setters

    /**
     * Obtém o identificador do utilizador.
     *
     * @return id do utilizador
     */
    public int getIdUtilizador() {
        return idUtilizador;
    }

    /**
     * Define o identificador do utilizador.
     *
     * @param idUtilizador id do utilizador
     */
    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    /**
     * Obtém o nome do utilizador.
     *
     * @return nome do utilizador
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do utilizador.
     *
     * @param nome nome do utilizador
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o email do utilizador.
     *
     * @return email do utilizador
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do utilizador.
     *
     * @param email email do utilizador
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém a password (hash) do utilizador.
     *
     * @return hash da password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a password (deve ser uma hash).
     *
     * @param password hash da password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtém o nível de acesso do utilizador.
     *
     * @return nível de acesso (1 - Admin, 2 - Normal, etc.)
     */
    public int getNivelAcesso() {
        return nivelAcesso;
    }

    /**
     * Define o nível de acesso do utilizador.
     *
     * @param nivelAcesso nível de acesso
     */
    public void setNivelAcesso(int nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
    
    /**
     * Retorna o nome do utilizador para ser exibido (pode ser usado em logs ou JComboBox).
     *
     * @return nome do utilizador
     */
    @Override
    public String toString() {
        return nome;
    }
}