package org.estga.data;

import org.estga.model.Utilizador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilizadorDAO {

    /**
     * Busca todos os utilizadores (funcionários) no sistema.
     * @return Lista de objetos Utilizador.
     */
    public List<Utilizador> buscarTodos() {
        List<Utilizador> utilizadores = new ArrayList<>();
        // Assumindo que apenas OPERADOR e ADMIN podem fazer saídas
        String sql = "SELECT id_utilizador, username, perfil FROM utilizador WHERE perfil IN ('ADMIN', 'OPERADOR') ORDER BY username";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Utilizador utilizador = new Utilizador(
                        rs.getInt("id_utilizador"),
                        rs.getString("username"),
                        rs.getString("perfil")
                );
                utilizadores.add(utilizador);
            }
        } catch (SQLException e) {
            // Em caso de erro, deve ser tratada a exceção
            System.err.println("Erro ao buscar utilizadores: " + e.getMessage());
        }
        return utilizadores;
    }
}