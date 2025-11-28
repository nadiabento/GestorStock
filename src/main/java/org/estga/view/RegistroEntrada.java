package org.estga.view;

import org.estga.data.ProdutoDAO;
import org.estga.data.FornecedorDAO;
import org.estga.model.Produto;
import org.estga.model.Fornecedor;
// import org.estga.service.MovimentoService; // Será usado na próxima etapa

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegistroEntrada extends JFrame {

    private JFrame janelaAnterior;
    private JComboBox<Produto> cmbProduto;
    private JComboBox<Fornecedor> cmbFornecedor;
    private JTextField txtQuantidade;

    // Instanciar os DAOs
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    // private final MovimentoService movimentoService = new MovimentoService();

    // ID de utilizador padrão para testes (deve existir na tabela 'utilizador')
    private static final int ID_UTILIZADOR_PADRAO = 1;

    public RegistroEntrada(JFrame janelaAnterior) {
        super("Registro de Entrada de Produtos");
        this.janelaAnterior = janelaAnterior;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Inicialização dos componentes
        txtQuantidade = new JTextField(10);
        cmbProduto = new JComboBox<>();
        cmbFornecedor = new JComboBox<>();

        // Carrega os dados da base de dados ao iniciar a janela
        carregarDados();

        // --- Painel de Formulário ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Produto:"));
        formPanel.add(cmbProduto);
        formPanel.add(new JLabel("Fornecedor:"));
        formPanel.add(cmbFornecedor);
        formPanel.add(new JLabel("Quantidade:"));
        formPanel.add(txtQuantidade);

        // --- Painel de Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnVoltar = new JButton("Voltar à Janela Anterior");
        JButton btnConfirmar = new JButton("Confirmar Entrada");

        // --- Eventos ---
        btnVoltar.addActionListener(e -> voltar());
        btnConfirmar.addActionListener(e -> confirmarEntrada());

        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnConfirmar);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Carrega os produtos e fornecedores da base de dados e popula os JComboBox.
     */
    private void carregarDados() {
        try {
            // Limpa antes de carregar
            cmbProduto.removeAllItems();
            cmbFornecedor.removeAllItems();

            // Carregar Produtos
            List<Produto> produtos = produtoDAO.buscarTodosComStock();
            for (Produto p : produtos) {
                cmbProduto.addItem(p);
            }
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado na base de dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

            // Carregar Fornecedores
            List<Fornecedor> fornecedores = fornecedorDAO.buscarTodos();
            for (Fornecedor f : fornecedores) {
                cmbFornecedor.addItem(f);
            }
            if (fornecedores.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado na base de dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            String mensagemAmigavel;

            // Tratamento amigável para o erro de conexão nula
            if (e.getMessage() != null && e.getMessage().contains("conn is null")) {
                mensagemAmigavel = "ERRO GRAVE: A ligação à base de dados falhou. Verifique se o servidor da base de dados está ativo e se as credenciais no DBConnection estão corretas.";
            } else {
                mensagemAmigavel = "Erro ao carregar dados: " + e.getMessage();
            }

            JOptionPane.showMessageDialog(this, mensagemAmigavel, "Erro de Configuração ou Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarEntrada() {
        Produto produtoSelecionado = (Produto) cmbProduto.getSelectedItem();
        Fornecedor fornecedorSelecionado = (Fornecedor) cmbFornecedor.getSelectedItem();

        if (produtoSelecionado == null || fornecedorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Produto e um Fornecedor.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int qtd = Integer.parseInt(txtQuantidade.getText());

            if (qtd <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 🚀 PONTO DE INTEGRAÇÃO COM SERVICE:
            // Descomentar e usar a linha abaixo quando o MovimentoService estiver pronto
            /*
            movimentoService.registrarEntrada(
                produtoSelecionado.getIdProduto(),
                qtd,
                fornecedorSelecionado.getIdFornecedor(),
                ID_UTILIZADOR_PADRAO
            );
            */

            JOptionPane.showMessageDialog(this,
                    String.format("Sucesso! Entrada de %d unidades de '%s' registada.", qtd, produtoSelecionado.getNome()),
                    "Entrada Confirmada", JOptionPane.INFORMATION_MESSAGE);

            txtQuantidade.setText("");
            // Recarrega os dados para garantir que a lista de produtos (se tiver o stock) seja atualizada.
            carregarDados();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A Quantidade deve ser um número inteiro válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        this.dispose();
        janelaAnterior.setVisible(true);
    }
}