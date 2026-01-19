package org.estga.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovimentoDAO {

    public int inserirMovimento(Connection conn, String tipo, int idUtilizador) throws SQLException {
        String sql = "INSERT INTO movimento (tipo_movimento, id_utilizador) VALUES (?, ?)";
        int idMovimento = -1;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tipo);
            stmt.setInt(2, idUtilizador);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idMovimento = rs.getInt(1);
                }
            }
        }
        if (idMovimento == -1) {
            throw new SQLException("Falha ao obter o ID do movimento gerado.");
        }
        return idMovimento;
    }

    public void inserirLinhaMovimento(Connection conn, int idMovimento, int idProduto, int quantidade) throws SQLException {
        String sql = "INSERT INTO linha_movimento (id_movimento, id_produto, quantidade) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMovimento);
            stmt.setInt(2, idProduto);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
        }
    }

    public void eliminarPorProduto(Connection conn, int idProduto) throws SQLException {
        String sql = "DELETE FROM linha_movimento WHERE id_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        }
    }
}