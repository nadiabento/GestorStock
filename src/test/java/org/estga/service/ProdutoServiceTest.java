package org.estga.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testPesquisaProdutos() {
        ProdutoService service = new ProdutoService();
        System.out.println("--- Teste: ProdutoService (Pesquisa) ---");

        // Pesquisar por um termo que sabes que existe (ex: "A")
        String termo = "A";
        System.out.println("A pesquisar produtos que contenham '" + termo + "'...");

        DefaultTableModel modelo = service.buscarProdutos(termo, "Menor Stock");

        assertNotNull(modelo, "O modelo da tabela nao deve ser nulo.");

        System.out.println("SUCESSO: Pesquisa concluida. Linhas encontradas: " + modelo.getRowCount());
    }
}