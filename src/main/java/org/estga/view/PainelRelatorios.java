package org.estga.view;

import org.estga.service.RelatorioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*; // Mudei para java.io.* para incluir OutputStreamWriter e FileOutputStream
import java.nio.charset.StandardCharsets;

public class PainelRelatorios extends JPanel {

    private JTable tabela;
    private RelatorioService service;

    public PainelRelatorios() {
        this.service = new RelatorioService();
        setLayout(new BorderLayout());

        // --- 1. BOTÕES DE TOPO ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.setBackground(Color.WHITE);

        JButton btnStock = new JButton(" Stock Valorizado");
        JButton btnSemanal = new JButton(" Movimentos (7 Dias)");

        estilizarBotao(btnStock, new Color(70, 130, 180));
        estilizarBotao(btnSemanal, new Color(70, 130, 180));

        btnStock.addActionListener(e -> tabela.setModel(service.getDadosStock()));
        btnSemanal.addActionListener(e -> tabela.setModel(service.getDadosMovimentos(7)));

        painelTopo.add(btnStock);
        painelTopo.add(btnSemanal);

        // --- 2. TABELA CENTRAL ---
        tabela = new JTable();
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // --- 3. BOTÃO DE EXPORTAR ---
        JPanel painelFundo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelFundo.setBackground(Color.WHITE);
        painelFundo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnExportar = new JButton(" Exportar Relatório");
        estilizarBotao(btnExportar, new Color(23, 162, 184));

        btnExportar.addActionListener(e -> abrirDialogoExportacao());

        painelFundo.add(btnExportar);

        add(painelTopo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelFundo, BorderLayout.SOUTH);
    }

    private void abrirDialogoExportacao() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        String[] tipos = {
                "Stock Valorizado (Atual)",
                "Movimentos (Últimos 7 Dias)",
                "Movimentos (Últimos 30 Dias)",
                "Movimentos (Histórico Completo)"
        };
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        String[] formatos = {"CSV (Excel)", "Texto Formatado (.txt)"};
        JComboBox<String> cbFormato = new JComboBox<>(formatos);

        panel.add(new JLabel("Selecione o Relatório:"));
        panel.add(cbTipo);
        panel.add(new JLabel("Formato do ficheiro:"));
        panel.add(cbFormato);

        int result = JOptionPane.showConfirmDialog(this, panel, "Exportar Relatório",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Escolher dados
            DefaultTableModel modelParaExportar;
            int escolha = cbTipo.getSelectedIndex();

            if (escolha == 0) modelParaExportar = service.getDadosStock();
            else if (escolha == 1) modelParaExportar = service.getDadosMovimentos(7);
            else if (escolha == 2) modelParaExportar = service.getDadosMovimentos(30);
            else modelParaExportar = service.getDadosMovimentos(0);

            if (modelParaExportar.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Não há dados para exportar.");
                return;
            }

            // Escolher ficheiro
            boolean isTxt = cbFormato.getSelectedIndex() == 1;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Relatório");

            String nomePadrao = escolha == 0 ? "relatorio_stock" : "relatorio_movimentos";
            String extensao = isTxt ? ".txt" : ".csv";
            fileChooser.setSelectedFile(new File(nomePadrao + extensao));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(extensao)) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + extensao);
                }

                if (isTxt) gravarFicheiroTXT(fileToSave, modelParaExportar);
                else gravarFicheiroCSV(fileToSave, modelParaExportar);
            }
        }
    }

    // --- CSV ---
    private void gravarFicheiroCSV(File file, DefaultTableModel model) {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter fw = new BufferedWriter(osw)
        ) {
            fw.write("\ufeff");

            String SEP = ";";

            // Cabeçalhos
            for (int i = 0; i < model.getColumnCount(); i++) {
                fw.write(model.getColumnName(i) + (i == model.getColumnCount() - 1 ? "" : SEP));
            }
            fw.write("\n");

            // Dados
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object val = model.getValueAt(i, j);
                    String texto = (val != null) ? val.toString() : "";

                    // Limpar quebras de linha que possam partir o CSV
                    texto = texto.replace("\n", " ").replace("\r", " ");

                    if (texto.contains(SEP)) {
                        texto = "\"" + texto + "\"";
                    }

                    fw.write(texto + (j == model.getColumnCount() - 1 ? "" : SEP));
                }
                fw.write("\n");
            }
            JOptionPane.showMessageDialog(this, "CSV (Excel) exportado com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    // --- TXT  ---
    private void gravarFicheiroTXT(File file, DefaultTableModel model) {
        try (FileWriter fw = new FileWriter(file)) {
            int colunas = model.getColumnCount();
            int linhas = model.getRowCount();
            int[] larguras = new int[colunas];

            for (int col = 0; col < colunas; col++) {
                larguras[col] = model.getColumnName(col).length();
                for (int row = 0; row < linhas; row++) {
                    Object val = model.getValueAt(row, col);
                    int len = (val != null) ? val.toString().length() : 0;
                    if (len > larguras[col]) larguras[col] = len;
                }
                larguras[col] += 4;
            }

            StringBuilder separator = new StringBuilder("+");
            for (int w : larguras) separator.append("-".repeat(w)).append("+");
            separator.append("\n");

            fw.write("RELATÓRIO GERADO PELO SISTEMA SGS\n");
            fw.write("Data: " + java.time.LocalDate.now() + "\n\n");
            fw.write(separator.toString());

            fw.write("|");
            for (int col = 0; col < colunas; col++) {
                String fmt = "%-" + larguras[col] + "s";
                fw.write(String.format(fmt, " " + model.getColumnName(col)) + "|");
            }
            fw.write("\n");
            fw.write(separator.toString());

            for (int row = 0; row < linhas; row++) {
                fw.write("|");
                for (int col = 0; col < colunas; col++) {
                    Object val = model.getValueAt(row, col);
                    String texto = (val != null) ? val.toString() : "";
                    String fmt = "%-" + larguras[col] + "s";
                    fw.write(String.format(fmt, " " + texto) + "|");
                }
                fw.write("\n");
            }
            fw.write(separator.toString());
            fw.write("\nTotal de registos: " + linhas);

            JOptionPane.showMessageDialog(this, "Relatório TXT exportado com sucesso!");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFocusPainted(false);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}