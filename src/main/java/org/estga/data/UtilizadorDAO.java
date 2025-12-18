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
     * Procura todos os utilizadores (para listagens futuras).
     */
    public List<Utilizador> buscarTodos() {
        List<Utilizador> utilizadores = new ArrayList<>();
        String sql = "SELECT id_utilizador, username, password, perfil FROM utilizador ORDER BY username";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Utilizador u = new Utilizador();
                u.setIdUtilizador(rs.getInt("id_utilizador"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setPerfil(rs.getString("perfil"));
                utilizadores.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizadores;
    }

    /**
     * Verifica as credenciais e devolve o utilizador se estiver correto.
     */
    public Utilizador autenticar(String username, String password) {
        String sql = "SELECT id_utilizador, username, perfil FROM utilizador WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        return null; // Login falhou
    }
}