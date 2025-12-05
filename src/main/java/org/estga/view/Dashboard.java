package org.estga.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard {

    private JPanel panel1;
    private JButton produtos;
    private JButton sairButton;
    private JButton relatóriosButton;
    private JScrollPane scrollPane1;
    private JTable tabAlertas;

    public Dashboard() {
        criarTabelaAlertas();


        if (sairButton != null) {
            // Dentro do construtor Dashboard()
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
    }

    public JPanel getPanel() {
        return panel1;
    }


    private void criarTabelaAlertas() {
        if (tabAlertas == null) return;

        String[] colunas = {"ID Produto", "Nome", "Stock Atual", "Stock Mínimo", "Status"};

        Object[][] dados = {
                {"001", "Parafusos M4", "5", "100", "⚠️ CRÍTICO"},
                {"045", "Martelo", "2", "5", "⚠️ BAIXO"},
                {"099", "Fita Cola", "10", "10", "🟡 ALERTA"}
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas);
        tabAlertas.setModel(modelo);
        tabAlertas.setRowHeight(25);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("SGS - Dashboard Principal");
        frame.setContentPane(new Dashboard().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}