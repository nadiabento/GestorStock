package org.estga.data;

import org.estga.model.Produto; // Usa o pacote do seu modelo Produto
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    /**
     * Insere um novo produto na tabela `produto`.
     * Se a inserção for bem sucedida devolve o id gerado (>=1), caso contrário devolve -1.
     */
    public int inserirProduto(Produto produto) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("[DB] Conexão nula ao inserir produto");
                return -1;
            }
            return inserirProduto(conn, produto);
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto (connection): " + e.getMessage());
            return -1;
        }
    }

    /**
     * Versão transaccional: insere um produto usando uma Connection externa.
     * Não fecha a Connection (quem abriu a conexão é responsável por fechar/commit/rollback).
     */
    public int inserirProduto(Connection conn, Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao, preco_unitario, stock_minimo, id_fornecedor) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPrecoUnitario());
            stmt.setInt(4, produto.getStockMinimo());
            stmt.setInt(5, produto.getIdFornecedor());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                return -1;
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Busca todos os produtos (sem informação de stock).
     */
    public List<Produto> buscarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id_produto, nome, descricao, preco_unitario, stock_minimo, id_fornecedor FROM produto ORDER BY nome";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("[DB] Conexão nula ao buscar produtos");
                return produtos;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Produto p = new Produto(
                            rs.getInt("id_produto"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getBigDecimal("preco_unitario"),
                            rs.getInt("stock_minimo"),
                            rs.getInt("id_fornecedor"),
                            0 // stockAtual desconhecido aqui
                    );
                    produtos.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }

    /**
     * Busca um produto pelo seu id.
     * Retorna null se não existir.
     */
    public Produto buscarPorId(int idProduto) {
        String sql = "SELECT id_produto, nome, descricao, preco_unitario, stock_minimo, id_fornecedor FROM produto WHERE id_produto = ?";
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("[DB] Conexão nula ao buscar produto por id");
                return null;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idProduto);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Produto(
                                rs.getInt("id_produto"),
                                rs.getString("nome"),
                                rs.getString("descricao"),
                                rs.getBigDecimal("preco_unitario"),
                                rs.getInt("stock_minimo"),
                                rs.getInt("id_fornecedor"),
                                0
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Atualiza os dados de um produto existente. Retorna true se a atualização foi bem sucedida.
     */
    public boolean atualizarProduto(Produto produto) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("[DB] Conexão nula ao atualizar produto");
                return false;
            }
            return atualizarProduto(conn, produto);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto (connection): " + e.getMessage());
            return false;
        }
    }

    /**
     * Versão transaccional: atualiza um produto usando uma Connection externa.
     */
    public boolean atualizarProduto(Connection conn, Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco_unitario = ?, stock_minimo = ?, id_fornecedor = ? WHERE id_produto = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPrecoUnitario());
            stmt.setInt(4, produto.getStockMinimo());
            stmt.setInt(5, produto.getIdFornecedor());
            stmt.setInt(6, produto.getIdProduto());

            int affected = stmt.executeUpdate();
            return affected > 0;

        }
    }

    /**
     * Elimina um produto pelo id. Retorna true se a remoção foi bem sucedida.
     */
    public boolean eliminarProduto(int idProduto) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("[DB] Conexão nula ao eliminar produto");
                return false;
            }
            return eliminarProduto(conn, idProduto);
        } catch (SQLException e) {
            System.err.println("Erro ao eliminar produto (connection): " + e.getMessage());
            return false;
        }
    }

    /**
     * Versão transaccional: elimina um produto usando uma Connection externa.
     */
    public boolean eliminarProduto(Connection conn, int idProduto) throws SQLException {
        String sql = "DELETE FROM produto WHERE id_produto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            int affected = stmt.executeUpdate();
            return affected > 0;

        }
    }

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

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("[DB] Conexão nula ao buscar produtos e stock");
                return produtos;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql);
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
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos e stock: " + e.getMessage());
        }
        return produtos;
    }
}