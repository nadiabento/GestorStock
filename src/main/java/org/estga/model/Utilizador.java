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

    /**
     Construtor para uso em DAOs (como o UtilizadorDAO.buscarTodos()),
     que só devolvem 3 campos (id, username e perfil).
     idUtilizador: ID do utilizador
     username: Nome de login
     perfil: String do perfil ('ADMIN', 'OPERADOR', etc.)
     */
    public Utilizador(int idUtilizador, String username, String perfil) {
        this.idUtilizador = idUtilizador;
        this.username = username;      // Mapeia o username para o campo 'nome'
        this.perfil = perfil; // Mapeia a string do perfil
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
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }


    /**
     * Obtém a representação textual do perfil do utilizador (ex: 'ADMIN').
     * @return perfil como string
     */
    public String getPerfilString() {
        return perfil;
    }

    /**
     * Define o perfil do utilizador.
     * @param perfil perfil a definir (ex: 'ADMIN', 'OPERADOR')
     */
    public void setPerfilString(String perfil) {
        this.perfil = perfil;
    }
    @Override
    public String toString() {
        return username + " (" + perfil + ")";
    }
}