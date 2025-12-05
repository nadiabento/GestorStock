package org.estga.data;

import org.estga.model.Fornecedor; // Usa o pacote do seu modelo Fornecedor
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    /**
     * Busca todos os fornecedores registados na base de dados para popular
     * o campo de seleção na janela de Registro de Entrada.
     * @return Lista de objetos Fornecedor.
     */
    public List<Fornecedor> buscarTodos() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        // Query simples para buscar todos os campos da tabela fornecedor
        String sql = "SELECT id_fornecedor, nome, nif, contacto FROM fornecedor ORDER BY nome";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor(
                        rs.getInt("id_fornecedor"),
                        rs.getString("nome"),
                        rs.getString("nif"),
                        rs.getString("contacto")
                );
                fornecedores.add(fornecedor);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedores: " + e.getMessage());
            // Em caso de erro na DB, retorna lista vazia e imprime erro no console
        }
        return fornecedores;
    }
}