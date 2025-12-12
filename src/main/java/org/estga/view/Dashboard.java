package org.estga.view;

import org.estga.data.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dashboard {
    private JPanel panel1;
    private JButton produtos;
    private JButton sairButton;
    private JButton relatóriosButton;
    private JScrollPane scrollPane1;
    private JTable tabAlertas;

    private String perfilAtual;

    public Dashboard(String perfil) {
        this.perfilAtual = perfil;

        configurarPermissoes();

        carregarTabelaAlertasBD();

        if (sairButton != null) {
            sairButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    SwingUtilities.getWindowAncestor(panel1).dispose();

                    JFrame frameLogin = new JFrame("Login SGS");
                    frameLogin.setContentPane(new Login().getPanel());
                    frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameLogin.setSize(600, 400);
                    frameLogin.setLocationRelativeTo(null);
                    frameLogin.setVisible(true);
                }
            });
        }
    }

    public JPanel getPanel() {
        return panel1;
    }

    private void configurarPermissoes() {
        if (perfilAtual == null) perfilAtual = "OPERADOR";

        if ("OPERADOR".equalsIgnoreCase(perfilAtual)) {
            if (relatóriosButton != null) {
                relatóriosButton.setEnabled(false);
                relatóriosButton.setToolTipText("Acesso reservado a Admin/Compras");
            }
        }
        System.out.println("Dashboard iniciado como: " + perfilAtual);
    }

    private void carregarTabelaAlertasBD() {
        if (tabAlertas == null) return;

        String[] colunas = {"ID", "Produto", "Stock Atual", "Mínimo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        String sql = "SELECT p.id_produto, p.nome, s.quantidade, p.stock_minimo " +
                "FROM produto p " +
                "JOIN stock s ON p.id_produto = s.id_produto " +
                "WHERE s.quantidade <= p.stock_minimo";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                modelo.addRow(new Object[]{"Erro", "Sem Conexão", 0, 0, "OFFLINE"});
                tabAlertas.setModel(modelo);
                return;
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_produto");
                String nome = rs.getString("nome");
                int qtd = rs.getInt("quantidade");
                int min = rs.getInt("stock_minimo");

                String estado = (qtd == 0) ? "⛔ RUTURA" : "⚠️ BAIXO";

                modelo.addRow(new Object[]{id, nome, qtd, min, estado});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            modelo.addRow(new Object[]{"Erro", "Falha SQL", 0, 0, e.getMessage()});
        }

        tabAlertas.setModel(modelo);
        tabAlertas.setRowHeight(25);
    }
}