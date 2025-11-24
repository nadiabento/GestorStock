package org.estga.data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Dados da ESTGA
    private static final String URL = "jdbc:mysql://estga-dev.ua.pt:3306/PTDA25_BD_01?sslMode=DISABLED&allowPublicKeyRetrieval=true";
    private static final String USER = "PTDA25_01";
    private static final String PASSWORD = "Xdft#345x";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] Conexão ao servidor ESTGA estabelecida com sucesso!");
            return connection;
        } catch (SQLException e) {
            System.err.println("[DB] Erro crítico na conexão: " + e.getMessage());
            return null;
        }
    }
}