package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class FinancialReportView extends JDialog {
    private JTable table;
    private JButton btnGenerate;
    private JComboBox<String> cmbStatus;
    private JTextField txtStartDate;
    private JTextField txtEndDate;

    public FinancialReportView(JFrame parent) {
        super(parent, "Financial Report", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new GridLayout(0, 2));
        filterPanel.add(new JLabel("Start Date:"));
        txtStartDate = new JTextField(10);
        filterPanel.add(txtStartDate);
        
        filterPanel.add(new JLabel("End Date:"));
        txtEndDate = new JTextField(10);
        filterPanel.add(txtEndDate);
        
        filterPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"Planned", "Completed", "All"});
        filterPanel.add(cmbStatus);
        
        btnGenerate = new JButton("Generate Report");
        filterPanel.add(btnGenerate);
        
        add(filterPanel, BorderLayout.NORTH);
        
        // Tabla de resultados
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    public void displayReport(FinancialReportDTO report) {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Activity", "Start Date", "End Date", "Status", 
                       "Estimated Income", "Actual Income", "Estimated Expenses", "Actual Expenses", "Balance"}, 0);
        
        for(ActivityDTO activity : report.getActivities()) {
            double balance = (activity.getActualIncome() - activity.getActualExpenses());
            model.addRow(new Object[]{
                activity.getName(),
                activity.getStartDate(),
                activity.getEndDate(),
                activity.getStatus(),
                activity.getEstimatedIncome(),
                activity.getActualIncome(),
                activity.getEstimatedExpenses(),
                activity.getActualExpenses(),
                balance
            });
        }
        
        table.setModel(model);
    }
    
    // Getters para los componentes
    public JButton getBtnGenerate() { return btnGenerate; }
    public String getStartDate() { return txtStartDate.getText(); }
    public String getEndDate() { return txtEndDate.getText(); }
    public String getSelectedStatus() { return (String) cmbStatus.getSelectedItem(); }
}