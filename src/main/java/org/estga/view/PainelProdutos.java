package org.estga.view;

import org.estga.service.ProdutoService;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PainelProdutos extends JPanel {

    private JTable tabela;
    private JTextField txtPesquisa;
    private JComboBox<String> cbOrdenacao;
    private ProdutoService service;

    public PainelProdutos() {
        this.service = new ProdutoService();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. BARRA DE TOPO (PESQUISA) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Campo de Texto
        txtPesquisa = new JTextField(15);
        estilizarCampo(txtPesquisa);

        // Combobox de Ordenação
        String[] opcoes = {"Menor Stock", "Maior Stock"};
        cbOrdenacao = new JComboBox<>(opcoes);
        cbOrdenacao.setBackground(Color.WHITE);
        cbOrdenacao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbOrdenacao.setPreferredSize(new Dimension(150, 35));

        // Botão Pesquisar
        JButton btnPesquisar = new JButton("Pesquisar");
        estilizarBotao(btnPesquisar);

        // Adicionar componentes
        topPanel.add(new JLabel("Produto:"));
        topPanel.add(txtPesquisa);
        topPanel.add(new JLabel("Ordenar por:"));
        topPanel.add(cbOrdenacao);
        topPanel.add(btnPesquisar);

        // --- 2. TABELA ---
        tabela = new JTable();
        tabela.setRowHeight(30);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(new Color(240, 240, 240));
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setShowVerticalLines(false);
        tabela.setFocusable(false);
        tabela.setRowSelectionAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // --- 3. AÇÕES ---
        // Ação do Botão
        btnPesquisar.addActionListener(e -> atualizarTabela());

        // Ação ao carregar Enter na caixa de texto
        txtPesquisa.addActionListener(e -> atualizarTabela());

        // Carregar dados iniciais
        atualizarTabela();

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        String termo = txtPesquisa.getText();
        String ordem = (String) cbOrdenacao.getSelectedItem();
        tabela.setModel(service.buscarProdutos(termo, ordem));
    }

    // --- ESTILOS ---
    private void estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 5, 5, 5)));
    }

    private void estilizarBotao(JButton btn) {
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setBackground(new Color(33, 37, 41));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }
}