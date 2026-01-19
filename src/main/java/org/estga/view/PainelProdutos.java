package org.estga.view;

import org.estga.Main;
import org.estga.service.ProdutoService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

public class PainelProdutos extends JPanel {

    private JTable tabela;
    private JTextField txtPesquisa;
    private JComboBox<String> cbOrdenacao;
    private ProdutoService service;
    private Main mainApp;

    public PainelProdutos(Main mainApp) {
        this.mainApp = mainApp;
        this.service = new ProdutoService();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                atualizarTabela();
            }
        });

        // ==================================================================================
        // 1. BARRA DE TOPO
        // ==================================================================================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        topPanel.add(new JLabel("Pesquisar:"));
        txtPesquisa = new JTextField(15);
        estilizarCampo(txtPesquisa);
        topPanel.add(txtPesquisa);

        String[] opcoes = {"Menor Stock", "Maior Stock", "Preço Menor", "Preço Maior"};
        cbOrdenacao = new JComboBox<>(opcoes);
        cbOrdenacao.setBackground(Color.WHITE);
        cbOrdenacao.setPreferredSize(new Dimension(130, 35));
        topPanel.add(cbOrdenacao);

        JButton btnPesquisar = new JButton("Procurar");
        estilizarBotao(btnPesquisar, new Color(33, 37, 41));
        btnPesquisar.setPreferredSize(new Dimension(100, 35));
        topPanel.add(btnPesquisar);

        topPanel.add(Box.createHorizontalStrut(20));

        JButton btnNovo = new JButton(" Novo");
        estilizarBotao(btnNovo, new Color(40, 167, 69));

        JButton btnEditar = new JButton(" Editar");
        estilizarBotao(btnEditar, new Color(255, 140, 0));

        JButton btnEliminar = new JButton("️ Eliminar");
        estilizarBotao(btnEliminar, new Color(220, 53, 69));

        // ==================================================================================
        // CONTROLO DE ACESSOS
        // ==================================================================================
        if (mainApp.getUtilizadorAtual() != null) {
            String perfil = mainApp.getUtilizadorAtual().getPerfil();

            // Lógica 1: Quem pode criar e editar? (Todos menos o Operador)
            if (!perfil.equals("OPERADOR")) {
                topPanel.add(btnNovo);
                topPanel.add(btnEditar);
            }

            // Lógica 2: Quem pode eliminar? (Apenas quem não é Operador e não é Compras)
            if (!perfil.equals("OPERADOR") && !perfil.equals("COMPRAS")) {
                topPanel.add(btnEliminar);
            }
        }

        // ==================================================================================
        // 2. TABELA
        // ==================================================================================
        tabela = new JTable();
        tabela.setRowHeight(35);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(new Color(240, 240, 240));
        tabela.setShowVerticalLines(false);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabela.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ==================================================================================
        // 3. LISTENERS
        // ==================================================================================
        btnPesquisar.addActionListener(e -> atualizarTabela());
        txtPesquisa.addActionListener(e -> atualizarTabela());
        cbOrdenacao.addActionListener(e -> atualizarTabela());

        btnNovo.addActionListener(e -> abrirDialogoNovoProduto());
        btnEditar.addActionListener(e -> abrirDialogoEditarProduto());
        btnEliminar.addActionListener(e -> eliminarProdutoSelecionado());

        atualizarTabela();

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void atualizarTabela() {
        String termo = txtPesquisa.getText();
        String ordem = (String) cbOrdenacao.getSelectedItem();
        tabela.setModel(service.buscarProdutos(termo, ordem));

        if(tabela.getColumnCount() > 0) {
            tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
            tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        }
    }

    // --- NOVO PRODUTO ---
    private void abrirDialogoNovoProduto() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        JTextField txtNome = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtMinimo = new JTextField();

        Vector<String> listaForns = service.getFornecedoresCombo();
        JComboBox<String> cbForn = new JComboBox<>(listaForns);

        panel.add(new JLabel("Nome:")); panel.add(txtNome);
        panel.add(new JLabel("Descrição:")); panel.add(txtDesc);
        panel.add(new JLabel("Preço Unitário (€):")); panel.add(txtPreco);
        panel.add(new JLabel("Stock Mínimo:")); panel.add(txtMinimo);
        panel.add(new JLabel("Fornecedor:")); panel.add(cbForn);

        int result = JOptionPane.showConfirmDialog(null, panel, "Novo Produto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = txtNome.getText();
                String desc = txtDesc.getText();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                int min = Integer.parseInt(txtMinimo.getText());

                String fornStr = (String) cbForn.getSelectedItem();
                if (fornStr == null) { JOptionPane.showMessageDialog(this, "Selecione um fornecedor."); return; }
                int idForn = Integer.parseInt(fornStr.split(" - ")[0]);

                if (service.criarProduto(nome, desc, preco, min, idForn)) {
                    JOptionPane.showMessageDialog(this, "Produto criado!");
                    atualizarTabela();
                    mainApp.atualizarHome();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao criar produto.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // --- EDITAR PRODUTO ---
    private void abrirDialogoEditarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
            return;
        }

        int id = Integer.parseInt(tabela.getValueAt(linha, 0).toString());
        String nomeAtual = (String) tabela.getValueAt(linha, 1);
        String descAtual = (String) tabela.getValueAt(linha, 2);
        String precoString = tabela.getValueAt(linha, 3).toString();
        int minAtual = Integer.parseInt(tabela.getValueAt(linha, 5).toString());
        String nomeFornecedorTabela = (String) tabela.getValueAt(linha, 6);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField txtNome = new JTextField(nomeAtual);
        JTextField txtDesc = new JTextField(descAtual);
        JTextField txtPreco = new JTextField(precoString.replace(",", ".").replace(" €", ""));
        JTextField txtMinimo = new JTextField(String.valueOf(minAtual));

        Vector<String> listaForns = service.getFornecedoresCombo();
        JComboBox<String> cbForn = new JComboBox<>(listaForns);

        for (int i = 0; i < cbForn.getItemCount(); i++) {
            String itemCombo = cbForn.getItemAt(i);
            if (itemCombo.contains(nomeFornecedorTabela)) {
                cbForn.setSelectedIndex(i);
                break;
            }
        }

        panel.add(new JLabel("Nome:")); panel.add(txtNome);
        panel.add(new JLabel("Descrição:")); panel.add(txtDesc);
        panel.add(new JLabel("Preço Unitário (€):")); panel.add(txtPreco);
        panel.add(new JLabel("Stock Mínimo:")); panel.add(txtMinimo);
        panel.add(new JLabel("Fornecedor:")); panel.add(cbForn);

        int result = JOptionPane.showConfirmDialog(null, panel, "Editar Produto #" + id,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = txtNome.getText();
                String desc = txtDesc.getText();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                int min = Integer.parseInt(txtMinimo.getText());

                String fornStr = (String) cbForn.getSelectedItem();
                int idForn = Integer.parseInt(fornStr.split(" - ")[0]);

                if (service.atualizarProduto(id, nome, desc, preco, min, idForn)) {
                    JOptionPane.showMessageDialog(this, "Produto atualizado!");
                    atualizarTabela();
                    mainApp.atualizarHome();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // --- ELIMINAR PRODUTO ---
    private void eliminarProdutoSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        int id = Integer.parseInt(tabela.getValueAt(linha, 0).toString());
        String nome = (String) tabela.getValueAt(linha, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem a certeza que quer apagar: " + nome + "?\n\nATENÇÃO: Isto apagará também todo o histórico de movimentos!",
                "Eliminar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (service.eliminarProduto(id)) {
                JOptionPane.showMessageDialog(this, "Produto eliminado!");
                atualizarTabela();
                mainApp.atualizarHome();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao eliminar.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 5, 5, 5)));
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());

        if (!btn.isEnabled()) {
            btn.setBackground(Color.LIGHT_GRAY);
        }
    }

}