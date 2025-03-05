package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class MainWindowController {
    private MainWindowModel model;
    private MainWindowView view;

    public MainWindowController(MainWindowModel m, MainWindowView v) {
        this.model = m;
        this.view = v;
        initController();
        loadInitialData();
    }

    private void initController() {
        // Cargar datos cuando se selecciona una tabla
        view.getTableComboBox().addActionListener(e -> SwingUtil.exceptionWrapper(() -> loadTableData()));

        // Abrir ventana emergente de reporte financiero
        view.getBtnOpenFinancialReport().addActionListener(e -> SwingUtil.exceptionWrapper(() -> openFinancialReport()));

        // Abrir ventana emergente de gestión de facturas
        view.getBtnOpenInvoiceManagement().addActionListener(e -> SwingUtil.exceptionWrapper(() -> openInvoiceManagement()));
    }

    private void loadInitialData() {
        try {
            // Cargar nombres de las tablas en el ComboBox
            Vector<String> tables = model.getAllTableNames();
            for (String table : tables) {
                view.getTableComboBox().addItem(table);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Error loading table names: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        try {
            String selectedTable = (String) view.getTableComboBox().getSelectedItem();
            ResultSet rs = model.getTableData(selectedTable);
            view.setTableData(rs);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Error loading table data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openFinancialReport() {
        FinancialReportView financialReportView = new FinancialReportView(view.getFrame());
        FinancialReportModel financialModel = new FinancialReportModel();
        new FinancialReportController(financialModel, financialReportView);
        financialReportView.setVisible(true);
    }

    private void openInvoiceManagement() {
        InvoiceView invoiceView = new InvoiceView(view.getFrame());
        InvoiceModel invoiceModel = new InvoiceModel();
        new InvoiceController(invoiceModel, invoiceView);
        invoiceView.setVisible(true);
    }
}