package org.estga.view;

import org.estga.service.AuthService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JPanel panel1;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnEntrar;

    public Login() {
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }

    private void realizarLogin() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());

        AuthService auth = new AuthService();
        String perfil = auth.autenticar(username, password);

        if (perfil != null) {
            System.out.println("Login OK! Perfil: " + perfil);
            abrirDashboard(perfil);
        } else {
            JOptionPane.showMessageDialog(panel1,
                    "Login falhou!\nVerifica o utilizador, a password ou a tua conexão à internet.",
                    "Erro de Acesso",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDashboard(String perfil) {
        SwingUtilities.getWindowAncestor(panel1).dispose();

        JFrame frameDash = new JFrame("SGS - Dashboard (" + perfil + ")");

        frameDash.setContentPane(new Dashboard(perfil).getPanel());

        frameDash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameDash.setSize(900, 600);
        frameDash.setLocationRelativeTo(null); // Centra no ecrã
        frameDash.setVisible(true);
    }

    public JPanel getPanel() {
        return panel1;
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login SGS");
        frame.setContentPane(new Login().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}