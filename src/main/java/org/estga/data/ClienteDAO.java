package org.estga.data;
import org.estga.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public List<Cliente> buscarTodos() {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT id_cliente, nome FROM cliente ORDER BY nome")) {
            while (rs.next()) lista.add(new Cliente(rs.getInt("id_cliente"), rs.getString("nome")));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}