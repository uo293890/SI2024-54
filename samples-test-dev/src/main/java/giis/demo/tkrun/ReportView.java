package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportView extends JFrame {

    private JTable tableActivities, tableSponsorships, tableIncome, tableExpenses;
    private DefaultTableModel modelActivities, modelSponsorships, modelIncome, modelExpenses;
    private JLabel lblEstIncome, lblEstExpenses, lblEstBalance;
    private JLabel lblPaidIncome, lblPaidExpenses, lblPaidBalance;
    private JLabel lblSponsorEst, lblSponsorAct, lblIncomeEst, lblIncomeAct, lblExpensesEst, lblExpensesAct;
    private JButton btnGoBack, btnConsult;
    private JTextField txtStartDate, txtEndDate;
    private JComboBox<String> cmbStatus;

    public ReportView() {
        setTitle("Consult Status Activity");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Filters Panel
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
        
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // Activity Table Panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(new TitledBorder("Select an Activity"));
        modelActivities = new DefaultTableModel(new Object[]{"ID", "Name", "Status", "Start Date", "End Date"}, 0);
        tableActivities = new JTable(modelActivities);
        activityPanel.add(new JScrollPane(tableActivities), BorderLayout.CENTER);
        mainPanel.add(activityPanel, BorderLayout.CENTER);

        // Financial Details Panel
        JPanel financePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        financePanel.setBorder(new TitledBorder("Financial Details"));

        modelSponsorships = new DefaultTableModel(new Object[]{"Amount", "Sponsor", "Status", "Date"}, 0);
        tableSponsorships = new JTable(modelSponsorships);
        JPanel sponsorshipPanel = new JPanel(new BorderLayout());
        sponsorshipPanel.setBorder(new TitledBorder("Sponsorships"));
        sponsorshipPanel.add(new JScrollPane(tableSponsorships), BorderLayout.CENTER);
        financePanel.add(sponsorshipPanel);

        modelIncome = new DefaultTableModel(new Object[]{"Amount", "Concept", "Status", "Date"}, 0);
        tableIncome = new JTable(modelIncome);
        JPanel incomePanel = new JPanel(new BorderLayout());
        incomePanel.setBorder(new TitledBorder("Income"));
        incomePanel.add(new JScrollPane(tableIncome), BorderLayout.CENTER);
        financePanel.add(incomePanel);

        modelExpenses = new DefaultTableModel(new Object[]{"Amount", "Concept", "Status", "Date"}, 0);
        tableExpenses = new JTable(modelExpenses);
        JPanel expensesPanel = new JPanel(new BorderLayout());
        expensesPanel.setBorder(new TitledBorder("Expenses"));
        expensesPanel.add(new JScrollPane(tableExpenses), BorderLayout.CENTER);
        financePanel.add(expensesPanel);

        mainPanel.add(financePanel, BorderLayout.SOUTH);

        // Totals Panel
        JPanel totalsPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        totalsPanel.setBorder(new TitledBorder("Totals"));
        
        lblEstIncome = new JLabel("0.0");
        lblEstExpenses = new JLabel("0.0");
        lblEstBalance = new JLabel("0.0");
        lblPaidIncome = new JLabel("0.0");
        lblPaidExpenses = new JLabel("0.0");
        lblPaidBalance = new JLabel("0.0");
        lblSponsorEst = new JLabel("0.0");
        lblSponsorAct = new JLabel("0.0");
        lblIncomeEst = new JLabel("0.0");
        lblIncomeAct = new JLabel("0.0");
        lblExpensesEst = new JLabel("0.0");
        lblExpensesAct = new JLabel("0.0");

        totalsPanel.add(new JLabel("Estimated Income:"));
        totalsPanel.add(lblEstIncome);
        totalsPanel.add(new JLabel("Actual Income:"));
        totalsPanel.add(lblPaidIncome);
        
        totalsPanel.add(new JLabel("Estimated Expenses:"));
        totalsPanel.add(lblEstExpenses);
        totalsPanel.add(new JLabel("Actual Expenses:"));
        totalsPanel.add(lblPaidExpenses);
        
        totalsPanel.add(new JLabel("Estimated Balance:"));
        totalsPanel.add(lblEstBalance);
        totalsPanel.add(new JLabel("Actual Balance:"));
        totalsPanel.add(lblPaidBalance);
        
        mainPanel.add(totalsPanel, BorderLayout.EAST);

        // Back Button
        btnGoBack = new JButton("Go back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGoBack);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getGoBackButton() {
        return btnGoBack;
    }

    public JButton getConsultButton() {
        return btnConsult;
    }

    public String getStartDate() {
        return txtStartDate.getText();
    }

    public String getEndDate() {
        return txtEndDate.getText();
    }

    public String getStatus() {
        return (String) cmbStatus.getSelectedItem();
    }

    public void updateActivitiesTable(List<Object[]> data) {
        modelActivities.setRowCount(0);
        for (Object[] row : data) {
            modelActivities.addRow(row);
        }
    }
}