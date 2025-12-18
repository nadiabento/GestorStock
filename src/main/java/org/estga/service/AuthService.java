package org.estga.service;

import org.estga.data.UtilizadorDAO;
import org.estga.model.Utilizador;

public class AuthService {

    private UtilizadorDAO utilizadorDAO = new UtilizadorDAO();

    public Utilizador autenticar(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        return utilizadorDAO.autenticar(username, password);
    }
}