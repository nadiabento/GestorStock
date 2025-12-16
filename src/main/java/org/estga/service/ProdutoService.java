package org.estga.service;

import org.estga.data.DBConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class ProdutoService {

    public DefaultTableModel buscarProdutos(String termoPesquisa, String ordenacao) {
        // Modelo não editável
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Descrição");
        modelo.addColumn("Preço");
        modelo.addColumn("Stock Atual");
        modelo.addColumn("Fornecedor");

        // SQL Dinâmico: Pesquisa por nome e Ordena por Quantidade
        String ordemSql = ordenacao.equals("Maior Stock") ? "DESC" : "ASC";

        String sql = """
            SELECT p.id_produto, p.nome, p.descricao, p.preco_unitario, s.quantidade, f.nome as fornecedor
            FROM produto p
            LEFT JOIN stock s ON p.id_produto = s.id_produto
            LEFT JOIN fornecedor f ON p.id_fornecedor = f.id_fornecedor
            WHERE p.nome LIKE ?
            ORDER BY s.quantidade """ + " " + ordemSql; // <--- O ESPAÇO IMPORTANTE ESTÁ AQUI

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termoPesquisa + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> linha = new Vector<>();
                    linha.add(rs.getInt("id_produto"));
                    linha.add(rs.getString("nome"));
                    linha.add(rs.getString("descricao"));
                    linha.add(String.format("%.2f €", rs.getDouble("preco_unitario")));

                    int qtd = rs.getInt("quantidade");
                    linha.add(qtd);
                    linha.add(rs.getString("fornecedor"));

                    modelo.addRow(linha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }
}