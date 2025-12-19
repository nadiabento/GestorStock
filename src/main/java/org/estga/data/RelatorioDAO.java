package org.estga.data;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class RelatorioDAO {

    /**
     * Relatório de Stock Valorizado (Join de Produto + Stock + Fornecedor)
     */
    public DefaultTableModel buscarStockValorizado() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"Produto", "Qtd", "Preço Unit.", "Total (€)", "Fornecedor"});

        String sql = """
            SELECT p.nome, COALESCE(s.quantidade, 0) as qtd, p.preco_unitario, 
                   (COALESCE(s.quantidade, 0) * p.preco_unitario) as valor_total, 
                   f.nome as fornecedor
            FROM produto p
            LEFT JOIN stock s ON p.id_produto = s.id_produto
            LEFT JOIN fornecedor f ON p.id_fornecedor = f.id_fornecedor
            ORDER BY qtd ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> linha = new Vector<>();
                linha.add(rs.getString("nome"));
                linha.add(rs.getInt("qtd"));
                linha.add(String.format("%.2f €", rs.getDouble("preco_unitario")));
                linha.add(String.format("%.2f €", rs.getDouble("valor_total")));
                linha.add(rs.getString("fornecedor"));
                modelo.addRow(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }

    /**
     * Relatório de Movimentos (Join de Movimento + Linha + Utilizador + Produto)
     */
    public DefaultTableModel buscarMovimentos(int dias) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"Data", "Tipo", "Utilizador", "Produto", "Qtd"});

        String sql;
        if (dias > 0) {
            sql = """
                SELECT m.data_movimento, m.tipo_movimento, u.username, p.nome, lm.quantidade
                FROM movimento m
                JOIN linha_movimento lm ON m.id_movimento = lm.id_movimento
                JOIN produto p ON lm.id_produto = p.id_produto
                JOIN utilizador u ON m.id_utilizador = u.id_utilizador
                WHERE m.data_movimento >= NOW() - INTERVAL ? DAY
                ORDER BY m.data_movimento DESC
            """;
        } else {
            sql = """
                SELECT m.data_movimento, m.tipo_movimento, u.username, p.nome, lm.quantidade
                FROM movimento m
                JOIN linha_movimento lm ON m.id_movimento = lm.id_movimento
                JOIN produto p ON lm.id_produto = p.id_produto
                JOIN utilizador u ON m.id_utilizador = u.id_utilizador
                ORDER BY m.data_movimento DESC
            """;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (dias > 0) {
                stmt.setInt(1, dias);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> linha = new Vector<>();
                    linha.add(rs.getString("data_movimento"));
                    linha.add(rs.getString("tipo_movimento"));
                    linha.add(rs.getString("username"));
                    linha.add(rs.getString("nome"));
                    linha.add(rs.getInt("quantidade"));
                    modelo.addRow(linha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }
}