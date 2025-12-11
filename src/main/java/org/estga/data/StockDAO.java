package org.estga.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockDAO {

    /**
     * Consulta a quantidade atual em stock de um produto.
     * (Mantém a conexão local pois é apenas uma leitura).
     * idProduto: ID do produto a consultar.
     * return Quantidade atual em stock, ou 0 se não existir registo.
     */
    public int consultarQuantidade(int idProduto) {
        String sql = "SELECT quantidade FROM stock WHERE id_produto = ?";

        // Mantém o DBConnection.getConnection() localmente para operações de leitura.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar stock do produto " + idProduto + ": " + e.getMessage());
        }
        return 0; // Se não houver registo na tabela stock, assume 0
    }

    /**
     * Atualiza o stock de um produto usando uma conexão transacional.
     * REMOVIDO: conn.setAutoCommit(false), conn.commit(), conn.rollback()
     * conn: Conexão de DB externa (controlada pelo Service).
     * idProduto: ID do produto.
     * diferenca: Quantidade a somar (positivo) ou subtrair (negativo).
     * throws SQLException Em caso de erro de DB ou tentativa de subtração sem registo.
     */
    public void atualizarStock(Connection conn, int idProduto, int diferenca) throws SQLException {
        // Tenta atualizar o stock existente
        String updateSql = "UPDATE stock SET quantidade = quantidade + ? WHERE id_produto = ?";
        // Tenta inserir um novo registo se o update falhar (apenas quando a quantidade atual é 0)
        String insertSql = "INSERT INTO stock (id_produto, quantidade) VALUES (?, ?)";

        // 1. Tenta fazer o UPDATE (se o produto já existir na tabela stock)
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, diferenca);
            updateStmt.setInt(2, idProduto);
            int linhasAfetadas = updateStmt.executeUpdate();

            // 2. Se o UPDATE não afetou nenhuma linha, faz o INSERT
            if (linhasAfetadas == 0) {
                // O INSERT só é seguro se diferenca for positiva (Entrada).
                if (diferenca >= 0) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, idProduto);
                        insertStmt.setInt(2, diferenca);
                        insertStmt.executeUpdate();
                    }
                } else {
                    // Se for uma saída (diferenca < 0) e não existir registo, lança exceção.
                    // O Service fará o rollback de todo o movimento (cabeçalho + linha).
                    throw new SQLException("Erro de Stock: Não é possível subtrair stock de um produto sem registo inicial.");
                }
            }
        }
    }
}