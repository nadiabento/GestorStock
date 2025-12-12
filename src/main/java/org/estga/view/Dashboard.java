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
    private JButton sairButton;
    private JButton relatóriosButton;
    private JButton produtos; // Botão para gestão CRUD de produtos

    private JButton SaidaProdutosButton;
    private JButton EntradaProdutosButton;

    private JScrollPane scrollPane1;
    private JTable tabAlertas;

    private String perfilAtual;

    public Dashboard(String perfil) {
        this.perfilAtual = perfil;

        configurarPermissoes(); // 1. Aplica as regras de acesso
        configurarAcoes();      // 2. Configura os Listeners dos botões
        carregarTabelaAlertasBD(); // 3. Carrega alertas de stock

        if (sairButton != null) {
            sairButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Lógica para voltar à tela de Login
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

    // ====================================================================
    // 1. LÓGICA DE PERMISSÕES
    // ====================================================================

    private void configurarPermissoes() {
        if (perfilAtual == null) perfilAtual = ""; // Garante que a string não é nula

        boolean isCompras = "COMPRAS".equalsIgnoreCase(perfilAtual);
        boolean isOperador = "OPERADOR".equalsIgnoreCase(perfilAtual);
        boolean isAdmin = "ADMIN".equalsIgnoreCase(perfilAtual);

        // Regras de Entrada
        boolean podeEntrada = isAdmin || isCompras || isOperador;
        if (EntradaProdutosButton != null) EntradaProdutosButton.setEnabled(podeEntrada);

        // Regras de Saída
        boolean podeSaida = isAdmin || isOperador; // Compras geralmente não faz Saída
        if (SaidaProdutosButton != null) SaidaProdutosButton.setEnabled(podeSaida);

        // Regras de Relatórios
        boolean podeRelatorios = isAdmin || isCompras;
        if (relatóriosButton != null) relatóriosButton.setEnabled(podeRelatorios);

        // Regras de CRUD de Produtos (Gestão de cadastro)
        boolean podeGerirProdutos = isAdmin || isCompras;
        if (produtos != null) produtos.setEnabled(podeGerirProdutos);

        System.out.println("Dashboard iniciado como: " + perfilAtual);
    }

    // ====================================================================
    // 2. AÇÕES DOS BOTÕES (Listeners)
    // ====================================================================

    private void configurarAcoes() {
        // Listener para o botão ENTRADA DE PRODUTOS
        if (EntradaProdutosButton != null) {
            EntradaProdutosButton.addActionListener(e -> {
                JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (dashboardFrame != null) dashboardFrame.setVisible(false);
                // NOTA: RegistroEntrada precisará do ID do Utilizador, que não está aqui.
                // Usando construtor simples por agora.
                new RegistroEntrada(dashboardFrame).setVisible(true);
            });
        }

        // Listener para o botão SAÍDA DE PRODUTOS
        if (SaidaProdutosButton != null) {
            SaidaProdutosButton.addActionListener(e -> {
                JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (dashboardFrame != null) dashboardFrame.setVisible(false);
                new RegistroSaida(dashboardFrame).setVisible(true);
            });
        }
    }

    // ====================================================================
    // 3. CARREGAMENTO DE DADOS (Alertas)
    // ====================================================================

    private void carregarTabelaAlertasBD() {
        if (tabAlertas == null) return;

        String[] colunas = {"ID", "Produto", "Stock Atual", "Mínimo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        // A query utiliza JOIN para ligar produto e stock e filtrar apenas os que estão abaixo do mínimo
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

                String estado = (qtd == 0) ? " RUTURA" : " BAIXO";

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