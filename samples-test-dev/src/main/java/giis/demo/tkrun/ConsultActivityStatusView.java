package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class ConsultActivityStatusView extends JFrame {
    private JTable eventsTable;
    private JTable sponsorsTable;
    private JTable incomesTable;
    private JTable expensesTable;
    
    // Table models
    private DefaultTableModel eventsTableModel;
    private DefaultTableModel sponsorsTableModel;
    private DefaultTableModel incomesTableModel;
    private DefaultTableModel expensesTableModel;
    
    // Financial summary labels - Estimated
    private JLabel estimatedIncomesLabel;
    private JLabel estimatedExpensesLabel;
    private JLabel estimatedBalanceLabel;
    
    // Financial summary labels - Actual
    private JLabel actualIncomesLabel;
    private JLabel actualExpensesLabel;
    private JLabel actualBalanceLabel;
    
    // Section summaries
    private JLabel sponsorsEstimatedLabel;
    private JLabel sponsorsPaidLabel;
    private JLabel incomesEstimatedLabel;
    private JLabel incomesPaidLabel;
    private JLabel expensesEstimatedLabel;
    private JLabel expensesPaidLabel;

    public ConsultActivityStatusView() {
        initialize();
        setupEventSelectionListener();
    }

    private void initialize() {
        setTitle("Consult Activity Status");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Activity Financial Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left Panel: Events Table
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Events"));
        
        // Initialize events table with DefaultTableModel
        eventsTableModel = new DefaultTableModel(
            new Object[]{"Event Name", "Start Date", "End Date", "Status", "Type"}, 0);
        eventsTable = new JTable(eventsTableModel);
        configureTable(eventsTable);
        leftPanel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        contentPanel.add(leftPanel);

        // Right Panel: Financial Information
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Totals Panel
        JPanel totalsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Financial Totals"));
        
        // Estimated Totals Panel
        JPanel estimatedPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        estimatedPanel.setBorder(BorderFactory.createTitledBorder("Estimated"));
        estimatedIncomesLabel = new JLabel("Incomes: €0.00");
        estimatedExpensesLabel = new JLabel("Expenses: €0.00");
        estimatedBalanceLabel = new JLabel("Balance: €0.00");
        estimatedIncomesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        estimatedExpensesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        estimatedBalanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        estimatedPanel.add(estimatedIncomesLabel);
        estimatedPanel.add(estimatedExpensesLabel);
        estimatedPanel.add(estimatedBalanceLabel);
        
        // Actual Totals Panel
        JPanel actualPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        actualPanel.setBorder(BorderFactory.createTitledBorder("Actual"));
        actualIncomesLabel = new JLabel("Incomes: €0.00");
        actualExpensesLabel = new JLabel("Expenses: €0.00");
        actualBalanceLabel = new JLabel("Balance: €0.00");
        actualIncomesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        actualExpensesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        actualBalanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        actualPanel.add(actualIncomesLabel);
        actualPanel.add(actualExpensesLabel);
        actualPanel.add(actualBalanceLabel);
        
        totalsPanel.add(estimatedPanel);
        totalsPanel.add(actualPanel);
        rightPanel.add(totalsPanel, BorderLayout.NORTH);

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));

        // Sponsors Section
        JPanel sponsorsSection = new JPanel(new BorderLayout());
        sponsorsSection.setBorder(BorderFactory.createTitledBorder("Sponsors"));
        
        JPanel sponsorsSummary = new JPanel(new GridLayout(2, 1));
        sponsorsEstimatedLabel = new JLabel("Estimated: €0.00");
        sponsorsPaidLabel = new JLabel("Paid: €0.00");
        sponsorsSummary.add(sponsorsEstimatedLabel);
        sponsorsSummary.add(sponsorsPaidLabel);
        sponsorsSection.add(sponsorsSummary, BorderLayout.NORTH);
        
        // Initialize sponsors table with DefaultTableModel
        sponsorsTableModel = new DefaultTableModel(
            new Object[]{"Sponsor", "Level", "Agreement Amount", "Status", "Payment Date"}, 0);
        sponsorsTable = new JTable(sponsorsTableModel);
        configureTable(sponsorsTable);
        sponsorsSection.add(new JScrollPane(sponsorsTable), BorderLayout.CENTER);
        detailsPanel.add(sponsorsSection);

        // Incomes Section
        JPanel incomesSection = new JPanel(new BorderLayout());
        incomesSection.setBorder(BorderFactory.createTitledBorder("Incomes"));
        
        JPanel incomesSummary = new JPanel(new GridLayout(2, 1));
        incomesEstimatedLabel = new JLabel("Estimated: €0.00");
        incomesPaidLabel = new JLabel("Paid: €0.00");
        incomesSummary.add(incomesEstimatedLabel);
        incomesSummary.add(incomesPaidLabel);
        incomesSection.add(incomesSummary, BorderLayout.NORTH);
        
        // Initialize incomes table with DefaultTableModel
        incomesTableModel = new DefaultTableModel(
            new Object[]{"Concept", "Amount", "Status"}, 0);
        incomesTable = new JTable(incomesTableModel);
        configureTable(incomesTable);
        incomesSection.add(new JScrollPane(incomesTable), BorderLayout.CENTER);
        detailsPanel.add(incomesSection);

        // Expenses Section
        JPanel expensesSection = new JPanel(new BorderLayout());
        expensesSection.setBorder(BorderFactory.createTitledBorder("Expenses"));
        
        JPanel expensesSummary = new JPanel(new GridLayout(2, 1));
        expensesEstimatedLabel = new JLabel("Estimated: €0.00");
        expensesPaidLabel = new JLabel("Paid: €0.00");
        expensesSummary.add(expensesEstimatedLabel);
        expensesSummary.add(expensesPaidLabel);
        expensesSection.add(expensesSummary, BorderLayout.NORTH);
        
        // Initialize expenses table with DefaultTableModel
        expensesTableModel = new DefaultTableModel(
            new Object[]{"Concept", "Amount", "Status"}, 0);
        expensesTable = new JTable(expensesTableModel);
        configureTable(expensesTable);
        expensesSection.add(new JScrollPane(expensesTable), BorderLayout.CENTER);
        detailsPanel.add(expensesSection);

        rightPanel.add(detailsPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void configureTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupEventSelectionListener() {
        eventsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = eventsTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String eventName = (String) eventsTable.getValueAt(selectedRow, 0);
                        fireEventSelected(eventName);
                    }
                }
            }
        });
    }

    protected void fireEventSelected(String eventName) {
        // To be implemented by controller
    }

    // Getters for table models
    public DefaultTableModel getEventsTableModel() { return eventsTableModel; }
    public DefaultTableModel getSponsorsTableModel() { return sponsorsTableModel; }
    public DefaultTableModel getIncomesTableModel() { return incomesTableModel; }
    public DefaultTableModel getExpensesTableModel() { return expensesTableModel; }

    // Getters for UI components
    public JTable getEventsTable() { return eventsTable; }
    public JTable getSponsorsTable() { return sponsorsTable; }
    public JTable getIncomesTable() { return incomesTable; }
    public JTable getExpensesTable() { return expensesTable; }

    // Method to update all financial information
    public void updateFinancialData(
            double sponsorsEstimated, double sponsorsPaid,
            double incomesEstimated, double incomesPaid,
            double expensesEstimated, double expensesPaid) {
        
        // Update section summaries
        sponsorsEstimatedLabel.setText(String.format("Estimated: €%.2f", sponsorsEstimated));
        sponsorsPaidLabel.setText(String.format("Paid: €%.2f", sponsorsPaid));
        
        incomesEstimatedLabel.setText(String.format("Estimated: €%.2f", incomesEstimated));
        incomesPaidLabel.setText(String.format("Paid: €%.2f", incomesPaid));
        
        expensesEstimatedLabel.setText(String.format("Estimated: €%.2f", expensesEstimated));
        expensesPaidLabel.setText(String.format("Paid: €%.2f", expensesPaid));
        
        // Calculate totals
        double totalEstimatedIncomes = sponsorsEstimated + incomesEstimated;
        double totalActualIncomes = sponsorsPaid + incomesPaid;
        
        // Update totals panel
        estimatedIncomesLabel.setText(String.format("Incomes: €%.2f", totalEstimatedIncomes));
        estimatedExpensesLabel.setText(String.format("Expenses: €%.2f", expensesEstimated));
        
        actualIncomesLabel.setText(String.format("Incomes: €%.2f", totalActualIncomes));
        actualExpensesLabel.setText(String.format("Expenses: €%.2f", expensesPaid));
        
        // Calculate and update balances
        double estimatedBalance = totalEstimatedIncomes - expensesEstimated;
        double actualBalance = totalActualIncomes - expensesPaid;
        
        estimatedBalanceLabel.setText(String.format("Balance: €%.2f", estimatedBalance));
        actualBalanceLabel.setText(String.format("Balance: €%.2f", actualBalance));
        
        // Highlight negative balances in red
        if (estimatedBalance < 0) {
            estimatedBalanceLabel.setForeground(Color.RED);
        } else {
            estimatedBalanceLabel.setForeground(Color.BLACK);
        }
        
        if (actualBalance < 0) {
            actualBalanceLabel.setForeground(Color.RED);
        } else {
            actualBalanceLabel.setForeground(Color.BLACK);
        }
    }
}