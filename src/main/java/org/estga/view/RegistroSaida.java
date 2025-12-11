package org.estga.view;

import org.estga.data.ProdutoDAO;
import org.estga.data.UtilizadorDAO;
import org.estga.model.Produto;
import org.estga.model.Utilizador;
import org.estga.service.MovimentoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegistroSaida extends JFrame {

    private JFrame janelaAnterior;
    private JComboBox<Utilizador> cmbFuncionario;
    private JComboBox<Produto> cmbProduto;
    private JTextField txtQuantidade;

    // Instanciar os DAOs
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final UtilizadorDAO utilizadorDAO = new UtilizadorDAO();
    private final MovimentoService movimentoService = new MovimentoService();

    // ID de utilizador padrão para testes (deve existir na tabela 'utilizador')
    private static final int ID_UTILIZADOR_TESTE = 1;

    public RegistroSaida(JFrame janelaAnterior) {
        super("Registro de Saída de Produtos");
        this.janelaAnterior = janelaAnterior;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Inicialização dos componentes
        cmbFuncionario = new JComboBox<>();
        cmbProduto = new JComboBox<>();
        txtQuantidade = new JTextField(10);

        // Carrega os dados da base de dados ao iniciar a janela
        carregarDados();

        // --- Painel de Formulário ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

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
        btnConfirmar.addActionListener(e -> confirmarSaida());

        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnConfirmar);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Carrega os produtos e utilizadores (funcionários) da base de dados.
     */
    private void carregarDados() {
        try {
            // Limpa antes de carregar
            cmbProduto.removeAllItems();
            cmbFuncionario.removeAllItems();

            // Carregar Produtos (com stock atual)
            List<Produto> produtos = produtoDAO.buscarTodosComStock();
            for (Produto p : produtos) {
                cmbProduto.addItem(p);
            }
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado na base de dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

            // Carregar Utilizadores
            List<Utilizador> utilizadores = utilizadorDAO.buscarTodos();
            for (Utilizador u : utilizadores) {
                cmbFuncionario.addItem(u);
            }
            if (utilizadores.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum funcionário encontrado na base de dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            String mensagemAmigavel;

            // Tratamento específico para o erro de conexão nula
            if (e.getMessage() != null && e.getMessage().contains("conn is null")) {
                mensagemAmigavel = "ERRO GRAVE: A ligação à base de dados falhou. Verifique se o servidor da base de dados está ativo e se as credenciais no DBConnection estão corretas.";
            } else {
                mensagemAmigavel = "Erro ao carregar dados: " + e.getMessage();
            }

            JOptionPane.showMessageDialog(this, mensagemAmigavel, "Erro de Configuração ou Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarSaida() {
        Produto produtoSelecionado = (Produto) cmbProduto.getSelectedItem();
        Utilizador funcionarioSelecionado = (Utilizador) cmbFuncionario.getSelectedItem();

        if (produtoSelecionado == null || funcionarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Produto e um Funcionário.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int qtdSaida = Integer.parseInt(txtQuantidade.getText());

            if (qtdSaida <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- Lógica de Validação de Estoque ---
            int estoqueAtual = produtoSelecionado.getStockAtual();

            if (qtdSaida > estoqueAtual) {
                // Exibe o aviso se sair mais do que tem
                JOptionPane.showMessageDialog(this,
                        String.format("AVISO: Stock insuficiente! A quantidade solicitada (%d) excede o stock atual de '%s' (%d).", qtdSaida, produtoSelecionado.getNome(), estoqueAtual),
                        "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
                return; // Impede a confirmação e o registo
            }


            movimentoService.registrarSaida(
                produtoSelecionado.getIdProduto(),
                qtdSaida,
                funcionarioSelecionado.getIdUtilizador()
            );


            JOptionPane.showMessageDialog(this,
                    String.format("Sucesso! Saída de %d unidades de '%s' registada.", qtdSaida, produtoSelecionado.getNome()),
                    "Saída Confirmada", JOptionPane.INFORMATION_MESSAGE);

            txtQuantidade.setText("");
            // Recarrega os dados para atualizar o stock visível no JComboBox após a saída
            carregarDados();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A Quantidade a sair deve ser um número inteiro válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        this.dispose();
        janelaAnterior.setVisible(true);
    }
}