package org.estga.view;

import org.estga.Main;
import org.estga.model.Utilizador;
import org.estga.service.AuthService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private AuthService authService;

    public LoginFrame() {
        super("Login SGS");
        authService = new AuthService();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTitulo = new JLabel("BEM-VINDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 37, 41));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Sistema de Gestão de Stock");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = criarLabel("Utilizador");
        txtUser = new JTextField();
        estilizarCampo(txtUser);

        JLabel lblPass = criarLabel("Password");
        txtPass = new JPasswordField();
        estilizarCampo(txtPass);

        JButton btnEntrar = new JButton("ENTRAR");
        estilizarBotao(btnEntrar);

        btnEntrar.addActionListener(e -> fazerLogin());
        txtPass.addActionListener(e -> fazerLogin());

        panel.add(lblTitulo);
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(40));
        panel.add(lblUser);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtUser);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblPass);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtPass);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnEntrar);

        add(panel);
    }

    private void fazerLogin() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        Utilizador utilizador = authService.autenticar(user, pass);

        if (utilizador != null) {
            this.dispose();
            new Main(utilizador).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Utilizador ou password incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estilizarCampo(JTextField txt) {
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setPreferredSize(new Dimension(300, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 10, 5, 10)));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void estilizarBotao(JButton btn) {
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(new Color(33, 37, 41));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}