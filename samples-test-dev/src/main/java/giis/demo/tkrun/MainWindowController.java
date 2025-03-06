package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;
import java.sql.SQLException;

public class MainWindowController {
    private MainWindowModel model;
    private MainWindowView view;

    public MainWindowController(MainWindowModel model, MainWindowView view) {
        this.model = model;
        this.view = view;
        initializeController();
    }

    private void initializeController() {
        // Cargar datos iniciales
        loadTableNames();
        
        // Listeners
        view.getCmbTables().addActionListener(e -> SwingUtil.exceptionWrapper(() -> 
            loadTableData(view.getSelectedTable()))
        );
        
        view.getBtnFinancialReport().addActionListener(e -> SwingUtil.exceptionWrapper(() -> 
            openFinancialReport())
        );
        
        view.getBtnInvoiceManagement().addActionListener(e -> SwingUtil.exceptionWrapper(() -> 
            openInvoiceManagement())
        );
    }

    private void loadTableNames() {
        try {
            view.getCmbTables().removeAllItems();
            model.getTableNames().forEach(view.getCmbTables()::addItem);
        } catch (Exception e) {
            SwingUtil.showError("Error loading tables: " + e.getMessage());
        }
    }

    private void loadTableData(String tableName) {
        try {
            view.setTableData(model.getTableData(tableName));
        } catch (SQLException e) {
            SwingUtil.showError("Error loading data: " + e.getMessage());
        }
    }

    private void openFinancialReport() {
        FinancialReportView reportView = new FinancialReportView(view.getFrame());
        FinancialReportModel reportModel = new FinancialReportModel();
        new FinancialReportController(reportModel, reportView);
        reportView.setVisible(true);
    }

    private void openInvoiceManagement() {
        InvoiceView invoiceView = new InvoiceView(view.getFrame());
        InvoiceModel invoiceModel = new InvoiceModel();
        new InvoiceController(invoiceModel, invoiceView);
        
        // Cargar datos necesarios
        invoiceView.loadActivities(model.getActiveActivityIds());
        invoiceView.loadSponsors(model.getActiveSponsorIds());
        invoiceView.refreshInvoices(invoiceModel.getPendingInvoices());
        
        invoiceView.setVisible(true);
    }
}