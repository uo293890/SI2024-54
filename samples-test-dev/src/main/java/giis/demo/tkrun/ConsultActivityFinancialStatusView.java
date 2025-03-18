package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

public class ActivityFinancialStatusView extends JFrame {
    private JTable activityTable;
    private JTable sponsorshipTable;
    private JTable financialOverviewTable;
    private JButton viewSummaryButton;

    public ActivityFinancialStatusView() {
        initialize();
    }

    private void initialize() {
        setTitle("Activity Financial Status");
        setSize(800, 600); // Adjusted window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Use BorderLayout for the main layout
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Activity Financial Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Select Activity Panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Select Activity"));
        activityTable = createTable(new String[]{"Edition", "Name", "Date", "State"}, new Object[][]{});
        activityPanel.add(new JScrollPane(activityTable), BorderLayout.CENTER);
        contentPanel.add(activityPanel);

        // Sponsorship List Panel
        JPanel sponsorshipPanel = new JPanel(new BorderLayout());
        sponsorshipPanel.setBorder(BorderFactory.createTitledBorder("Sponsorship List"));
        sponsorshipTable = createTable(new String[]{"Sponsor Name", "Agreement Date", "Amount (€)", "Status"}, new Object[][]{});
        sponsorshipPanel.add(new JScrollPane(sponsorshipTable), BorderLayout.CENTER);
        contentPanel.add(sponsorshipPanel);

        // Financial Overview Panel
        JPanel financialPanel = new JPanel(new BorderLayout());
        financialPanel.setBorder(BorderFactory.createTitledBorder("Financial Overview"));
        financialOverviewTable = createTable(new String[]{"Category", "Estimated (€)", "Paid (€)"}, new Object[][]{
                {"Income", "", ""},
                {"Expenses", "", ""}
        });
        financialPanel.add(new JScrollPane(financialOverviewTable), BorderLayout.CENTER);
        contentPanel.add(financialPanel);

        add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        viewSummaryButton = new JButton("View Financial Summary");
        viewSummaryButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewSummaryButton.setBackground(new Color(50, 150, 250)); // Blue background
        viewSummaryButton.setForeground(Color.WHITE); // White text
        viewSummaryButton.setFocusPainted(false);
        buttonPanel.add(viewSummaryButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Helper method to create a table with consistent styling
    private JTable createTable(String[] columnNames, Object[][] data) {
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25); // Increase row height for better readability
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    // Getters for UI components
    public JTable getActivityTable() { return activityTable; }
    public JTable getSponsorshipTable() { return sponsorshipTable; }
    public JTable getFinancialOverviewTable() { return financialOverviewTable; }
    public JButton getViewSummaryButton() { return viewSummaryButton; }
}