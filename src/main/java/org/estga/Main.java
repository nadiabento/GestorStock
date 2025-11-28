package org.estga;

import org.estga.view.RegistroEntrada;
import org.estga.view.RegistroSaida;
import org.estga.data.DBConnection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class Main extends JFrame {

    public Main() {
        super("Sistema de Gestão de Stock - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Centraliza a janela

        // --- Componentes ---
        JButton btnEntrada = new JButton("Registro de Entrada");
        JButton btnSaida = new JButton("Registro de Saída");

        // --- Eventos ---
        btnEntrada.addActionListener(e -> {
            // Abre a janela de Entrada e esconde esta
            new RegistroEntrada(this).setVisible(true);
            this.setVisible(false);
        });

        btnSaida.addActionListener(e -> {
            // Abre a janela de Saída e esconde esta
            new RegistroSaida(this).setVisible(true);
            this.setVisible(false);
        });

        // --- Layout ---
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        panelBotoes.add(btnEntrada);
        panelBotoes.add(btnSaida);

        add(panelBotoes, BorderLayout.CENTER);
    }

    // METODO MAIN ADICIONADO PARA INICIAR A APLICAÇÃO
    public static void main(String[] args) {
        // Usa SwingUtilities.invokeLater para iniciar a GUI na Event Dispatch Thread (EDT)
        System.out.println("A iniciar Sistema de Gestão de Stock...");
        DBConnection.getConnection();
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });

}}