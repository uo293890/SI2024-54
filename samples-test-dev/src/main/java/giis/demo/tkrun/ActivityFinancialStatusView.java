package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ActivityFinancialStatusView extends JFrame {
    private JTable eventGrid;
    private JTable editionGrid;
    private JTable agreementGrid;
    private JTable otherieGrid;
    private JTable invoiceGrid;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;

    public ActivityFinancialStatusView() {
        initialize();
    }

    private void initialize() {
        setTitle("Activity Financial Status");
        setSize(1200, 800); // Larger window to accommodate all grids
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Use GridBagLayout for flexible layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding
        gbc.fill = GridBagConstraints.BOTH;

        // Event Grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.2;
        eventGrid = new JTable();
        add(new JScrollPane(eventGrid), gbc);

        // Edition Grid
        gbc.gridy = 1;
        gbc.weighty = 0.2;
        editionGrid = new JTable();
        add(new JScrollPane(editionGrid), gbc);

        // Agreement Grid
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        agreementGrid = new JTable();
        add(new JScrollPane(agreementGrid), gbc);

        // Otherie Grid
        gbc.gridy = 3;
        gbc.weighty = 0.2;
        otherieGrid = new JTable();
        add(new JScrollPane(otherieGrid), gbc);

        // Invoice Grid
        gbc.gridy = 4;
        gbc.weighty = 0.2;
        invoiceGrid = new JTable();
        add(new JScrollPane(invoiceGrid), gbc);

        // Totals Panel
        gbc.gridy = 5;
        gbc.weighty = 0.1;
        JPanel totalsPanel = new JPanel(new GridLayout(1, 2));
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Totals"));
        totalIncomeLabel = new JLabel("Total Income: 0.00");
        totalExpenseLabel = new JLabel("Total Expenses: 0.00");
        totalsPanel.add(totalIncomeLabel);
        totalsPanel.add(totalExpenseLabel);
        add(totalsPanel, gbc);
    }

    // Getters for UI components
    public JTable getEventGrid() { return eventGrid; }
    public JTable getEditionGrid() { return editionGrid; }
    public JTable getAgreementGrid() { return agreementGrid; }
    public JTable getOtherieGrid() { return otherieGrid; }
    public JTable getInvoiceGrid() { return invoiceGrid; }

    // Methods to populate grids
    public void populateEventGrid(List<ActivityFinancialStatusDTO> events) {
        String[] columnNames = {"Event ID", "Event Title"};
        Object[][] data = new Object[events.size()][2];
        for (int i = 0; i < events.size(); i++) {
            ActivityFinancialStatusDTO event = events.get(i);
            data[i][0] = event.getEventId();
            data[i][1] = event.getEventTitle();
        }
        eventGrid.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public void populateEditionGrid(List<ActivityFinancialStatusDTO> editions) {
        String[] columnNames = {"Edition ID", "Edition Title", "Start Date", "End Date", "Status"};
        Object[][] data = new Object[editions.size()][5];
        for (int i = 0; i < editions.size(); i++) {
            ActivityFinancialStatusDTO edition = editions.get(i);
            data[i][0] = edition.getEditionId();
            data[i][1] = edition.getEditionTitle();
            data[i][2] = edition.getEditionStartDate();
            data[i][3] = edition.getEditionEndDate();
            data[i][4] = edition.getEditionStatus();
        }
        editionGrid.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public void populateAgreementGrid(List<ActivityFinancialStatusDTO> agreements) {
        String[] columnNames = {"Agreement ID", "Sponsor Name", "Agreement Date", "Amount", "Status"};
        Object[][] data = new Object[agreements.size()][5];
        for (int i = 0; i < agreements.size(); i++) {
            ActivityFinancialStatusDTO agreement = agreements.get(i);
            data[i][0] = agreement.getAgreementId();
            data[i][1] = agreement.getSponsorName();
            data[i][2] = agreement.getAgreementDate();
            data[i][3] = agreement.getAgreementAmount();
            data[i][4] = agreement.getAgreementStatus();
        }
        agreementGrid.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public void populateOtherieGrid(List<ActivityFinancialStatusDTO> otheries) {
        String[] columnNames = {"Otherie ID", "Description", "Amount", "Status"};
        Object[][] data = new Object[otheries.size()][4];
        for (int i = 0; i < otheries.size(); i++) {
            ActivityFinancialStatusDTO otherie = otheries.get(i);
            data[i][0] = otherie.getOtherieId();
            data[i][1] = otherie.getOtherieDescription();
            data[i][2] = otherie.getOtherieAmount();
            data[i][3] = otherie.getOtherieStatus();
        }
        otherieGrid.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public void populateInvoiceGrid(List<ActivityFinancialStatusDTO> invoices) {
        String[] columnNames = {"Invoice ID", "Invoice Date", "Amount", "Status"};
        Object[][] data = new Object[invoices.size()][4];
        for (int i = 0; i < invoices.size(); i++) {
            ActivityFinancialStatusDTO invoice = invoices.get(i);
            data[i][0] = invoice.getInvoiceId();
            data[i][1] = invoice.getInvoiceDate();
            data[i][2] = invoice.getInvoiceAmount();
            data[i][3] = invoice.getInvoiceStatus();
        }
        invoiceGrid.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    // Method to display totals
    public void displayTotals(double totalIncome, double totalExpenses) {
        totalIncomeLabel.setText("Total Income: " + totalIncome);
        totalExpenseLabel.setText("Total Expenses: " + totalExpenses);
    }

    // Method to get the selected event ID
    public int getSelectedEventId() {
        int selectedRow = eventGrid.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) eventGrid.getValueAt(selectedRow, 0); // Event ID is in column 0
        }
        return -1;
    }

    // Method to get the selected edition ID
    public int getSelectedEditionId() {
        int selectedRow = editionGrid.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) editionGrid.getValueAt(selectedRow, 0); // Edition ID is in column 0
        }
        return -1;
    }
}