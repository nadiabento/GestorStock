package org.estga.data;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class MovimentoDAOTest {


    @BeforeEach
    void setUp() {
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void testInserirMovimentoSimples() throws Exception {
        MovimentoDAO dao = new MovimentoDAO(); // Instancia o DAO

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Inicia transação de teste

            // Tenta inserir um movimento (Tipo, ID Utilizador)
            // Nota: Garantimos que o ID do utilizador existe na tua tabela
            int idMov = dao.inserirMovimento(conn, "ENTRADA", 1);

            // Verifica se o ID foi criado
            assertTrue(idMov > 0, "O movimento deve ser criado na base de dados");

            conn.rollback(); // Limpa a base de dados após o teste
            System.out.println("Teste Movimento: Criado ID " + idMov + " e revertido.");
        }
        }
}