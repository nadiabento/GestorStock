package org.estga.service;

import org.estga.data.*;
import org.estga.model.*;
import javax.swing.DefaultComboBoxModel;
import java.sql.Connection;
import java.util.Vector;

public class MovimentoService {
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private MovimentoDAO movimentoDAO = new MovimentoDAO();
    private StockDAO stockDAO = new StockDAO();

    public DefaultComboBoxModel<String> getModelProdutos() {
        Vector<String> v = new Vector<>();
        for(Produto p : produtoDAO.buscarTodosComStock()) v.add(p.getNome());
        return new DefaultComboBoxModel<>(v);
    }

    public DefaultComboBoxModel<String> getModelFornecedores() {
        Vector<String> v = new Vector<>();
        for(Fornecedor f : fornecedorDAO.buscarTodos()) v.add(f.getNome());
        return new DefaultComboBoxModel<>(v);
    }

    public DefaultComboBoxModel<String> getModelClientes() {
        Vector<String> v = new Vector<>();
        for(Cliente c : clienteDAO.buscarTodos()) v.add(c.getNome());
        return new DefaultComboBoxModel<>(v);
    }

    public boolean registarEntrada(String nomeProd, int qtd, String nomeForn, int idUser) {
        return processar(nomeProd, qtd, idUser, "ENTRADA");
    }

    public boolean registarSaida(String nomeProd, int qtd, String nomeCli, int idUser) {
        // Verificar stock antes
        int idProd = getIdByName(nomeProd);
        if(stockDAO.consultarQuantidade(idProd) < qtd) return false;
        return processar(nomeProd, qtd, idUser, "SAIDA");
    }

    private boolean processar(String nomeProd, int qtd, int idUser, String tipo) {
        int idProd = getIdByName(nomeProd);
        if(idProd == -1) return false;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            int idMov = movimentoDAO.inserirMovimento(conn, tipo, idUser);
            movimentoDAO.inserirLinhaMovimento(conn, idMov, idProd, qtd);
            stockDAO.atualizarStockTransacional(conn, idProd, qtd, tipo.equals("ENTRADA"));
            conn.commit();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Helper
    private int getIdByName(String nome) {
        for(Produto p : produtoDAO.buscarTodosComStock()) if(p.getNome().equals(nome)) return p.getIdProduto(); // Ajusta getId()
        return -1;
    }
}