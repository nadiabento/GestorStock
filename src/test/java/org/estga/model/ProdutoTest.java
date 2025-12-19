package org.estga.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;


class ProdutoTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCriacaoProduto() {
        System.out.println("A iniciar teste: Criacao de Produto...");

        Produto p = new Produto(1, "Monitor", "24 polegadas", new BigDecimal("150.00"), 5, 10, 20);

        assertEquals("Monitor", p.getNome());
        assertEquals(new BigDecimal("150.00"), p.getPrecoUnitario());

        // Se chegar aqui, o teste passou
        System.out.println("SUCESSO: Produto '" + p.getNome() + "' criado e validado corretamente!");
    }
}