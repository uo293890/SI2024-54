package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportView extends JFrame {

    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JComboBox<String> statusCombo;
    private JButton btnConsultar;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ReportView() {
        setTitle("Income and Expenses Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Main panel with padding, similar to the HTML container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Centered title
        JLabel titleLabel = new JLabel("Income and Expenses Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Filter panel (mimicking the HTML filter section)
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(new TitledBorder("Filters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Filter: Start date
        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Start Date (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        txtStartDate = new JTextField(10);
        filterPanel.add(txtStartDate, gbc);

        // Filter: End date
        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("End Date (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        txtEndDate = new JTextField(10);
        filterPanel.add(txtEndDate, gbc);

        // Filter: Activity status
        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(new JLabel("Activity Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"All", "Active", "Completed", "Cancelled"});
        filterPanel.add(statusCombo, gbc);

        // Button to consult (similar to the "Consultar" button in HTML)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        btnConsultar = new JButton("Consult");
        filterPanel.add(btnConsultar, gbc);

        // Position the filter panel to the left
        mainPanel.add(filterPanel, BorderLayout.WEST);

        // Table to display the report (mimicking the HTML table)
        tableModel = new DefaultTableModel(
            new Object[]{"Date", "Name", "Status", "Income", "Expenses", "Balance"}, 0);
        reportTable = new JTable(tableModel);
        reportTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(new TitledBorder("Activity Details"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }


    // Getters para recoger los valores de los filtros
    public String getStartDate() {
        return txtStartDate.getText();
    }

    public String getEndDate() {
        return txtEndDate.getText();
    }

    public String getStatus() {
        return (String) statusCombo.getSelectedItem();
    }

    public JButton getConsultarButton() {
        return btnConsultar;
    }

    /**
     * Actualiza la tabla con los datos del reporte y agrega una fila final con los totales.
     * Se adapta la información del ReportDTO para mostrar solo las columnas esenciales.
     */
    public void updateTable(List<ReportDTO> data) {
        // Se limpia la tabla
        tableModel.setRowCount(0);

        // Variables para acumular totales
        double totalIngresos = 0;
        double totalGastos = 0;
        double totalBalance = 0;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        for (ReportDTO item : data) {
            // Se combinan las fechas de inicio y fin (si existe)
            String fechas = sdf.format(item.getEditionStartDate());
            if (item.getEditionEndDate() != null) {
                fechas += " - " + sdf.format(item.getEditionEndDate());
            }
            String nombre = item.getEditionTitle();
            String estado = item.getEditionStatus();

            // Para este ejemplo se toman como "Ingresos" el total estimado de ingresos (acumulando acuerdos y otros ingresos)
            // y como "Gastos" el total estimado de otros gastos.
            double ingresos = item.getEstimatedIncome();
            double gastos = item.getEstimatedExpenses();
            double balance = ingresos - gastos;

            totalIngresos += ingresos;
            totalGastos += gastos;
            totalBalance += balance;

            tableModel.addRow(new Object[]{
                fechas,
                nombre,
                estado,
                String.format("€%.2f", ingresos),
                String.format("€%.2f", gastos),
                String.format("€%.2f", balance)
            });
        }

        // Fila de totales
        tableModel.addRow(new Object[]{
            "TOTALS", "", "",
            String.format("€%.2f", totalIngresos),
            String.format("€%.2f", totalGastos),
            String.format("€%.2f", totalBalance)
        });
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
