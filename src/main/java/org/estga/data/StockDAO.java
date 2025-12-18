package org.estga.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockDAO {

    public int consultarQuantidade(int idProduto) {
        String sql = "SELECT quantidade FROM stock WHERE id_produto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("quantidade");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar stock: " + e.getMessage());
        }
        return 0;
    }

    public void atualizarStock(Connection conn, int idProduto, int diferenca) throws SQLException {
        String updateSql = "UPDATE stock SET quantidade = quantidade + ? WHERE id_produto = ?";
        String insertSql = "INSERT INTO stock (id_produto, quantidade) VALUES (?, ?)";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, diferenca);
            updateStmt.setInt(2, idProduto);
            int linhasAfetadas = updateStmt.executeUpdate();

            if (linhasAfetadas == 0) {
                if (diferenca >= 0) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, idProduto);
                        insertStmt.setInt(2, diferenca);
                        insertStmt.executeUpdate();
                    }
                } else {
                    throw new SQLException("Erro: Stock negativo sem registo inicial.");
                }
            }
        }
    }

    public void atualizarStockTransacional(Connection conn, int idProduto, int qtd, boolean isEntrada) throws SQLException {
        String sqlUpdate = isEntrada ?
                "UPDATE stock SET quantidade = quantidade + ? WHERE id_produto = ?" :
                "UPDATE stock SET quantidade = quantidade - ? WHERE id_produto = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
            stmt.setInt(1, qtd);
            stmt.setInt(2, idProduto);
            int rows = stmt.executeUpdate();

            if (rows == 0 && isEntrada) {
                try (PreparedStatement ins = conn.prepareStatement("INSERT INTO stock (id_produto, quantidade) VALUES (?, ?)")) {
                    ins.setInt(1, idProduto);
                    ins.setInt(2, qtd);
                    ins.executeUpdate();
                }
            }
        }
    }

    public void eliminarPorProduto(Connection conn, int idProduto) throws SQLException {
        String sql = "DELETE FROM stock WHERE id_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        }
    }
}