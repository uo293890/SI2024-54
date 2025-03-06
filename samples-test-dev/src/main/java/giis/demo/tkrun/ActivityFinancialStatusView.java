package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ActivityFinancialStatusView extends JFrame {
    private JComboBox<String> eventComboBox;
    private JTable sponsorshipTable;
    private JTable incomeTable;
    private JTable expenseTable;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;

    private ActivityFinancialStatusController controller;

    public ActivityFinancialStatusView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Activity Financial Status");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Event Selection
        add(new JLabel("Select Activity:"));
        eventComboBox = new JComboBox<>();
        eventComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFinancialStatus();
            }
        });
        add(eventComboBox);

        // Sponsorship Table
        String[] sponsorshipColumns = {"Sponsor Name", "Agreement Date", "Amount", "Status"};
        Object[][] sponsorshipData = {};
        sponsorshipTable = new JTable(sponsorshipData, sponsorshipColumns);
        add(new JScrollPane(sponsorshipTable));

        // Income Table
        String[] incomeColumns = {"Category", "Estimated", "Paid"};
        Object[][] incomeData = {};
        incomeTable = new JTable(incomeData, incomeColumns);
        add(new JScrollPane(incomeTable));

        // Expense Table
        String[] expenseColumns = {"Category", "Estimated", "Paid"};
        Object[][] expenseData = {};
        expenseTable = new JTable(expenseData, expenseColumns);
        add(new JScrollPane(expenseTable));

        // Totals
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalExpenseLabel = new JLabel("Total Expense: $0.00");
        add(totalIncomeLabel);
        add(totalExpenseLabel);

        // Initialize Controller
        controller = new ActivityFinancialStatusController(
            new ActivityFinancialStatusModel(), this);

        // Load Events
        loadEvents();
    }

    private void loadEvents() {
        // Fetch events from the database (not implemented here)
        // For now, add sample data
        eventComboBox.addItem("Tech Conference");
        eventComboBox.addItem("Cybersecurity Forum");
    }

    private void updateFinancialStatus() {
        int eventId = eventComboBox.getSelectedIndex() + 1; // Assuming IDs start at 1
        ActivityFinancialStatusDTO dto = controller.getFinancialStatus(eventId);

        // Update Sponsorship Table
        Object[][] sponsorshipData = new Object[dto.getSponsorships().size()][4];
        for (int i = 0; i < dto.getSponsorships().size(); i++) {
            SponsorshipDTO sponsorship = dto.getSponsorships().get(i);
            sponsorshipData[i][0] = sponsorship.getSponsorName();
            sponsorshipData[i][1] = sponsorship.getAgreementDate();
            sponsorshipData[i][2] = sponsorship.getAmount();
            sponsorshipData[i][3] = sponsorship.getStatus();
        }
        sponsorshipTable.setModel(new javax.swing.table.DefaultTableModel(
            sponsorshipData,
            new String[]{"Sponsor Name", "Agreement Date", "Amount", "Status"}
        ));

        // Update Income Table
        Object[][] incomeData = new Object[dto.getIncomes().size()][3];
        double totalIncome = 0;
        for (int i = 0; i < dto.getIncomes().size(); i++) {
            FinancialCategoryDTO income = dto.getIncomes().get(i);
            incomeData[i][0] = income.getCategory();
            incomeData[i][1] = income.getEstimated();
            incomeData[i][2] = income.getPaid();
            totalIncome += income.getPaid();
        }
        incomeTable.setModel(new javax.swing.table.DefaultTableModel(
            incomeData,
            new String[]{"Category", "Estimated", "Paid"}
        ));
        totalIncomeLabel.setText("Total Income: $" + totalIncome);

        // Update Expense Table
        Object[][] expenseData = new Object[dto.getExpenses().size()][3];
        double totalExpense = 0;
        for (int i = 0; i < dto.getExpenses().size(); i++) {
            FinancialCategoryDTO expense = dto.getExpenses().get(i);
            expenseData[i][0] = expense.getCategory();
            expenseData[i][1] = expense.getEstimated();
            expenseData[i][2] = expense.getPaid();
            totalExpense += expense.getPaid();
        }
        expenseTable.setModel(new javax.swing.table.DefaultTableModel(
            expenseData,
            new String[]{"Category", "Estimated", "Paid"}
        ));
        totalExpenseLabel.setText("Total Expense: $" + totalExpense);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ActivityFinancialStatusView view = new ActivityFinancialStatusView();
            view.setVisible(true);
        });
    }
}