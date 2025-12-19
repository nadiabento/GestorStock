package org.estga.data;

import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class DashboardDAO {

    // Contar total de produtos
    public int getTotalProdutos() {
        return getCount("SELECT COUNT(*) FROM produto");
    }

    // Contar alertas (Stock <= Minimo)
    public int getTotalAlertas() {
        return getCount("SELECT COUNT(*) FROM stock s JOIN produto p ON s.id_produto = p.id_produto WHERE s.quantidade <= p.stock_minimo");
    }

    // Calcular valor total do stock
    public double getValorTotalStock() {
        String sql = "SELECT SUM(s.quantidade * p.preco_unitario) FROM stock s JOIN produto p ON s.id_produto = p.id_produto";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    // Buscar dados para a tabela de alertas
    public DefaultTableModel getTabelaAlertas() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modelo.addColumn("Produto");
        modelo.addColumn("Stock Atual");
        modelo.addColumn("Mínimo");
        modelo.addColumn("Estado");

        String sql = """
            SELECT p.nome, s.quantidade, p.stock_minimo 
            FROM produto p 
            JOIN stock s ON p.id_produto = s.id_produto 
            WHERE s.quantidade <= p.stock_minimo
            ORDER BY s.quantidade ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> linha = new Vector<>();
                linha.add(rs.getString("nome"));
                int qtd = rs.getInt("quantidade");
                linha.add(qtd);
                linha.add(rs.getInt("stock_minimo"));
                linha.add(qtd == 0 ? "RUTURA" : "BAIXO");
                modelo.addRow(linha);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return modelo;
    }

    private int getCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}