package org.estga;

import org.estga.data.DBConnection;
import org.estga.model.Utilizador;
import org.estga.view.LoginFrame;
import org.estga.view.PainelEntrada;
import org.estga.view.PainelHome;
import org.estga.view.PainelProdutos;
import org.estga.view.PainelRelatorios;
import org.estga.view.PainelSaida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Utilizador utilizadorAtual;
    private PainelHome panelHome;

    public Main(Utilizador user) {
        super("SGS - Gestão de Stock");
        this.utilizadorAtual = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==================================================================================
        // 1. BARRA LATERAL (SIDEBAR)
        // ==================================================================================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(240, 600));

        // Título
        JLabel lblTitulo = new JLabel("GESTÃO STOCK");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblTitulo);

        sidebar.add(Box.createVerticalStrut(30));

        // Info do Utilizador
        JLabel lblOla = new JLabel("Olá, " + user.getUsername());
        lblOla.setForeground(new Color(40, 167, 69)); // Verde
        lblOla.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblOla.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblOla);

        JLabel lblPerfil = new JLabel(user.getPerfil());
        lblPerfil.setForeground(Color.GRAY);
        lblPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPerfil.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblPerfil);
        sidebar.add(Box.createVerticalStrut(40));

        // Botões do Menu
        JButton btnHome = criarBotaoMenu("  Início");
        JButton btnProdutos = criarBotaoMenu("  Produtos");
        JButton btnEntrada = criarBotaoMenu("  Registar Entrada");
        JButton btnSaida = criarBotaoMenu("  Registar Saída");
        JButton btnRelatorios = criarBotaoMenu("  Relatórios");
        JButton btnSair = criarBotaoMenu("  Sair");

        // Adicionar Botões à Barra
        sidebar.add(btnHome);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(btnProdutos);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(btnEntrada);
        sidebar.add(Box.createVerticalStrut(5));

        if (!user.getPerfil().equals("COMPRAS")) {
            sidebar.add(btnSaida);
            sidebar.add(Box.createVerticalStrut(5));
        }

        if (!user.getPerfil().equals("OPERADOR")) {
            sidebar.add(btnRelatorios);
            sidebar.add(Box.createVerticalStrut(5));
        }

        sidebar.add(Box.createVerticalGlue());

        sidebar.add(btnSair);
        sidebar.add(Box.createVerticalStrut(10));

        // ==================================================================================
        // 2. ÁREA DE CONTEÚDO (CARD LAYOUT)
        // ==================================================================================
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        this.panelHome = new PainelHome(user);
        PainelProdutos panelProdutos = new PainelProdutos(this);
        PainelEntrada panelEntrada = new PainelEntrada(this);
        PainelSaida panelSaida = new PainelSaida(this);
        PainelRelatorios panelRelatorios = new PainelRelatorios();

        cardPanel.add(panelHome, "HOME");
        cardPanel.add(panelProdutos, "PRODUTOS");
        cardPanel.add(panelEntrada, "ENTRADA");
        cardPanel.add(panelSaida, "SAIDA");
        cardPanel.add(panelRelatorios, "RELATORIOS");

        // ==================================================================================
        // 3. AÇÕES DOS BOTÕES
        // ==================================================================================
        btnHome.addActionListener(e -> {
            atualizarHome();
            cardLayout.show(cardPanel, "HOME");
        });
        btnProdutos.addActionListener(e -> cardLayout.show(cardPanel, "PRODUTOS"));
        btnEntrada.addActionListener(e -> cardLayout.show(cardPanel, "ENTRADA"));
        btnSaida.addActionListener(e -> cardLayout.show(cardPanel, "SAIDA"));
        btnRelatorios.addActionListener(e -> cardLayout.show(cardPanel, "RELATORIOS"));

        btnSair.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        add(sidebar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    public void atualizarHome() {
        if (panelHome != null) panelHome.atualizarTabela();
    }

    public int getIdUtilizadorAtual() {
        return utilizadorAtual.getIdUtilizador();
    }

    public Utilizador getUtilizadorAtual() {
        return utilizadorAtual;
    }
    // ----------------------------------

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFocusPainted(false);
        btn.setBackground(new Color(33, 37, 41));
        btn.setForeground(new Color(200, 200, 200));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setMaximumSize(new Dimension(240, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 58, 64));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(33, 37, 41));
                btn.setForeground(new Color(200, 200, 200));
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        System.out.println("A iniciar o programa ...");
        DBConnection.getConnection();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}