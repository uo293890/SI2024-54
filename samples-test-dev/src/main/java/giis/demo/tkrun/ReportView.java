package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportView extends JDialog {
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JComboBox<String> statusCombo;
    private JTable reportTable;
    private JButton btnGenerate;

    public ReportView() {
        setTitle("Financial Report");
        setSize(800, 500);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Start Date (dd/MM/yyyy):"));
        txtStartDate = new JTextField(10);
        add(txtStartDate);

        add(new JLabel("End Date (dd/MM/yyyy):"));
        txtEndDate = new JTextField(10);
        add(txtEndDate);

        add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(new String[]{"All", "Planned", "In Progress", "Completed"});
        add(statusCombo);

        btnGenerate = new JButton("Generate Report");
        add(btnGenerate);

        reportTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public String getStartDate() { return txtStartDate.getText(); }
    public String getEndDate() { return txtEndDate.getText(); }
    public String getStatus() { return (String) statusCombo.getSelectedItem(); }
    public JButton getGenerateButton() { return btnGenerate; }
    
    

    public void updateTable(java.util.List<ReportDTO> data) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Activity", "Start Date", "End Date", "Status", "Total Income", "Total Expenses", "Balance", "Estimated Income", "Estimated Expenses", "Paid Income", "Paid Expenses"}, 0);

        for (ReportDTO item : data) {
            model.addRow(new Object[]{
                item.getEditionTitle(),
                item.getEditionStartDate(),
                item.getEditionEndDate(),
                item.getEditionStatus(),
                String.format("€%.2f", item.getTotalIncome()),
                String.format("€%.2f", item.getTotalExpenses()),
                String.format("€%.2f", item.getBalance()),
                String.format("€%.2f", item.getEstimatedIncome()),
                String.format("€%.2f", item.getEstimatedExpenses()),
                String.format("€%.2f", item.getTotalPaidIncome()),
                String.format("€%.2f", item.getTotalPaidExpenses())
            });
        }
        reportTable.setModel(model);
    }
    
    
}