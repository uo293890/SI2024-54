package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class MainWindowView {
    private JFrame frame;
    private JComboBox<String> tableComboBox;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JButton btnOpenFinancialReport;
    private JButton btnOpenInvoiceManagement;

    public MainWindowView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Main Window");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel superior con ComboBox y botones
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // ComboBox para seleccionar la tabla
        tableComboBox = new JComboBox<>();
        topPanel.add(new JLabel("Select Table:"));
        topPanel.add(tableComboBox);

        // Botón para abrir el reporte financiero
        btnOpenFinancialReport = new JButton("Open Financial Report");
        topPanel.add(btnOpenFinancialReport);

        // Botón para abrir la gestión de facturas
        btnOpenInvoiceManagement = new JButton("Open Invoice Management");
        topPanel.add(btnOpenInvoiceManagement);

        frame.add(topPanel, BorderLayout.NORTH);

        // Tabla para mostrar datos
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public JComboBox<String> getTableComboBox() {
        return tableComboBox;
    }

    public JButton getBtnOpenFinancialReport() {
        return btnOpenFinancialReport;
    }

    public JButton getBtnOpenInvoiceManagement() {
        return btnOpenInvoiceManagement;
    }

    public void setTableData(ResultSet rs) throws SQLException {
        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Obtener los nombres de las columnas
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        tableModel.setColumnIdentifiers(columnNames);

        // Obtener los datos de las filas
        while (rs.next()) {
            Vector<Object> rowData = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.add(rs.getObject(i));
            }
            tableModel.addRow(rowData);
        }
    }

    public JFrame getFrame() {
        return frame;
    }
}