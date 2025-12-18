package org.estga.service;

import org.estga.data.*;
import org.estga.model.Fornecedor;
import org.estga.model.Produto;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class ProdutoService {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private StockDAO stockDAO = new StockDAO();
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private MovimentoDAO movimentoDAO = new MovimentoDAO();

    // --- 1. LISTAR PRODUTOS ---
    public DefaultTableModel buscarProdutos(String termoPesquisa, String ordenacao) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Descrição");
        modelo.addColumn("Preço");
        modelo.addColumn("Stock");
        modelo.addColumn("Mínimo");
        modelo.addColumn("Fornecedor");

        // 1. Buscar dados
        List<Produto> listaProdutos = produtoDAO.buscarTodosComStock();
        List<Fornecedor> listaFornecedores = fornecedorDAO.buscarTodos();

        // 2. Aplicar ordenação
        if (ordenacao != null) {
            switch (ordenacao) {
                case "Menor Stock" -> listaProdutos.sort(Comparator.comparingInt(Produto::getStockAtual));
                case "Maior Stock" -> listaProdutos.sort((p1, p2) -> Integer.compare(p2.getStockAtual(), p1.getStockAtual()));
                case "Preço Menor" -> listaProdutos.sort(Comparator.comparing(Produto::getPrecoUnitario));
                case "Preço Maior" -> listaProdutos.sort((p1, p2) -> p2.getPrecoUnitario().compareTo(p1.getPrecoUnitario()));
            }
        }

        // 3. Preencher Tabela
        for (Produto p : listaProdutos) {
            // Filtro de Pesquisa (Nome)
            if (!termoPesquisa.isEmpty() && !p.getNome().toLowerCase().contains(termoPesquisa.toLowerCase())) {
                continue;
            }

            Vector<Object> linha = new Vector<>();
            linha.add(p.getIdProduto());
            linha.add(p.getNome());
            linha.add(p.getDescricao());
            linha.add(String.format("%.2f €", p.getPrecoUnitario()));
            linha.add(p.getStockAtual());
            linha.add(p.getStockMinimo());

            String nomeFornecedor = "Desconhecido";
            for (Fornecedor f : listaFornecedores) {
                if (f.getIdFornecedor() == p.getIdFornecedor()) {
                    nomeFornecedor = f.getNome();
                    break;
                }
            }
            linha.add(nomeFornecedor);
            modelo.addRow(linha);
        }
        return modelo;
    }

    // --- 2. CRIAR PRODUTO ---
    public boolean criarProduto(String nome, String desc, double preco, int minimo, int idFornecedor) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            Produto p = new Produto();
            p.setNome(nome);
            p.setDescricao(desc);
            p.setPrecoUnitario(BigDecimal.valueOf(preco));
            p.setStockMinimo(minimo);
            p.setIdFornecedor(idFornecedor);

            int idNovo = produtoDAO.inserir(conn, p);
            stockDAO.atualizarStock(conn, idNovo, 0);

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 3. ATUALIZAR PRODUTO ---
    public boolean atualizarProduto(int id, String nome, String desc, double preco, int minimo, int idFornecedor) {
        try (Connection conn = DBConnection.getConnection()) {
            Produto p = new Produto();
            p.setIdProduto(id);
            p.setNome(nome);
            p.setDescricao(desc);
            p.setPrecoUnitario(BigDecimal.valueOf(preco));
            p.setStockMinimo(minimo);
            p.setIdFornecedor(idFornecedor);

            return produtoDAO.atualizar(conn, p);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 4. ELIMINAR PRODUTO ---
    public boolean eliminarProduto(int idProduto) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            movimentoDAO.eliminarPorProduto(conn, idProduto);
            stockDAO.eliminarPorProduto(conn, idProduto);
            produtoDAO.eliminar(conn, idProduto);

            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                Connection conn = DBConnection.getConnection();
                if (conn != null) conn.rollback();
            } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        }
    }

    // --- HELPER ---
    public Vector<String> getFornecedoresCombo() {
        Vector<String> lista = new Vector<>();
        for (Fornecedor f : fornecedorDAO.buscarTodos()) {
            lista.add(f.getIdFornecedor() + " - " + f.getNome());
        }
        return lista;
    }
}