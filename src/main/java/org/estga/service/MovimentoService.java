package org.estga.service;

import org.estga.data.MovimentoDAO;
import org.estga.data.StockDAO;
import java.sql.SQLException;

public class MovimentoService {

    private final MovimentoDAO movimentoDAO;
    private final StockDAO stockDAO;

    public MovimentoService() {
        this.movimentoDAO = new MovimentoDAO();
        this.stockDAO = new StockDAO();
    }

    /**
     * Regista uma entrada de stock.
     * @param idProduto ID do produto.
     * @param quantidade Quantidade a entrar.
     * @param idFornecedor ID do fornecedor.
     * @param idUtilizador ID do utilizador que regista.
     * @throws Exception Se a quantidade for inválida ou ocorrer um erro de DB.
     */
    public void registrarEntrada(int idProduto, int quantidade, int idFornecedor, int idUtilizador) throws Exception {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser positiva.");
        }

        try {
            // 1. Grava o Movimento principal
            int idMovimento = movimentoDAO.inserirMovimento("ENTRADA", idUtilizador);

            // 2. Grava a Linha de Movimento (detalhe)
            movimentoDAO.inserirLinhaMovimento(idMovimento, idProduto, quantidade);

            // 3. Atualiza o Stock (aumenta)
            // Usa-se a quantidade positiva
            stockDAO.atualizarStock(idProduto, quantidade);

        } catch (SQLException e) {
            // Tratamento genérico de erro de DB
            throw new Exception("Erro ao registar Entrada na base de dados: " + e.getMessage());
        }
    }

    /**
     * Regista uma saída de stock, verificando a disponibilidade.
     * @param idProduto ID do produto.
     * @param quantidade Quantidade a sair.
     * @param idUtilizador ID do utilizador que regista.
     * @throws Exception Se a quantidade for inválida, o stock for insuficiente, ou ocorrer um erro de DB.
     */
    public void registrarSaida(int idProduto, int quantidade, int idUtilizador) throws Exception {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de saída deve ser positiva.");
        }

        // 1. Verifica o Stock
        int stockAtual = stockDAO.consultarQuantidade(idProduto);

        if (quantidade > stockAtual) {
            throw new IllegalStateException(String.format("Stock insuficiente! Apenas %d unidades disponíveis.", stockAtual));
        }

        try {
            // 2. Grava o Movimento principal
            int idMovimento = movimentoDAO.inserirMovimento("SAÍDA", idUtilizador);

            // 3. Grava a Linha de Movimento (detalhe)
            movimentoDAO.inserirLinhaMovimento(idMovimento, idProduto, quantidade);

            // 4. Atualiza o Stock (diminui)
            // Usa-se a quantidade negativa para subtrair
            stockDAO.atualizarStock(idProduto, -quantidade);

        } catch (SQLException e) {
            // Tratamento genérico de erro de DB
            throw new Exception("Erro ao registar Saída na base de dados: " + e.getMessage());
        }
    }
}