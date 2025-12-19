package org.estga.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.estga.model.Cliente;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class ClienteDAOTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testListarClientes() {
        ClienteDAO dao = new ClienteDAO(); // Instancia o DAO

        // Chama o metodo que faz o SELECT na BD
        List<Cliente> lista = dao.buscarTodos();

        // Verifica se o resultado não é nulo (a BD respondeu)
        assertNotNull(lista, "A lista deve ser devolvida (mesmo que vazia)");
        System.out.println("Teste Cliente: Encontrados " + lista.size() + " clientes.");
    }
}