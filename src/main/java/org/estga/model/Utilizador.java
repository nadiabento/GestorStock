package org.estga.model;

/**
 Representa a entidade Utilizador, mapeada à tabela 'utilizador'.
 Esta classe funciona como um JavaBean com os dados básicos necessários para
 autenticação e autorização no sistema (e.g., username, password hash e perfil de acesso).
 */
public class Utilizador {

    private int idUtilizador;
    private String nome; // Usado para nome completo ou username
    private String email;
    private String password; // Deverá armazenar o hash da password
    private String perfilString; // Perfil (e.g., 'ADMIN', 'OPERADOR', 'COMPRAS')

    /**
     Construtor vazio
     */
    public Utilizador() {
    }

    /**
     * Construtor: completo para mapeamento de DB.
     idUtilizador: Identificador do utilizador (PK)
     nome: Nome completo ou username
     email: Email do utilizador
     password: Hash da password
     perfilString: Perfil do utilizador (String)
     */
    public Utilizador(int idUtilizador, String nome, String email, String password, String perfilString) {
        this.idUtilizador = idUtilizador;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.perfilString = perfilString;
    }

    /**
     Construtor para uso em DAOs (como o UtilizadorDAO.buscarTodos()),
     que só devolvem 3 campos (id, username e perfil).
     idUtilizador: ID do utilizador
     username: Nome de login
     perfil: String do perfil ('ADMIN', 'OPERADOR', etc.)
     */
    public Utilizador(int idUtilizador, String username, String perfil) {
        this.idUtilizador = idUtilizador;
        this.nome = username;      // Mapeia o username para o campo 'nome'
        this.perfilString = perfil; // Mapeia a string do perfil
        // Os outros campos (email, password) ficam nulos, o que é aceitável para listas de seleção.
    }


    // --- Getters e Setters ---

    /**
     * Obtém o identificador do utilizador (PK).
     * @return id do utilizador
     */
    public int getIdUtilizador() {
        return idUtilizador;
    }

    /**
     * Define o identificador do utilizador.
     * @param idUtilizador id a definir
     */
    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    /**
     * Obtém o nome (ou username) do utilizador.
     * @return nome do utilizador
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome (ou username) do utilizador.
     * @param nome nome a definir
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o email do utilizador.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do utilizador.
     * @param email email a definir
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém o hash da password do utilizador.
     * @return hash da password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define (armazena) o hash da password do utilizador.
     * NOTA: nunca guardar passwords em claro; guardar sempre o hash.
     * @param password hash da password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtém a representação textual do perfil do utilizador (ex: 'ADMIN').
     * @return perfil como string
     */
    public String getPerfilString() {
        return perfilString;
    }

    /**
     * Define o perfil do utilizador.
     * @param perfilString perfil a definir (ex: 'ADMIN', 'OPERADOR')
     */
    public void setPerfilString(String perfilString) {
        this.perfilString = perfilString;
    }
    @Override
    public String toString() {
        return nome;
    }
}