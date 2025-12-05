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
    private JButton SaidaProdutosButton;
    private JButton EntradaProdutosButton;
    private JButton produtosButton;

    public Dashboard() {
        criarTabelaAlertas();

        // 1. Listener para o botão SAIR (Já existente)
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

        // --- NOVO: Ação para o botão ENTRADA DE PRODUTOS ---
        // ESTE BLOCO FOI MOVIDO PARA DENTRO DO CONSTRUTOR
        if (EntradaProdutosButton != null) {
            EntradaProdutosButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 1. Obtém a referência da JFrame atual (Dashboard)
                    JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);

                    // 2. Esconde a janela do Dashboard
                    dashboardFrame.setVisible(false);

                    // 3. Cria e mostra a janela de RegistroEntrada, passando o Dashboard como janela anterior
                    new RegistroEntrada(dashboardFrame).setVisible(true); // Classe RegistroEntrada deve ser importada ou estar no mesmo pacote
                }
            });
        }

        // --- NOVO: Ação para o botão SAÍDA DE PRODUTOS ---
        // ESTE BLOCO FOI MOVIDO PARA DENTRO DO CONSTRUTOR
        if (SaidaProdutosButton != null) {
            SaidaProdutosButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 1. Obtém a referência da JFrame atual (Dashboard)
                    JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);

                    // 2. Esconde a janela do Dashboard
                    dashboardFrame.setVisible(false);

                    // 3. Cria e mostra a janela de RegistroSaida, passando o Dashboard como janela anterior
                    new RegistroSaida(dashboardFrame).setVisible(true); // Classe RegistroSaida deve ser importada ou estar no mesmo pacote
                }
            });
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