package org.estga.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.estga.model.Produto;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoDAOTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testInserirProdutoSimples() throws Exception {
        ProdutoDAO dao = new ProdutoDAO(); // Instancia o DAO

        // Abre a ligação real para podermos fazer rollback
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Impede que a gravação seja permanente

            // Cria um produto fictício para o teste
            Produto p = new Produto();
            p.setNome("TESTE JUNIT");
            p.setPrecoUnitario(new BigDecimal("10.00"));
            p.setIdFornecedor(1); // Assume que o ID 1 existe

            // Tenta inserir e recebe o ID gerado
            int id = dao.inserir(conn, p);

            // Se o ID for maior que 0, a base de dados aceitou o comando
            assertTrue(id > 0, "O produto deve ser inserido com sucesso");

            conn.rollback(); // APAGA o produto de teste da base de dados
            System.out.println("Teste Produto: Inserção e Rollback concluídos.");
        }
    }
}