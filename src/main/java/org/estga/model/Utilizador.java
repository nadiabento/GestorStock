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

    public String getPerfilString() {
        return perfilString;
    }

    public void setPerfilString(String perfilString) {
        this.perfilString = perfilString;
    }
    @Override
    public String toString() {
        return nome;
    }
}