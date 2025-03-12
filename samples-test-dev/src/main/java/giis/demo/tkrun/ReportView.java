package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;

public class ReportView extends JFrame {
    private JTable tableActivities;
    private DefaultTableModel modelActivities;
    private JLabel lblEstIncome, lblEstExpenses, lblEstBalance;
    private JLabel lblPaidIncome, lblPaidExpenses, lblPaidBalance;
    private JButton btnGoBack, btnConsult, btnExportExcel, btnExportPDF, btnPreview;
    private JTextField txtStartDate, txtEndDate;
    private JComboBox<String> cmbStatus;
    private JPanel totalsPanel;

    public ReportView() {
        setTitle("Financial Report - Activities Overview");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(new TitledBorder("Filters"));

        filterPanel.add(new JLabel("Start Date:"));
        txtStartDate = new JTextField(10);
        filterPanel.add(txtStartDate);

        filterPanel.add(new JLabel("End Date:"));
        txtEndDate = new JTextField(10);
        filterPanel.add(txtEndDate);

        filterPanel.add(new JLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"All", "Planned", "Ongoing", "Completed"});
        filterPanel.add(cmbStatus);

        btnConsult = new JButton("Consult");
        filterPanel.add(btnConsult);
        
        add(filterPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(new TitledBorder("Activities"));
        modelActivities = new DefaultTableModel(new Object[]{"ID", "Name", "Status", "Start Date", "End Date", "Est. Income", "Est. Expenses", "Act. Income", "Act. Expenses", "Est. Balance", "Act. Balance"}, 0);
        tableActivities = new JTable(modelActivities);
        JScrollPane scrollPane = new JScrollPane(tableActivities);
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        ReportModel.applyColorToTable(tableActivities);

        centerPanel.add(activityPanel);
        
        totalsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        totalsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Financial Overview"));
        
        totalsPanel.add(new JLabel("Estimated Income:"));
        lblEstIncome = createStyledLabel();
        totalsPanel.add(lblEstIncome);
        
        totalsPanel.add(new JLabel("Estimated Expenses:"));
        lblEstExpenses = createStyledLabel();
        totalsPanel.add(lblEstExpenses);
        
        totalsPanel.add(new JLabel("Estimated Balance:"));
        lblEstBalance = createStyledLabel();
        totalsPanel.add(lblEstBalance);
        
        totalsPanel.add(new JLabel("Actual Income:"));
        lblPaidIncome = createStyledLabel();
        totalsPanel.add(lblPaidIncome);
        
        totalsPanel.add(new JLabel("Actual Expenses:"));
        lblPaidExpenses = createStyledLabel();
        totalsPanel.add(lblPaidExpenses);
        
        totalsPanel.add(new JLabel("Actual Balance:"));
        lblPaidBalance = createStyledLabel();
        totalsPanel.add(lblPaidBalance);
        
        centerPanel.add(totalsPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGoBack = new JButton("Go Back");
        btnExportExcel = new JButton("Export to Excel");
        btnExportPDF = new JButton("Export to PDF");
        btnPreview = new JButton("Preview");
        
       
        actionPanel.add(btnGoBack);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JLabel createStyledLabel() {
        JLabel label = new JLabel("0.0", SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    public JButton getGoBackButton() { return btnGoBack; }
    public JButton getConsultButton() { return btnConsult; }
    public JButton getExportExcelButton() { return btnExportExcel; }
    public JButton getExportPDFButton() { return btnExportPDF; }
    public JButton getPreviewButton() { return btnPreview; }
    public String getStartDate() { return txtStartDate.getText(); }
    public String getEndDate() { return txtEndDate.getText(); }
    public String getStatus() {
        return (String) cmbStatus.getSelectedItem();
    }

    public void updateActivitiesTable(List<ReportDTO> data) {
        System.out.println("Updating table with " + data.size() + " records.");
        modelActivities.setRowCount(0);  // Limpiar tabla antes de agregar nuevas filas
        for (ReportDTO dto : data) {
            System.out.println("Adding to table: " + dto.getId() + " - " + dto.getActivityName() + " - " + dto.getStatus());
            modelActivities.addRow(new Object[]{
                dto.getId(),
                dto.getActivityName(),
                dto.getStatus(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getEstimatedIncome(),
                dto.getEstimatedExpenses(),
                dto.getActualIncome(),
                dto.getActualExpenses(),
                dto.getEstimatedBalance(),
                dto.getActualBalance()
            });
        }
    }


    public void updateTotals(double estIncome, double estExpenses, double estBalance, 
            double actIncome, double actExpenses, double actBalance) {
    		lblEstIncome.setText(String.format("%.2f", estIncome));
    		lblEstExpenses.setText(String.format("%.2f", estExpenses));
    		lblEstBalance.setText(String.format("%.2f", estBalance));
    		lblPaidIncome.setText(String.format("%.2f", actIncome));
    		lblPaidExpenses.setText(String.format("%.2f", actExpenses));
    		lblPaidBalance.setText(String.format("%.2f", actBalance));

    		ReportModel.applyColorToFinancialOverview(lblEstIncome, lblEstExpenses, lblEstBalance, lblPaidIncome, lblPaidExpenses, lblPaidBalance);
    }


    public TableModel getReportTableModel() {
        return tableActivities.getModel();
    }
}