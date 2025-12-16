package org.estga.service;

import org.estga.data.DBConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class RelatorioService {

    public DefaultTableModel getDadosStock() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Produto");
        modelo.addColumn("Qtd");
        modelo.addColumn("Preço Unit.");
        modelo.addColumn("Total (€)");
        modelo.addColumn("Fornecedor");

        String sql = """
            SELECT p.nome, s.quantidade, p.preco_unitario, (s.quantidade * p.preco_unitario) as valor_total, f.nome as fornecedor
            FROM stock s
            JOIN produto p ON s.id_produto = p.id_produto
            JOIN fornecedor f ON p.id_fornecedor = f.id_fornecedor
            ORDER BY s.quantidade ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> linha = new Vector<>();
                linha.add(rs.getString("nome"));
                linha.add(rs.getInt("quantidade"));
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

    public DefaultTableModel getDadosSemanal() {
        // MUDANÇA AQUI TAMBÉM: Bloquear edição
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Data");
        modelo.addColumn("Tipo");
        modelo.addColumn("Utilizador");
        modelo.addColumn("Produto");
        modelo.addColumn("Qtd");

        String sql = """
            SELECT m.data_movimento, m.tipo_movimento, u.username, p.nome, lm.quantidade
            FROM movimento m
            JOIN linha_movimento lm ON m.id_movimento = lm.id_movimento
            JOIN produto p ON lm.id_produto = p.id_produto
            JOIN utilizador u ON m.id_utilizador = u.id_utilizador
            WHERE m.data_movimento >= NOW() - INTERVAL 7 DAY
            ORDER BY m.data_movimento DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> linha = new Vector<>();
                linha.add(rs.getString("data_movimento"));
                linha.add(rs.getString("tipo_movimento"));
                linha.add(rs.getString("username"));
                linha.add(rs.getString("nome"));
                linha.add(rs.getInt("quantidade"));
                modelo.addRow(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }
}