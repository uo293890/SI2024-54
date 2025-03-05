package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import giis.demo.util.TableColumnAdjuster;

import java.awt.*;
import java.text.NumberFormat;

public class FinancialReportView extends JDialog {
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JComboBox<String> statusComboBox;
    private JButton btnGenerateReport;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public FinancialReportView(JFrame parent) {
        super(parent, "Financial Report", true); // true para modal
        initialize();
    }

    private void initialize() {
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        // Panel de filtros
        JPanel filterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        filterPanel.add(new JLabel("Start Date:"));
        txtStartDate = new JTextField();
        filterPanel.add(txtStartDate);

        filterPanel.add(new JLabel("End Date:"));
        txtEndDate = new JTextField();
        filterPanel.add(txtEndDate);

        filterPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Planned", "Completed", "All"});
        filterPanel.add(statusComboBox);

        btnGenerateReport = new JButton("Generate Report");
        filterPanel.add(btnGenerateReport);

        add(filterPanel, BorderLayout.NORTH);

        // Tabla de resultados
        tableModel = new DefaultTableModel(
            new Object[]{"Activity", "Start Date", "End Date", "Status", "Income", "Expenses", "Balance"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Centrar la ventana
    }

    public JButton getBtnGenerateReport() {
        return btnGenerateReport;
    }

    public String getStartDate() {
        return txtStartDate.getText();
    }

    public String getEndDate() {
        return txtEndDate.getText();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }

    public void displayReport(FinancialReportDTO report) {
        // Limpiar la tabla antes de agregar nuevos datos
        tableModel.setRowCount(0);

        NumberFormat currency = NumberFormat.getCurrencyInstance();

        for (ActivityDTO activity : report.getActivities()) {
            tableModel.addRow(new Object[]{
                activity.getName(),
                activity.getStartDate(),
                activity.getEndDate(),
                activity.getStatus(),
                currency.format(activity.getIncome()),
                currency.format(activity.getExpenses()),
                currency.format(activity.getBalance())
            });
        }

        // Totales
        tableModel.addRow(new Object[]{"TOTAL", "", "", "",
            currency.format(report.getTotalIncome()),
            currency.format(report.getTotalExpenses()),
            currency.format(report.getTotalIncome() - report.getTotalExpenses())
        });

        // Ajustar el ancho de las columnas automáticamente
        new TableColumnAdjuster(reportTable).adjustColumns();
    }
}