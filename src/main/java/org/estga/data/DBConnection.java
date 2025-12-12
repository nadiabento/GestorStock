package org.estga.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Dados de acesso à ESTGA
    private static final String URL = "jdbc:mysql://estga-dev.ua.pt:3306/PTDA25_BD_01?sslMode=DISABLED&allowPublicKeyRetrieval=true";
    private static final String USER = "PTDA25_01";
    private static final String PASSWORD = "Xdft#345x";

    public static Connection getConnection() {
        try {
            // --- A LINHA MÁGICA QUE RESOLVE O ERRO ---
            // Isto obriga o programa a usar a biblioteca do MySQL que baixaste
            Class.forName("com.mysql.cj.jdbc.Driver");
            // -----------------------------------------

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] Sucesso: Ligado à base de dados!");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("[DB] ERRO CRÍTICO: O Driver MySQL não foi encontrado!");
            System.err.println("Verifica se adicionaste a biblioteca no Project Structure > Modules > Dependencies");
            return null;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
}