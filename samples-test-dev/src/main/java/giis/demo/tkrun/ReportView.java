package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportView extends JDialog {
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JComboBox<String> statusCombo;
    private JTable reportTable;
    private JButton btnGenerate;

    public ReportView() {
        initialize();
    }

    private void initialize() {
        setTitle("Reporte Financiero");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con filtros
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Fecha Inicio (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1;
        txtStartDate = new JTextField(10);
        topPanel.add(txtStartDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Fecha Fin (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1;
        txtEndDate = new JTextField(10);
        topPanel.add(txtEndDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"All", "Planned", "In Progress", "Completed"});
        topPanel.add(statusCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        btnGenerate = new JButton("Generar Reporte");
        topPanel.add(btnGenerate, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Tabla para mostrar los resultados
        reportTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Getters para la UI
    public String getStartDate() { return txtStartDate.getText(); }
    public String getEndDate() { return txtEndDate.getText(); }
    public String getStatus() { return (String) statusCombo.getSelectedItem(); }
    public JButton getGenerateButton() { return btnGenerate; }

    // Actualiza la tabla y añade una fila de totales al final
    public void updateTable(List<ReportDTO> data) {
        DefaultTableModel model = new DefaultTableModel(
            new String[] {
                "Actividad", "Fecha Inicio", "Fecha Fin", "Estado", 
                "Est. Acuerdo", "Est. Otros Ingresos", "Est. Otros Gastos", 
                "Ingresos Estimados", "Gastos Estimados", "Balance Estimado", 
                "Ingresos Pagados", "Gastos Pagados", "Balance Pagado"
            }, 0);
        
        // Variables para totales generales
        double totEstAgreement = 0;
        double totEstOtherIncome = 0;
        double totEstOtherExpenses = 0;
        double totEstimatedIncome = 0;
        double totEstimatedExpenses = 0;
        double totEstimatedBalance = 0;
        double totPaidIncome = 0;
        double totPaidExpenses = 0;
        double totPaidBalance = 0;
        
        for (ReportDTO item : data) {
            double estIncome = item.getEstimatedIncome();
            double estExpenses = item.getEstimatedExpenses();
            double estBalance = item.getEstimatedBalance();
            double paidBalance = item.getPaidBalance();
            
            totEstAgreement += item.getTotalEstimatedAgreement();
            totEstOtherIncome += item.getTotalEstimatedOtherIncome();
            totEstOtherExpenses += item.getTotalEstimatedOtherExpenses();
            totEstimatedIncome += estIncome;
            totEstimatedExpenses += estExpenses;
            totEstimatedBalance += estBalance;
            totPaidIncome += item.getTotalPaidIncome();
            totPaidExpenses += item.getTotalPaidExpenses();
            totPaidBalance += paidBalance;
            
            model.addRow(new Object[] {
                item.getEditionTitle(),
                item.getEditionStartDate(),
                item.getEditionEndDate(),
                item.getEditionStatus(),
                String.format("€%.2f", item.getTotalEstimatedAgreement()),
                String.format("€%.2f", item.getTotalEstimatedOtherIncome()),
                String.format("€%.2f", item.getTotalEstimatedOtherExpenses()),
                String.format("€%.2f", estIncome),
                String.format("€%.2f", estExpenses),
                String.format("€%.2f", estBalance),
                String.format("€%.2f", item.getTotalPaidIncome()),
                String.format("€%.2f", item.getTotalPaidExpenses()),
                String.format("€%.2f", paidBalance)
            });
        }
        
        // Añadimos una fila final con los totales
        model.addRow(new Object[] {
            "TOTALES", "", "", "",
            String.format("€%.2f", totEstAgreement),
            String.format("€%.2f", totEstOtherIncome),
            String.format("€%.2f", totEstOtherExpenses),
            String.format("€%.2f", totEstimatedIncome),
            String.format("€%.2f", totEstimatedExpenses),
            String.format("€%.2f", totEstimatedBalance),
            String.format("€%.2f", totPaidIncome),
            String.format("€%.2f", totPaidExpenses),
            String.format("€%.2f", totPaidBalance)
        });
        
        reportTable.setModel(model);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
