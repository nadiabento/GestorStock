package org.estga;

import org.estga.data.DBConnection; // Importa a tua classe

public class Main {
    public static void main(String[] args) {
        System.out.println("A iniciar Sistema de Gestão de Stock...");

        DBConnection.getConnection();
    }
}