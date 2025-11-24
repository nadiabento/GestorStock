package org.estga.view;

import javax.swing.*;
import java.awt.*;

public class RegistroSaida extends JFrame {

    private JFrame janelaAnterior;

    // Simulação do estoque atual (em produção, viria de um StockDAO/Service)
    // Usamos um valor fixo para testar o aviso de "mais do que tem"
    private static final int ESTOQUE_ATUAL_PRODUTO_A = 50;

    public RegistroSaida(JFrame janelaAnterior) {
        super("Registro de Saída de Produtos");
        this.janelaAnterior = janelaAnterior;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Painel de Formulário (Centro) ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Dados de simulação (em produção, viriam do FuncionárioDAO e ProdutoDAO)
        String[] funcionarios = {"Nádia", "João", "Maria"};
        String[] produtos = {"Produto A", "Produto B", "Produto C"};

        // Campos do formulário
        JComboBox<String> cmbFuncionario = new JComboBox<>(funcionarios);
        JComboBox<String> cmbProduto = new JComboBox<>(produtos);
        JTextField txtQuantidade = new JTextField(10);

        formPanel.add(new JLabel("Funcionário:"));
        formPanel.add(cmbFuncionario);
        formPanel.add(new JLabel("Produto:"));
        formPanel.add(cmbProduto);
        formPanel.add(new JLabel("Quantidade a Sair:"));
        formPanel.add(txtQuantidade);

        // --- Painel de Botões (Sul) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnVoltar = new JButton("Voltar à Janela Anterior");
        JButton btnConfirmar = new JButton("Confirmar Saída");

        // --- Eventos ---
        btnVoltar.addActionListener(e -> voltar());
        btnConfirmar.addActionListener(e -> confirmarSaida(txtQuantidade, cmbProduto, cmbFuncionario));

        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnConfirmar);

        // Adiciona os painéis ao JFrame
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
                // Simula a consulta ao estoque para o Produto A
                estoqueAtual = ESTOQUE_ATUAL_PRODUTO_A;
            } else {
                // Simulação de estoque para outros produtos
                estoqueAtual = 100;
            }

            if (qtdSaida > estoqueAtual) {
                // Exibe o aviso se sair mais do que tem
                JOptionPane.showMessageDialog(this,
                        String.format("AVISO: Stock insuficiente! A quantidade solicitada (%d) excede o stock atual de '%s' (%d).", qtdSaida, prod, estoqueAtual),
                        "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
                return; // Impede a confirmação e o registo
            }

            // 🚨 PONTO DE INTEGRAÇÃO:
            // Aqui, você chamaria o MovimentoService para:
            // 1. Gravar o movimento de saída.
            // 2. Atualizar o Stock do produto (subtrair a quantidade).

            JOptionPane.showMessageDialog(this,
                    String.format("Sucesso! Saída de %d unidades de '%s' registada (Funcionário: %s).", qtdSaida, prod, func),
                    "Saída Confirmada", JOptionPane.INFORMATION_MESSAGE);

            // Limpa os campos após o sucesso
            quantidade.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A Quantidade a sair deve ser um número inteiro válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        this.dispose(); // Fecha a janela atual
        janelaAnterior.setVisible(true); // Mostra a janela anterior
    }
}