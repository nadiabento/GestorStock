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
                        rs.getInt("stock_atual")
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos e stock: " + e.getMessage());
        }
        return produtos;
    }

    public int inserir(Connection conn, Produto p) throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao, preco_unitario, stock_minimo, id_fornecedor) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setBigDecimal(3, p.getPrecoUnitario());
            stmt.setInt(4, p.getStockMinimo());
            stmt.setInt(5, p.getIdFornecedor());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean atualizar(Connection conn, Produto p) throws SQLException {
        String sql = "UPDATE produto SET nome=?, descricao=?, preco_unitario=?, stock_minimo=?, id_fornecedor=? WHERE id_produto=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setBigDecimal(3, p.getPrecoUnitario());
            stmt.setInt(4, p.getStockMinimo());
            stmt.setInt(5, p.getIdFornecedor());
            stmt.setInt(6, p.getIdProduto());
            return stmt.executeUpdate() > 0;
        }
    }

    public void eliminar(Connection conn, int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM produto WHERE id_produto=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}