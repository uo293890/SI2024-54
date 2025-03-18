package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the view for consulting the financial status of an activity.
 * It includes tables for editions, sponsors, incomes, and expenses, as well as summary labels.
 */
public class ConsultActivityFinancialStatusView extends JFrame {
	private JTable editionsTable;
    private JTable sponsorsTable;
    private JTable incomesTable;
    private JTable expensesTable;
    private JLabel overallEstimatedLabel;
    private JLabel overallPaidLabel;
    private JLabel sponsorEstimatedLabel;
    private JLabel sponsorPaidLabel;
    private JLabel incomesEstimatedLabel;
    private JLabel incomesPaidLabel;
    private JLabel expensesEstimatedLabel;
    private JLabel expensesPaidLabel;

    /**
     * Constructor that initializes the UI components.
     */
    public ConsultActivityFinancialStatusView() {
        initialize();
    }

    /**
     * Initializes the UI components and layout.
     */
    private void initialize() {
        setTitle("Consult Activity Financial Status");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel with Title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Activity Financial Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel with two sections (Editions and Financial Details)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left Panel: Editions Table
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Events"));
        editionsTable = createTable(new String[]{"Type", "Event", "Start Date", "End Date", "State"}, new Object[][]{});
        leftPanel.add(new JScrollPane(editionsTable), BorderLayout.CENTER);
        contentPanel.add(leftPanel);

        // Right Panel: Detailed Financial Information
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Finance Panel
        JPanel financePanel = new JPanel(new GridLayout(4, 1, 10, 10));
        financePanel.setBorder(BorderFactory.createTitledBorder("Financial Info"));

        // Overall Totals Section
        JPanel overallFinancePanel = new JPanel(new BorderLayout());
        overallFinancePanel.setBorder(BorderFactory.createTitledBorder("Overalls"));
        overallEstimatedLabel = new JLabel("Overall Estimated: ");
        overallPaidLabel = new JLabel("Overall Paid: ");
        JPanel overallLabelsPanel = new JPanel(new GridLayout(2, 1));
        overallLabelsPanel.add(overallEstimatedLabel);
        overallLabelsPanel.add(overallPaidLabel);
        overallFinancePanel.add(overallLabelsPanel, BorderLayout.CENTER);
        financePanel.add(overallFinancePanel);

        // Sponsors Finance Section
        JPanel sponsorsFinancePanel = new JPanel(new BorderLayout());
        sponsorsFinancePanel.setBorder(BorderFactory.createTitledBorder("Sponsors"));
        sponsorEstimatedLabel = new JLabel("Estimated: ");
        sponsorPaidLabel = new JLabel("Paid: ");
        JPanel sponsorsLabelsPanel = new JPanel(new GridLayout(2, 1));
        sponsorsLabelsPanel.add(sponsorEstimatedLabel);
        sponsorsLabelsPanel.add(sponsorPaidLabel);
        sponsorsFinancePanel.add(sponsorsLabelsPanel, BorderLayout.CENTER);
        financePanel.add(sponsorsFinancePanel);

        // Incomes Finance Section
        JPanel incomesFinancePanel = new JPanel(new BorderLayout());
        incomesFinancePanel.setBorder(BorderFactory.createTitledBorder("Incomes"));
        incomesEstimatedLabel = new JLabel("Estimated: ");
        incomesPaidLabel = new JLabel("Paid: ");
        JPanel incomesLabelsPanel = new JPanel(new GridLayout(2, 1));
        incomesLabelsPanel.add(incomesEstimatedLabel);
        incomesLabelsPanel.add(incomesPaidLabel);
        incomesFinancePanel.add(incomesLabelsPanel, BorderLayout.CENTER);
        financePanel.add(incomesFinancePanel);

        // Expenses Finance Section
        JPanel expensesFinancePanel = new JPanel(new BorderLayout());
        expensesFinancePanel.setBorder(BorderFactory.createTitledBorder("Expenses"));
        expensesEstimatedLabel = new JLabel("Estimated: ");
        expensesPaidLabel = new JLabel("Paid: ");
        JPanel expensesLabelsPanel = new JPanel(new GridLayout(2, 1));
        expensesLabelsPanel.add(expensesEstimatedLabel);
        expensesLabelsPanel.add(expensesPaidLabel);
        expensesFinancePanel.add(expensesLabelsPanel, BorderLayout.CENTER);
        financePanel.add(expensesFinancePanel);

        rightPanel.add(financePanel, BorderLayout.NORTH);

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));

        // Sponsors Table
        JPanel sponsorsPanel = new JPanel(new BorderLayout());
        sponsorsPanel.setBorder(BorderFactory.createTitledBorder("Sponsors"));
        sponsorsTable = createTable(new String[]{"Sponsor", "Amount", "Status", "Date"}, new Object[][]{});
        sponsorsPanel.add(new JScrollPane(sponsorsTable), BorderLayout.CENTER);
        detailsPanel.add(sponsorsPanel);

        // Incomes Table
        JPanel incomesPanel = new JPanel(new BorderLayout());
        incomesPanel.setBorder(BorderFactory.createTitledBorder("Incomes"));
        incomesTable = createTable(new String[]{"Concept", "Amount", "Status", "Date"}, new Object[][]{});
        incomesPanel.add(new JScrollPane(incomesTable), BorderLayout.CENTER);
        detailsPanel.add(incomesPanel);

        // Expenses Table
        JPanel expensesPanel = new JPanel(new BorderLayout());
        expensesPanel.setBorder(BorderFactory.createTitledBorder("Expenses"));
        expensesTable = createTable(new String[]{"Concept", "Amount", "Status", "Date"}, new Object[][]{});
        expensesPanel.add(new JScrollPane(expensesTable), BorderLayout.CENTER);
        detailsPanel.add(expensesPanel);

        rightPanel.add(detailsPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates and returns a JTable with specified columns and data.
     */
    private JTable createTable(String[] columnNames, Object[][] data) {
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    // Getters for UI components
    public JTable getEditionsTable() { return editionsTable; }
    public JTable getSponsorsTable() { return sponsorsTable; }
    public JTable getIncomesTable() { return incomesTable; }
    public JTable getExpensesTable() { return expensesTable; }
    public JLabel getOverallEstimatedLabel() { return overallEstimatedLabel; }
	public JLabel getOverallPaidLabel() { return overallPaidLabel; }
	public JLabel getSponsorEstimatedLabel() { return sponsorEstimatedLabel; }
	public JLabel getSponsorPaidLabel() { return sponsorPaidLabel; }
	public JLabel getIncomesEstimatedLabel() { return incomesEstimatedLabel; }
	public JLabel getIncomesPaidLabel() { return incomesPaidLabel; }
	public JLabel getExpensesEstimatedLabel() { return expensesEstimatedLabel; }
	public JLabel getExpensesPaidLabel() { return expensesPaidLabel; }
	

}
