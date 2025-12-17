package org.estga.service;

import org.estga.data.DBConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class ProdutoService {

    public DefaultTableModel buscarProdutos(String termoPesquisa, String ordenacao) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Coluna 0 (ID) e 4 (Stock) devem ser tratadas como números para alinhar à direita
                if (columnIndex == 0 || columnIndex == 4) return Integer.class;
                return Object.class;
            }
        };

        // 1. Definição das colunas (Não mude a ordem aqui)
        modelo.addColumn("ID");             // Coluna 0
        modelo.addColumn("Nome");           // Coluna 1
        modelo.addColumn("Descrição");      // Coluna 2
        modelo.addColumn("Preço");          // Coluna 3
        modelo.addColumn("Stock Atual");    // Coluna 4
        modelo.addColumn("Fornecedor");     // Coluna 5

        // 2. Lógica de Ordenação (Atenção: Os nomes devem ser iguais aos da JComboBox na View)
        String orderClause = switch (ordenacao) {
            case "Maior Stock" -> "s.quantidade DESC";
            case "Menor Stock" -> "s.quantidade ASC";
            case "Maior Preço" -> "p.preco_unitario DESC";
            case "Menor Preço" -> "p.preco_unitario ASC";
            default            -> "p.nome ASC";
        };

        // 3. SQL com nomes de colunas claros para evitar confusão
        String sql = """
        SELECT p.id_produto, p.nome, p.descricao, p.preco_unitario, 
               COALESCE(s.quantidade, 0) as valor_stock, 
               f.nome as nome_fornecedor
        FROM produto p
        LEFT JOIN stock s ON p.id_produto = s.id_produto
        LEFT JOIN fornecedor f ON p.id_fornecedor = f.id_fornecedor
        WHERE p.nome LIKE ?
        ORDER BY """ + " " + orderClause;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termoPesquisa + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> linha = new Vector<>();

                    // 4. PREENCHIMENTO MANUAL (A ordem aqui tem de bater com o passo 1)
                    linha.add(rs.getInt("id_produto"));         // ID -> Coluna 0
                    linha.add(rs.getString("nome"));           // Nome -> Coluna 1
                    linha.add(rs.getString("descricao"));      // Descrição -> Coluna 2

                    // Preço formatado (O SQL já ordenou pelo valor numérico p.preco_unitario)
                    linha.add(String.format("%.2f €", rs.getDouble("preco_unitario"))); // Preço -> Coluna 3

                    linha.add(rs.getInt("valor_stock"));       // Stock -> Coluna 4 (CORRIGE O Vazio/Desconfigurado)
                    linha.add(rs.getString("nome_fornecedor"));// Fornecedor -> Coluna 5

                    modelo.addRow(linha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }
}