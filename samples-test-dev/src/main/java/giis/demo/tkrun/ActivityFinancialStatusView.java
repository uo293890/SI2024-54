package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActivityFinancialStatusView extends JFrame {
    private JComboBox<String> activityComboBox;
    private JTable activityTable;
    private JTable sponsorshipTable;
    private JTextField incomeEstimatedField;
    private JTextField incomePaidField;
    private JTextField expenseEstimatedField;
    private JTextField expensePaidField;
    private JButton viewSummaryButton;

    public ActivityFinancialStatusView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Activity Financial Status");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Activity Selection Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Activity:"));
        activityComboBox = new JComboBox<>(new String[]{"Tech Conference", "Cybersecurity Forum"});
        activityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedActivity = (String) activityComboBox.getSelectedItem();
                new ActivityFinancialStatusController(new ActivityFinancialStatusModel(), ActivityFinancialStatusView.this)
                        .updateFinancialStatus(selectedActivity);
            }
        });
        topPanel.add(activityComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Activity Table
        String[] activityColumns = {"Edition", "Name", "Date", "State"};
        Object[][] activityData = {
                {"2024", "Tech Conference", "2024-05-01", "Active"},
                {"2025", "Cybersecurity Forum", "2025-06-01", "Planned"}
        };
        activityTable = new JTable(activityData, activityColumns);
        add(new JScrollPane(activityTable), BorderLayout.CENTER);

        // Sponsorship Table
        String[] sponsorshipColumns = {"Sponsor Name", "Agreement Date", "Amount ($)", "Status"};
        Object[][] sponsorshipData = {
                {"Company A", "01/02/2024", "5000", "Paid"},
                {"Company B", "05/03/2024", "7000", "Estimated"}
        };
        sponsorshipTable = new JTable(sponsorshipData, sponsorshipColumns);
        add(new JScrollPane(sponsorshipTable), BorderLayout.WEST);

        // Financial Overview Panel
        JPanel financialPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        financialPanel.setBorder(BorderFactory.createTitledBorder("Financial Overview"));

        financialPanel.add(new JLabel("Category"));
        financialPanel.add(new JLabel("Estimated ($)"));
        financialPanel.add(new JLabel("Paid ($)"));

        financialPanel.add(new JLabel("Income"));
        incomeEstimatedField = new JTextField();
        incomePaidField = new JTextField();
        financialPanel.add(incomeEstimatedField);
        financialPanel.add(incomePaidField);

        financialPanel.add(new JLabel("Expenses"));
        expenseEstimatedField = new JTextField();
        expensePaidField = new JTextField();
        financialPanel.add(expenseEstimatedField);
        financialPanel.add(expensePaidField);

        add(financialPanel, BorderLayout.EAST);

        // View Financial Summary Button
        viewSummaryButton = new JButton("View Financial Summary");
        viewSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to display financial summary
                JOptionPane.showMessageDialog(ActivityFinancialStatusView.this, "Financial Summary Displayed");
            }
        });
        add(viewSummaryButton, BorderLayout.SOUTH);
    }

    public void displayFinancialStatus(ActivityFinancialStatusDTO dto) {
        // Update Sponsorship Table
        Object[][] sponsorshipData = new Object[dto.getSponsorships().size()][4];
        for (int i = 0; i < dto.getSponsorships().size(); i++) {
            SponsorshipDTO sponsorship = dto.getSponsorships().get(i);
            sponsorshipData[i][0] = sponsorship.getSponsorName();
            sponsorshipData[i][1] = sponsorship.getAgreementDate();
            sponsorshipData[i][2] = sponsorship.getAmount(); // Use getAmount method
            sponsorshipData[i][3] = sponsorship.getStatus();
        }
        sponsorshipTable.setModel(new javax.swing.table.DefaultTableModel(
                sponsorshipData,
                new String[]{"Sponsor Name", "Agreement Date", "Amount ($)", "Status"}
        ));

        // Update Financial Overview
        double totalIncomeEstimated = 0;
        double totalIncomePaid = 0;
        for (IncomeDTO income : dto.getIncomes()) {
            totalIncomeEstimated += income.getEstimatedAmount();
            totalIncomePaid += income.getPaidAmount();
        }
        incomeEstimatedField.setText(String.format("%.2f", totalIncomeEstimated));
        incomePaidField.setText(String.format("%.2f", totalIncomePaid));

        double totalExpenseEstimated = 0;
        double totalExpensePaid = 0;
        for (ExpenseDTO expense : dto.getExpenses()) {
            totalExpenseEstimated += expense.getEstimatedAmount();
            totalExpensePaid += expense.getPaidAmount();
        }
        expenseEstimatedField.setText(String.format("%.2f", totalExpenseEstimated));
        expensePaidField.setText(String.format("%.2f", totalExpensePaid));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ActivityFinancialStatusView view = new ActivityFinancialStatusView();
            view.setVisible(true);
        });
    }
}