package org.estga.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovimentoServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testValidacaoStockInsuficiente() {
        MovimentoService service = new MovimentoService();
        System.out.println("--- Teste: MovimentoService (Saida Sem Stock) ---");

        // Tentamos tirar uma quantidade absurda (ex: 999999) de um produto
        // Ajusta "Teclado" para um nome de produto que tenhas na BD
        String nomeProd = "Teclado";
        int qtdImpossivel = 999999;

        System.out.println("A tentar registar saida de " + qtdImpossivel + " unidades de '" + nomeProd + "'...");

        boolean sucesso = service.registarSaida(nomeProd, qtdImpossivel, "Cliente Teste", 1);

        // O serviço DEVE retornar false porque o stock e insuficiente
        assertFalse(sucesso, "O servico nao deveria permitir a saida sem stock.");

        System.out.println("SUCESSO: O MovimentoService impediu a saida ilegal de stock!");
    }
}