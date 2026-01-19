package org.estga.view;

import org.estga.model.Utilizador;
import org.estga.service.HomeService; // Usa o Service

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PainelHome extends JPanel {

    private JLabel lblTotalProdutos;
    private JLabel lblAlertas;
    private JLabel lblValorStock;
    private JTable tabelaAlertas;
    private HomeService service; // Referência ao Service

    public PainelHome(Utilizador utilizador) {
        this.service = new HomeService();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // 1. CABEÇALHO
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));

        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("Olá, " + utilizador.getUsername() + "!");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 37, 41));

        JLabel lblSubtitulo = new JLabel("Aqui tens o resumo do teu inventário hoje.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);

        titles.add(lblTitulo);
        titles.add(lblSubtitulo);
        headerPanel.add(titles, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. CARDS DE KPI
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(new Color(245, 245, 245));
        cardsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        lblTotalProdutos = new JLabel("...");
        lblAlertas = new JLabel("...");
        lblValorStock = new JLabel("...");

        cardsPanel.add(criarCard(" Total Produtos", lblTotalProdutos, new Color(23, 162, 184)));
        cardsPanel.add(criarCard("️ Atenção (Stock)", lblAlertas, new Color(220, 53, 69)));
        cardsPanel.add(criarCard(" Valor em Armazém", lblValorStock, new Color(40, 167, 69)));

        // 3. TABELA DE ALERTAS
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel lblTabTitulo = new JLabel(" Produtos em Risco (Baixo Stock ou Rutura)");
        lblTabTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTabTitulo.setForeground(new Color(100, 100, 100));
        lblTabTitulo.setPreferredSize(new Dimension(0, 45));
        lblTabTitulo.setBorder(new EmptyBorder(0, 10, 0, 0));

        tabelaAlertas = new JTable();
        tabelaAlertas.setRowHeight(35);
        tabelaAlertas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaAlertas.getTableHeader().setBackground(new Color(248, 249, 250));
        tabelaAlertas.setShowVerticalLines(false);
        tabelaAlertas.setFocusable(false);
        tabelaAlertas.setRowSelectionAllowed(false);
        tabelaAlertas.getTableHeader().setReorderingAllowed(false);

        tabelaAlertas.setDefaultRenderer(Object.class, new AlertaRenderer());

        JScrollPane scrollPane = new JScrollPane(tabelaAlertas);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        centerPanel.add(lblTabTitulo, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.add(cardsPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        atualizarTabela();
    }

    public void atualizarTabela() {
        lblTotalProdutos.setText(String.valueOf(service.getTotalProdutos()));
        lblAlertas.setText(String.valueOf(service.getAlertasCount()));
        lblValorStock.setText(String.format("%.2f €", service.getValorStock()));

        tabelaAlertas.setModel(service.getModelTabela());
    }

    private JPanel criarCard(String titulo, JLabel labelValor, Color corBarra) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(6, 0));
        sideBar.setBackground(corBarra);

        JPanel content = new JPanel(new GridLayout(2, 1));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTit = new JLabel(titulo.toUpperCase());
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTit.setForeground(Color.GRAY);

        labelValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labelValor.setForeground(new Color(33, 37, 41));

        content.add(lblTit);
        content.add(labelValor);

        card.add(sideBar, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(0, 110));

        return card;
    }

    class AlertaRenderer extends DefaultTableCellRenderer {
        Color COR_RUTURA = new Color(255, 235, 238);
        Color COR_BAIXO = new Color(255, 249, 196);
        Color COR_NORMAL = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String estado = (String) table.getModel().getValueAt(row, 3);

            if ("RUTURA".equals(estado)) c.setBackground(COR_RUTURA);
            else if ("BAIXO".equals(estado)) c.setBackground(COR_BAIXO);
            else c.setBackground(COR_NORMAL);

            setForeground(new Color(50, 50, 50));
            setHorizontalAlignment(SwingConstants.CENTER);
            if (column == 0) {
                setHorizontalAlignment(SwingConstants.LEFT);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            } else {
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }
            return c;
        }
    }
}