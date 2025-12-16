package org.estga.view;

import org.estga.data.DBConnection;
import org.estga.model.Utilizador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PainelHome extends JPanel {

    private JTable tabelaAlertas;

    public PainelHome(Utilizador utilizador) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Cabeçalho
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Olá, " + utilizador.getUsername() + "!");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Aqui estão os alertas de stock que requerem atenção:");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);

        topPanel.add(lblTitulo);
        topPanel.add(lblSub);

        // Tabela
        tabelaAlertas = new JTable();
        tabelaAlertas.setRowHeight(30);
        tabelaAlertas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaAlertas.getTableHeader().setBackground(new Color(240, 240, 240));
        tabelaAlertas.setShowVerticalLines(false);
        tabelaAlertas.setFocusable(false);
        tabelaAlertas.setRowSelectionAllowed(false);
        tabelaAlertas.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaAlertas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        atualizarTabela(); // Carregar dados iniciais

        add(topPanel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void atualizarTabela() {
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
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> linha = new Vector<>();
                linha.add(rs.getString("nome"));
                int qtd = rs.getInt("quantidade");
                int min = rs.getInt("stock_minimo");
                linha.add(qtd);
                linha.add(min);
                linha.add(qtd == 0 ? "RUTURA" : "BAIXO");
                modelo.addRow(linha);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        tabelaAlertas.setModel(modelo);
    }
}