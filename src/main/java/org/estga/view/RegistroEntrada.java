package org.estga.view;

import javax.swing.*;
import java.awt.*;

public class RegistroEntrada extends JFrame {

    private JFrame janelaAnterior; // Armazena a referência para a Main View

    public RegistroEntrada(JFrame janelaAnterior) {
        super("Registro de Entrada de Produtos");
        this.janelaAnterior = janelaAnterior;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Painel de Formulário (Centro) ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Dados de simulação (em produção, viriam de um FornecedorDAO)
        String[] fornecedores = {"Fornecedor Alfa", "Fornecedor Beta"};
        String[] produtos = {"Produto A", "Produto B", "Produto C"};

        // Campos do formulário
        JTextField txtQuantidade = new JTextField(10);
        JComboBox<String> cmbFornecedor = new JComboBox<>(fornecedores);
        JComboBox<String> cmbProduto = new JComboBox<>(produtos);

        formPanel.add(new JLabel("Quantidade:"));
        formPanel.add(txtQuantidade);
        formPanel.add(new JLabel("Fornecedor:"));
        formPanel.add(cmbFornecedor);
        formPanel.add(new JLabel("Produto:"));
        formPanel.add(cmbProduto);

        // --- Painel de Botões (Sul) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnVoltar = new JButton("Voltar à Janela Anterior");
        JButton btnConfirmar = new JButton("Confirmar Entrada");

        // --- Eventos ---
        btnVoltar.addActionListener(e -> voltar());
        btnConfirmar.addActionListener(e -> confirmarEntrada(txtQuantidade, cmbFornecedor, cmbProduto));

        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnConfirmar);

        // Adiciona os painéis ao JFrame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void confirmarEntrada(JTextField quantidade, JComboBox<String> fornecedor, JComboBox<String> produto) {
        try {
            int qtd = Integer.parseInt(quantidade.getText());

            if (qtd <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String forn = (String) fornecedor.getSelectedItem();
            String prod = (String) produto.getSelectedItem();

            // 🚨 PONTO DE INTEGRAÇÃO:
            // Aqui, você chamaria o MovimentoService para:
            // 1. Gravar o movimento de entrada.
            // 2. Atualizar o Stock do produto.

            JOptionPane.showMessageDialog(this,
                    String.format("Sucesso! Entrada de %d unidades de '%s' registada (Fornecedor: %s).", qtd, prod, forn),
                    "Entrada Confirmada", JOptionPane.INFORMATION_MESSAGE);

            // Limpa os campos após o sucesso
            quantidade.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A Quantidade deve ser um número inteiro válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        this.dispose(); // Fecha a janela atual
        janelaAnterior.setVisible(true); // Mostra a janela anterior
    }
}