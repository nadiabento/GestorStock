package org.estga.data;

import org.estga.model.Produto; // Usa o pacote do seu modelo Produto
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    /**
     * Busca todos os produtos e a sua quantidade em stock, que é necessária
     * para popular as janelas de Entrada/Saída e validar o stock.
     * @return Lista de objetos Produto com o campo stockAtual preenchido.
     */
    public List<Produto> buscarTodosComStock() {
        List<Produto> produtos = new ArrayList<>();

        // A query faz um LEFT JOIN com a tabela stock para obter a quantidade.
        // COALESCE(s.quantidade, 0) garante que o stock é 0 se o produto
        // ainda não tiver um registo na tabela stock.
        String sql = "SELECT p.*, COALESCE(s.quantidade, 0) AS stock_atual " +
                "FROM produto p " +
                "LEFT JOIN stock s ON p.id_produto = s.id_produto " +
                "ORDER BY p.nome";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("preco_unitario"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("id_fornecedor"),
                        rs.getInt("stock_atual") // Mapeia o resultado do COALESCE
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos e stock: " + e.getMessage());
        }
        return produtos;
    }
}