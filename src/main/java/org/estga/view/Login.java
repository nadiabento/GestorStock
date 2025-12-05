package org.estga.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JPanel panel1;
    private JTextField txtUser;
    private JButton btnEntrar;
    private JPasswordField txtPass;

    public Login() {
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = txtUser.getText();
                String password = new String(txtPass.getPassword());


                if (username.equals("admin") && password.equals("1234")) {

                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                    loginFrame.dispose();


                    JFrame frameDash = new JFrame("SGS - Dashboard");
                    frameDash.setContentPane(new Dashboard().getPanel());
                    frameDash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameDash.setSize(900, 600);
                    frameDash.setLocationRelativeTo(null); // Centra no ecrã
                    frameDash.setVisible(true);

                } else {

                    JOptionPane.showMessageDialog(null,
                            "Dados incorretos!",
                            "Erro de Acesso",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login SGS");
        frame.setContentPane(new Login().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public JPanel getPanel() {
        return panel1;
    }
}