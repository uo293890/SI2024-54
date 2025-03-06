package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MainWindowView {
    private JFrame frame;
    private JComboBox<String> cmbTables;
    private JTable mainTable;
    private JButton btnFinancialReport;
    private JButton btnInvoiceManagement;

    public MainWindowView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Event Management System");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        cmbTables = new JComboBox<>();
        topPanel.add(new JLabel("Select Table:"));
        topPanel.add(cmbTables);
        
        btnFinancialReport = new JButton("Financial Report");
        topPanel.add(btnFinancialReport);
        
        btnInvoiceManagement = new JButton("Invoice Management");
        topPanel.add(btnInvoiceManagement);

        // Tabla principal
        mainTable = new JTable();
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(mainTable), BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void setTableData(ResultSet rs) throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        ResultSetMetaData metaData = rs.getMetaData();
        
        // Añadir columnas
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(metaData.getColumnName(i));
        }
        
        // Añadir filas
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }
        
        mainTable.setModel(model);
    }

    // Getters
    public JFrame getFrame() { return frame; }
    public JComboBox<String> getCmbTables() { return cmbTables; }
    public String getSelectedTable() { return (String) cmbTables.getSelectedItem(); }
    public JButton getBtnFinancialReport() { return btnFinancialReport; }
    public JButton getBtnInvoiceManagement() { return btnInvoiceManagement; }
}