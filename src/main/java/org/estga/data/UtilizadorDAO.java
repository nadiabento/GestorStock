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
     * Busca todos os utilizadores no sistema.
     * @return Lista de objetos Utilizador.
     */
    public List<Utilizador> buscarTodos() {
        List<Utilizador> utilizadores = new ArrayList<>();

        String sql = "SELECT id_utilizador, username, password, perfil FROM utilizador " +
                "WHERE perfil IN ('ADMIN', 'OPERADOR', 'COMPRAS') " +
                "ORDER BY username";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Utilizador u = new Utilizador();

                // Mapeamento direto da Base de Dados para o Java
                u.setIdUtilizador(rs.getInt("id_utilizador"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password")); // Necessário para criar o objeto completo
                u.setPerfil(rs.getString("perfil"));     // Guarda "ADMIN", "OPERADOR" ou "COMPRAS"

                utilizadores.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao procurar utilizadores: " + e.getMessage());
        }
        return utilizadores;
    }
}