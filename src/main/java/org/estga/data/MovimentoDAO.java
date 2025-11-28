package org.estga.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovimentoDAO {

    /**
     * Insere um novo registo na tabela 'movimento'.
     * tipo Tipo de movimento ('ENTRADA' ou 'SAÍDA').
     * idUtilizador ID do utilizador responsável.
     * O ID gerado para o novo movimento.
     * SQLException Em caso de erro de DB.
     */
    public int inserirMovimento(String tipo, int idUtilizador) throws SQLException {
        String sql = "INSERT INTO movimento (tipo_movimento, id_utilizador) VALUES (?, ?)";
        int idMovimento = -1;

        // Usa Statement.RETURN_GENERATED_KEYS para obter o ID gerado.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tipo);
            stmt.setInt(2, idUtilizador);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idMovimento = rs.getInt(1);
                }
            }
        }
        if (idMovimento == -1) {
            throw new SQLException("Falha ao obter o ID do movimento gerado.");
        }
        return idMovimento;
    }

    /**
     * Insere uma linha de detalhe na tabela 'linha_movimento'.
     * idMovimento ID do movimento principal.
     * idProduto ID do produto envolvido.
     * quantidade Quantidade que entrou ou saiu.
     * SQLException Em caso de erro de DB.
     */
    public void inserirLinhaMovimento(int idMovimento, int idProduto, int quantidade) throws SQLException {
        // NOTA: Assumindo que a coluna 'quantidade' foi adicionada à sua tabela linha_movimento.
        String sql = "INSERT INTO linha_movimento (id_movimento, id_produto, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMovimento);
            stmt.setInt(2, idProduto);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
        }
    }
}