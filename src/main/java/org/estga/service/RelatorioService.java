package org.estga.service;

import org.estga.data.RelatorioDAO;
import javax.swing.table.DefaultTableModel;

public class RelatorioService {

    private RelatorioDAO relatorioDAO = new RelatorioDAO();

    // --- STOCK VALORIZADO ---
    public DefaultTableModel getDadosStock() {
        return relatorioDAO.buscarStockValorizado();
    }

    // --- MOVIMENTOS ---
    public DefaultTableModel getDadosMovimentos(int dias) {
        return relatorioDAO.buscarMovimentos(dias);
    }
}