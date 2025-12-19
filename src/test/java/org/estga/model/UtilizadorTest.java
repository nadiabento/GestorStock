package org.estga.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class UtilizadorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testToStringUtilizador() {
        System.out.println("A iniciar teste: Formatacao de Utilizador (toString)...");

        Utilizador u = new Utilizador();
        u.setUsername("admin");
        u.setPerfil("ADMINISTRADOR");

        String esperado = "admin (ADMINISTRADOR)";
        assertEquals(esperado, u.toString());

        System.out.println("SUCESSO: O formato do Utilizador esta correto: " + u.toString());
    }
}