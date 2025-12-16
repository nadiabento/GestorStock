package org.estga.view;

import org.estga.service.RelatorioService;
import javax.swing.*;
import java.awt.*;

public class PainelRelatorios extends JPanel {

    private JTable tabela;
    private RelatorioService service;

    public PainelRelatorios() {
        this.service = new RelatorioService();
        setLayout(new BorderLayout());

        // Tabela
        tabela = new JTable();
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabela);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.setBackground(Color.WHITE);

        JButton btnStock = new JButton("Stock Valorizado");
        JButton btnSemanal = new JButton("Movimentos (7 Dias)");

        estilizarBotao(btnStock);
        estilizarBotao(btnSemanal);

        btnStock.addActionListener(e -> tabela.setModel(service.getDadosStock()));
        btnSemanal.addActionListener(e -> tabela.setModel(service.getDadosSemanal()));

        painelBotoes.add(btnStock);
        painelBotoes.add(btnSemanal);

        add(painelBotoes, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void estilizarBotao(JButton btn) {
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}