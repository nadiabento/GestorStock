package org.estga.service;

import org.estga.data.DBConnection;
import org.estga.model.Utilizador; // Vamos usar o teu modelo corrigido
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    /**
     * Verifica login e devolve o Utilizador completo (com ID e Perfil).
     * Retorna null se falhar.
     */
    public Utilizador autenticar(String username, String password) {
        String sql = "SELECT id_utilizador, username, perfil FROM utilizador WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilizador u = new Utilizador();
                    u.setIdUtilizador(rs.getInt("id_utilizador"));
                    u.setUsername(rs.getString("username"));
                    u.setPerfil(rs.getString("perfil"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}