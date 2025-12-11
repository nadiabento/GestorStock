package org.estga.service;

import org.estga.data.DBConnection;
import org.estga.data.MovimentoDAO;
import org.estga.data.StockDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class MovimentoService {

    private final MovimentoDAO movimentoDAO;
    private final StockDAO stockDAO;

    public MovimentoService() {
        this.movimentoDAO = new MovimentoDAO();
        this.stockDAO = new StockDAO();
    }

    /**
     * Regista uma entrada de stock. Esta operação é ATÓMICA (Transação de DB).
     * * 1. Abre Conexão e Desliga AutoCommit.
     * 2. Insere Movimento -> Insere LinhaMovimento -> Atualiza Stock.
     * 3. Se tudo OK: COMMIT.
     * 4. Se ERRO: ROLLBACK.
     * idProduto: ID do produto.
     * quantidade: Quantidade a entrar.
     * idFornecedor: ID do fornecedor (não usado na lógica de stock, mas pode ser útil para relatórios).
     * idUtilizador: ID do utilizador que regista.
     * throws Exception Se a quantidade for inválida ou ocorrer um erro de DB (transacional).
     */
    public void registrarEntrada(int idProduto, int quantidade, int idFornecedor, int idUtilizador) throws Exception {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser positiva.");
        }

        Connection conn = null;
        try {
            // 1. Obtém a Conexão e Inicia a Transação
            conn = DBConnection.getConnection();
            if (conn == null) {
                // Se a DBConnection falhar (erro crítico), lança exceção imediatamente
                throw new SQLException("Falha ao obter conexão com a base de dados. Verifique a configuração.");
            }
            conn.setAutoCommit(false); // ATIVA o modo de transação

            // 2. Executa as operações transacionais

            // 2.1. Grava o Movimento principal
            int idMovimento = movimentoDAO.inserirMovimento(conn, "ENTRADA", idUtilizador);

            // 2.2. Grava a Linha de Movimento (detalhe)
            movimentoDAO.inserirLinhaMovimento(conn, idMovimento, idProduto, quantidade);

            // 2.3. Atualiza o Stock (aumenta)
            stockDAO.atualizarStock(conn, idProduto, quantidade);

            // 3. Sucesso: Confirma todas as operações na BD
            conn.commit();

        } catch (SQLException e) {
            // 4. Falha: Desfaz tudo o que foi feito na transação
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rb) {
                    System.err.println("Erro durante o rollback: " + rb.getMessage());
                }
            }
            // Relança uma exceção para a camada View
            throw new Exception("Erro transacional ao registar Entrada. Operações desfeitas: " + e.getMessage());
        } finally {
            // 5. Fecha a Conexão
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura o modo padrão (boa prática)
                    conn.close();
                } catch (SQLException close) {
                    // Ignora, mas imprime erro se o fechamento falhar
                    System.err.println("Aviso: Falha ao fechar a conexão de DB: " + close.getMessage());
                }
            }
        }
    }


    /**
     * Regista uma saída de stock. Esta operação é ATÓMICA (Transação de DB).
     * idProduto: ID do produto.
     * quantidade: Quantidade a sair.
     * idUtilizador: ID do utilizador que regista.
     * throws Exception Se a quantidade for inválida, o stock for insuficiente, ou ocorrer um erro de DB.
     */
    public void registrarSaida(int idProduto, int quantidade, int idUtilizador) throws Exception {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de saída deve ser positiva.");
        }

        // Validação de Stock (OPERAÇÃO DE LEITURA ANTES DA TRANSAÇÃO)
        // É importante que esta validação seja feita ANTES de iniciar a transação
        int stockAtual = stockDAO.consultarQuantidade(idProduto);

        if (quantidade > stockAtual) {
            // Lança uma exceção que será capturada pela View para mostrar a mensagem de stock insuficiente.
            throw new IllegalStateException(String.format("Stock insuficiente! Apenas %d unidades disponíveis.", stockAtual));
        }

        Connection conn = null;
        try {
            // 1. Obtém a Conexão e Inicia a Transação
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Falha ao obter conexão com a base de dados. Verifique a configuração.");
            }
            conn.setAutoCommit(false); // ATIVA o modo de transação

            // 2. Executa as operações transacionais

            // 2.1. Grava o Movimento principal
            int idMovimento = movimentoDAO.inserirMovimento(conn, "SAÍDA", idUtilizador);

            // 2.2. Grava a Linha de Movimento (detalhe)
            movimentoDAO.inserirLinhaMovimento(conn, idMovimento, idProduto, quantidade);

            // 2.3. Atualiza o Stock (diminui)
            // Passamos a quantidade negativa para que o StockDAO faça a subtração
            stockDAO.atualizarStock(conn, idProduto, -quantidade);

            // 3. Sucesso: Confirma todas as operações na BD
            conn.commit();

        } catch (SQLException e) {
            // 4. Falha: Desfaz tudo o que foi feito na transação
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rb) {
                    System.err.println("Erro durante o rollback: " + rb.getMessage());
                }
            }
            // Relança uma exceção para a camada View
            throw new Exception("Erro transacional ao registar Saída. Operações desfeitas: " + e.getMessage());
        } finally {
            // 5. Fecha a Conexão
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura o modo padrão
                    conn.close();
                } catch (SQLException close) {
                    System.err.println("Aviso: Falha ao fechar a conexão de DB: " + close.getMessage());
                }
            }
        }
    }
}