package org.estga.service;

import org.estga.data.DashboardDAO;
import javax.swing.table.DefaultTableModel;

public class HomeService {

    private DashboardDAO dashboardDAO = new DashboardDAO();

    public int getTotalProdutos() {
        return dashboardDAO.getTotalProdutos();
    }

    public int getAlertasCount() {
        return dashboardDAO.getTotalAlertas();
    }

    public double getValorStock() {
        return dashboardDAO.getValorTotalStock();
    }

    public DefaultTableModel getModelTabela() {
        return dashboardDAO.getTabelaAlertas();
    }
}