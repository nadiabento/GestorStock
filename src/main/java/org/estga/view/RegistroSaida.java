package org.estga.view;

import javax.swing.*;
import java.awt.*;

public class RegistroSaida extends JFrame {

    private JFrame janelaAnterior;

    // Simulação do estoque atual (para o teste de stock)
    private static final int ESTOQUE_ATUAL_PRODUTO_A = 50;

    public RegistroSaida(JFrame janelaAnterior) {
        super("Registro de Saída de Produtos");
        this.janelaAnterior = janelaAnterior;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Dados de simulação
        String[] funcionarios = {"Nádia", "João", "Maria"};
        String[] produtos = {"Produto A", "Produto B", "Produto C"};

        // --- Painel de Formulário ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JComboBox<String> cmbFuncionario = new JComboBox<>(funcionarios);
        JComboBox<String> cmbProduto = new JComboBox<>(produtos);
        JTextField txtQuantidade = new JTextField(10);

        formPanel.add(new JLabel("Funcionário:"));
        formPanel.add(cmbFuncionario);
        formPanel.add(new JLabel("Produto:"));
        formPanel.add(cmbProduto);
        formPanel.add(new JLabel("Quantidade a Sair:"));
        formPanel.add(txtQuantidade);

        // --- Painel de Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnVoltar = new JButton("Voltar à Janela Anterior");
        JButton btnConfirmar = new JButton("Confirmar Saída");

        // --- Eventos ---
        btnVoltar.addActionListener(e -> voltar());
        btnConfirmar.addActionListener(e -> confirmarSaida(txtQuantidade, cmbProduto, cmbFuncionario));

        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnConfirmar);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void confirmarSaida(JTextField quantidade, JComboBox<String> produto, JComboBox<String> funcionario) {
        try {
            int qtdSaida = Integer.parseInt(quantidade.getText());
            String prod = (String) produto.getSelectedItem();
            String func = (String) funcionario.getSelectedItem();

            if (qtdSaida <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- Lógica de Validação de Estoque (Requisito) ---
            int estoqueAtual = 0;
            if (prod.equals("Produto A")) {
                estoqueAtual = ESTOQUE_ATUAL_PRODUTO_A;
            } else {
                estoqueAtual = 100; // Simulação de estoque suficiente para os outros
            }

            if (qtdSaida > estoqueAtual) {
                // Exibe o aviso se sair mais do que tem
                JOptionPane.showMessageDialog(this,
                        String.format("AVISO: Stock insuficiente! A quantidade solicitada (%d) excede o stock atual de '%s' (%d).", qtdSaida, prod, estoqueAtual),
                        "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // SIMULAÇÃO: Sem objeto Movimento. Apenas mostra o que seria feito.
            JOptionPane.showMessageDialog(this,
                    String.format("Sucesso! (Simulado) Saída de %d unidades de '%s' registada (Funcionário: %s).", qtdSaida, prod, func),
                    "Saída Confirmada", JOptionPane.INFORMATION_MESSAGE);

            quantidade.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A Quantidade a sair deve ser um número inteiro válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        this.dispose();
        janelaAnterior.setVisible(true);
    }
}