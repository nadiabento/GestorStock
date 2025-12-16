package org.estga.service;

import org.estga.data.DBConnection;
import javax.swing.DefaultComboBoxModel;
import java.sql.*;
import java.util.Vector;

public class MovimentoService {

    // ==================================================================================
    // 1. MÉTODOS PARA A INTERFACE GRÁFICA (PREENCHER COMBOBOXES)
    // ==================================================================================

    /**
     * Devolve uma lista de nomes de produtos para a Combobox.
     */
    public DefaultComboBoxModel<String> getModelProdutos() {
        return getListaNomes("produto");
    }

    /**
     * Devolve uma lista de nomes de fornecedores para a Combobox.
     */
    public DefaultComboBoxModel<String> getModelFornecedores() {
        return getListaNomes("fornecedor");
    }

    /**
     * Devolve uma lista de nomes de clientes para a Combobox.
     */
    public DefaultComboBoxModel<String> getModelClientes() {
        return getListaNomes("cliente");
    }

    // Metodo genérico auxiliar para ir buscar nomes a qualquer tabela
    private DefaultComboBoxModel<String> getListaNomes(String tabela) {
        Vector<String> lista = new Vector<>();
        String sql = "SELECT nome FROM " + tabela + " ORDER BY nome";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new DefaultComboBoxModel<>(lista);
    }

    // ==================================================================================
    // 2. LÓGICA DE REGISTO (TRANSAÇÕES + CONVERSÃO NOME -> ID)
    // ==================================================================================

    /**
     * Regista uma entrada de material.
     * Recebe Strings (da GUI) e converte para IDs internamente.
     */
    public boolean registarEntrada(String nomeProduto, int qtd, String nomeFornecedor, int idUtilizador) {
        if (qtd <= 0) return false;
        // Nota: Enviamos 'ENTRADA' para corresponder ao ENUM da Base de Dados
        return executarMovimento(nomeProduto, qtd, "ENTRADA", nomeFornecedor, null, idUtilizador);
    }

    /**
     * Regista uma saída de material.
     * Verifica se há stock suficiente antes de gravar.
     */
    public boolean registarSaida(String nomeProduto, int qtd, String nomeCliente, int idUtilizador) {
        if (qtd <= 0) return false;
        // Nota: Enviamos 'SAIDA' (sem acento) para corresponder ao ENUM da Base de Dados
        return executarMovimento(nomeProduto, qtd, "SAIDA", null, nomeCliente, idUtilizador);
    }

    /**
     * O MOTOR DO SISTEMA:
     * Este metodo faz tudo numa única transação segura:
     * 1. Descobre os IDs.
     * 2. Verifica Stock (se for saída).
     * 3. Cria o Movimento.
     * 4. Cria a Linha de Movimento.
     * 5. Atualiza o Stock.
     */
    private boolean executarMovimento(String prodNome, int qtd, String tipo, String fornNome, String cliNome, int idUser) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // A. Descobrir os IDs baseados nos Nomes
            int idProd = buscarId(conn, "produto", prodNome);
            if (idProd == 0) {
                System.out.println(" Produto não encontrado: " + prodNome);
                conn.rollback();
                return false;
            }

            // Verificação extra para SAÍDA: Há stock suficiente?
            if (tipo.equals("SAIDA")) { // Importante: Sem acento, como no schema.sql
                if (!verificarStockSuficiente(conn, idProd, qtd)) {
                    System.out.println(" Erro: Stock insuficiente para o produto " + prodNome);
                    conn.rollback();
                    return false;
                }
            }

            // B. Inserir Movimento (Cabeçalho)
            // O id_movimento é gerado automaticamente pelo Auto_Increment
            String sqlMov = "INSERT INTO movimento (tipo_movimento, id_utilizador, data_movimento) VALUES (?, ?, NOW())";
            PreparedStatement stmtMov = conn.prepareStatement(sqlMov, Statement.RETURN_GENERATED_KEYS);
            stmtMov.setString(1, tipo);
            stmtMov.setInt(2, idUser);
            stmtMov.executeUpdate();

            // Recuperar o ID do movimento que acabou de ser criado
            ResultSet rsKeys = stmtMov.getGeneratedKeys();
            int idMov = 0;
            if (rsKeys.next()) {
                idMov = rsKeys.getInt(1);
            } else {
                throw new SQLException("Falha ao criar movimento, nenhum ID obtido.");
            }

            // C. Inserir Linha de Movimento (Detalhes)
            String sqlLinha = "INSERT INTO linha_movimento (id_movimento, id_produto, quantidade) VALUES (?, ?, ?)";
            PreparedStatement stmtLinha = conn.prepareStatement(sqlLinha);
            stmtLinha.setInt(1, idMov);
            stmtLinha.setInt(2, idProd);
            stmtLinha.setInt(3, qtd);
            stmtLinha.executeUpdate();

            // D. Atualizar Tabela de Stock
            String sqlStock;
            if (tipo.equals("ENTRADA")) {
                sqlStock = "UPDATE stock SET quantidade = quantidade + ? WHERE id_produto = ?";
            } else {
                sqlStock = "UPDATE stock SET quantidade = quantidade - ? WHERE id_produto = ?";
            }
            PreparedStatement stmtStock = conn.prepareStatement(sqlStock);
            stmtStock.setInt(1, qtd);
            stmtStock.setInt(2, idProd);
            stmtStock.executeUpdate();

            conn.commit();
            System.out.println(" Movimento registado com sucesso!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ==================================================================================
    // 3. MÉTODOS AUXILIARES SQL (Privados)
    // ==================================================================================

    private int buscarId(Connection conn, String tabela, String nome) throws SQLException {
        if (nome == null) return 0;
        String sql = "SELECT id_" + tabela + " FROM " + tabela + " WHERE nome = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    private boolean verificarStockSuficiente(Connection conn, int idProduto, int qtdSaida) throws SQLException {
        String sql = "SELECT quantidade FROM stock WHERE id_produto = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idProduto);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int stockAtual = rs.getInt("quantidade");
            return stockAtual >= qtdSaida;
        }
        return false;
    }
}