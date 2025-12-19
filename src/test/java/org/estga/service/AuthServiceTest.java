package org.estga.service;

import org.estga.model.Utilizador;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAutenticacao() {
        AuthService service = new AuthService();
        System.out.println("--- Teste: AuthService ---");

        // 1. Testar campos vazios
        System.out.println("A validar bloqueio de campos vazios...");
        Utilizador uVazio = service.autenticar("", "");
        assertNull(uVazio, "Nao deve autenticar campos vazios.");
        System.out.println("SUCESSO: Sistema bloqueou login vazio corretamente.");

        // 2. Testar login real (Assume que existe 'admin' com pass '123' na BD)
        // Ajusta os valores abaixo para um user que exista na tua BD
        System.out.println("A tentar login real com utilizador 'admin'...");
        Utilizador uReal = service.autenticar("admin", "admin123");

        if (uReal != null) {
            System.out.println("SUCESSO: Utilizador '" + uReal.getUsername() + "' autenticado!");
        } else {
            System.out.println("AVISO: Login falhou. Verifica se as credenciais existem na BD UA.");
        }
    }
}