package org.estga.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MovimentoTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDadosMovimento() {
        System.out.println("A iniciar teste: Atribuicao de dados de Movimento...");

        LocalDate hoje = LocalDate.now();
        Movimento m = new Movimento(hoje, "SAIDA", 10, 5, 1);

        assertEquals("SAIDA", m.getTipo());
        assertEquals(5, m.getQuantidade());

        System.out.println("SUCESSO: Movimento de " + m.getTipo() + " com quantidade " + m.getQuantidade() + " validado.");
    }
}