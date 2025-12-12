package org.estga.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockDAO {

    /**
     * Consulta a quantidade atual em stock de um produto.
     * @param idProduto ID do produto a consultar.
     * @return Quantidade atual em stock, ou 0 se não existir registo.
     */
    public int consultarQuantidade(int idProduto) {
        String sql = "SELECT quantidade FROM stock WHERE id_produto = ?";

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
     * Atualiza o stock de um produto, somando (Entrada) ou subtraindo (Saída) a quantidade.
     * Se não existir um registo de stock para o produto, insere um novo.
     * @param idProduto ID do produto.
     * @param diferenca Quantidade a somar (positivo) ou subtrair (negativo).
     */
    public void atualizarStock(int idProduto, int diferenca) throws SQLException {
        // Tenta atualizar o stock existente
        String updateSql = "UPDATE stock SET quantidade = quantidade + ? WHERE id_produto = ?";
        // Tenta inserir um novo registo se o update falhar (apenas quando a quantidade atual é 0)
        String insertSql = "INSERT INTO stock (id_produto, quantidade) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Tenta fazer o UPDATE (se o produto já existir na tabela stock)
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, diferenca);
                updateStmt.setInt(2, idProduto);
                int linhasAfetadas = updateStmt.executeUpdate();

                // 2. Se o UPDATE não afetou nenhuma linha, faz o INSERT
                if (linhasAfetadas == 0) {
                    // Nota: O INSERT só é seguro se diferenca for positiva (Entrada)
                    if (diferenca >= 0) {
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setInt(1, idProduto);
                            insertStmt.setInt(2, diferenca);
                            insertStmt.executeUpdate();
                        }
                    } else {
                        // Se for uma saída (diferenca < 0) e não existir registo,
                        // deve ser tratado pelo Service (que faz a verificação antes de chamar este método).
                        throw new SQLException("Erro: Não é possível subtrair stock de um produto sem registo inicial.");
                    }
                }
            }
            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            // Em caso de erro, é crucial fazer o rollback (desfazer as alterações)
            System.err.println("Erro durante a atualização de stock: " + e.getMessage());
            throw e; // Relança para que o MovimentoService possa tratar
        }
    }
}