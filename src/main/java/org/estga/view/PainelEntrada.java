package org.estga.view;

import org.estga.Main;
import org.estga.service.MovimentoService;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PainelEntrada extends JPanel {

    private JComboBox<String> cbProduto;
    private JComboBox<String> cbFornecedor;
    private JTextField txtQuantidade;
    private MovimentoService service;
    private Main mainApp;

    public PainelEntrada(Main mainApp) {
        this.mainApp = mainApp;
        service = new MovimentoService();

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Registrar Entrada");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(33, 37, 41));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 15, 30, 15);
        add(lblTitulo, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0; add(criarLabel("Produto:"), gbc);
        gbc.gridx = 1;
        cbProduto = new JComboBox<>(service.getModelProdutos());
        estilizarCombo(cbProduto);
        add(cbProduto, gbc);

        gbc.gridy++; gbc.gridx = 0; add(criarLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        txtQuantidade = new JTextField();
        estilizarCampo(txtQuantidade);
        add(txtQuantidade, gbc);

        gbc.gridy++; gbc.gridx = 0; add(criarLabel("Fornecedor:"), gbc);
        gbc.gridx = 1;
        cbFornecedor = new JComboBox<>(service.getModelFornecedores());
        estilizarCombo(cbFornecedor);
        add(cbFornecedor, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);
        JButton btnSalvar = new JButton("CONFIRMAR ENTRADA");
        estilizarBotao(btnSalvar, new Color(40, 167, 69)); // Verde

        btnSalvar.addActionListener(e -> confirmarEntrada());
        add(btnSalvar, gbc);
    }

    private void confirmarEntrada() {
        try {
            String prod = (String) cbProduto.getSelectedItem();
            String forn = (String) cbFornecedor.getSelectedItem();
            String qtdTexto = txtQuantidade.getText();

            if(qtdTexto.isEmpty()) { JOptionPane.showMessageDialog(this, "Insira uma quantidade."); return; }

            int qtd = Integer.parseInt(qtdTexto);
            int idUser = mainApp.getIdUtilizadorAtual();

            if (service.registarEntrada(prod, qtd, forn, idUser)) {
                JOptionPane.showMessageDialog(this, "Entrada registada com sucesso!");
                txtQuantidade.setText("");
                mainApp.atualizarHome();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registar.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser um número.", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }
    private void estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(250, 35));
        txt.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 5, 5, 5)));
    }
    private void estilizarCombo(JComboBox box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setPreferredSize(new Dimension(250, 35));
        box.setBackground(Color.WHITE);
    }
    private void estilizarBotao(JButton btn, Color cor) {
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }
}