package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects; // Import Objects

/**
 * Graphical user interface (GUI) for managing invoices.
 * Allows users to select activities and agreements, process invoices (generate and send).
 * Displays invoice details and a list of already processed invoices.
 */
public class InvoiceView extends JFrame {
	private JComboBox<String> activityDropdown;
    private JTable agreementTable;
    private DefaultTableModel agreementTableModel;
    private JTable generatedInvoicesTable;
    private DefaultTableModel generatedInvoicesTableModel;
    // Ensure fields are explicitly private
    private JTextField txtInvoiceNumber, txtInvoiceDate, txtInvoiceVat;
    private JTextField txtRecipientName, txtRecipientTaxId, txtRecipientAddress;
    private JButton btnProcess; // Single button for processing

    public InvoiceView() {
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        initComponents();
    }

    private void initComponents() {
        // Top Selection Panel (Activity)
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        selectionPanel.setBorder(new TitledBorder("Select Activity"));
        activityDropdown = new JComboBox<>();
        activityDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionPanel.add(activityDropdown);
        add(selectionPanel, BorderLayout.NORTH);

        // Center Panel (Agreement Table and Details/Generated)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Left Side: Agreement Table
        agreementTableModel = new DefaultTableModel(
                new Object[]{"Agreement ID", "Sponsor Name", "Contact Name", "Email", "Amount", "Event Date", "Event Location", "Sponsorship Level", "Agreement Status"}, 0);
        agreementTable = new JTable(agreementTableModel);
        agreementTable.setFont(new Font("Arial", Font.PLAIN, 14));
        agreementTable.setRowHeight(25);
        agreementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Hide the Agreement ID column
        agreementTable.getColumnModel().getColumn(0).setMinWidth(0);
        agreementTable.getColumnModel().getColumn(0).setMaxWidth(0);
        agreementTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        agreementTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = agreementTable.getSelectedRow();
                if (selectedRow != -1) {
                    // When an agreement is selected, clear the invoice number and date for new input
                    txtInvoiceNumber.setText("");
                    txtInvoiceDate.setText("");
                    // Populate recipient fields from the table (use correct column indices after adding ID)
                    // Added null checks for table cell values
                    txtRecipientName.setText(Objects.toString(agreementTableModel.getValueAt(selectedRow, 1), ""));
                    txtRecipientTaxId.setText(Objects.toString(agreementTableModel.getValueAt(selectedRow, 2), ""));
                    txtRecipientAddress.setText(Objects.toString(agreementTableModel.getValueAt(selectedRow, 3), ""));
                    btnProcess.setEnabled(true); // Enable the process button
                } else {
                     // If no row is selected (e.g., after removing a row or initial state)
                    clearInvoiceFields(); // Clear all fields
                    btnProcess.setEnabled(false); // Disable the process button
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(agreementTable);
        tableScrollPane.setBorder(new TitledBorder("Select Agreement to Process"));
        centerPanel.add(tableScrollPane);

        // Right Side: Invoice Details and Generated Invoices
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Invoice Details Panel (for user input/display before processing)
        JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Adjusted grid size
        detailsPanel.setBorder(new TitledBorder("Invoice Details (Enter Number and Date before Processing)"));

        txtInvoiceNumber = new JTextField();
        txtInvoiceDate = new JTextField();
        txtInvoiceVat = new JTextField("21");
        txtInvoiceVat.setEditable(false); // VAT remains non-editable
        txtRecipientName = new JTextField();
        txtRecipientTaxId = new JTextField();
        txtRecipientAddress = new JTextField();

        // Make Invoice Number and Invoice Date editable as requested
        txtInvoiceNumber.setEditable(true);
        txtInvoiceDate.setEditable(true);

        // Recipient fields should be populated from the table and remain non-editable by the user
        txtRecipientName.setEditable(false);
        txtRecipientTaxId.setEditable(false);
        txtRecipientAddress.setEditable(false);

        detailsPanel.add(new JLabel("Invoice Number:")); detailsPanel.add(txtInvoiceNumber);
        detailsPanel.add(new JLabel("Invoice Date (yyyy-MM-dd):")); detailsPanel.add(txtInvoiceDate); // Indicate expected format
        detailsPanel.add(new JLabel("VAT (%):")); detailsPanel.add(txtInvoiceVat);
        detailsPanel.add(new JLabel("Recipient Name:")); detailsPanel.add(txtRecipientName);
        detailsPanel.add(new JLabel("Recipient Tax ID:")); detailsPanel.add(txtRecipientTaxId);
        detailsPanel.add(new JLabel("Recipient Address:")); detailsPanel.add(txtRecipientAddress);

        // Generated Invoices Table
        generatedInvoicesTableModel = new DefaultTableModel(
                new Object[]{"Invoice Number", "Date Sent", "Recipient", "VAT", "Event"}, 0);
        generatedInvoicesTable = new JTable(generatedInvoicesTableModel);
        generatedInvoicesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        generatedInvoicesTable.setRowHeight(25);
        // Make generated invoices table non-editable
        generatedInvoicesTable.setEnabled(false);

        JScrollPane invoiceScrollPane = new JScrollPane(generatedInvoicesTable);
        invoiceScrollPane.setBorder(new TitledBorder("Processed Invoices")); // Renamed title

        rightPanel.add(detailsPanel, BorderLayout.NORTH);
        rightPanel.add(invoiceScrollPane, BorderLayout.CENTER);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Button Panel (Single Button)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnProcess = new JButton("Generate and Send Invoice"); // Single button
        btnProcess.setFont(new Font("Arial", Font.BOLD, 16));
        btnProcess.setEnabled(false); // Disabled initially until an agreement is selected

        buttonPanel.add(btnProcess);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- Getters for Controller Access ---
    // Renamed getGenerateButton to getProcessButton
    public JButton getProcessButton() { return btnProcess; }
    // Removed getSendButton
    public String getInvoiceNumber() { return txtInvoiceNumber.getText(); }
    public String getInvoiceVat() { return txtInvoiceVat.getText(); }
    public String getInvoiceDate() { return txtInvoiceDate.getText(); } // Return text as entered
    public String getRecipientName() { return txtRecipientName.getText(); }
    public String getRecipientTaxId() { return txtRecipientTaxId.getText(); }
    public String getRecipientAddress() { return txtRecipientAddress.getText(); }

    // Returns the Agreement ID from the hidden column of the selected row
    public Integer getSelectedAgreementId() {
        int selectedRow = agreementTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the value from the hidden first column (Agreement ID)
            Object id = agreementTableModel.getValueAt(selectedRow, 0);
            if (id instanceof Number) { // Handle potential different Number types from database (Integer, Long, etc.)
                 return ((Number) id).intValue();
            } else if (id != null) {
                 // Attempt to parse if it's a String and not a Number
                 try {
                     return Integer.parseInt(id.toString());
                 } catch (NumberFormatException e) {
                     System.err.println("Warning: Agreement ID in table column 0 is not a valid number format: " + id);
                     // Handle parsing error - could show a view error or return null
                     // For now, print warning and return null
                     return null;
                 }
            }
             // Handle case where ID is null in the table
             System.err.println("Warning: Agreement ID in table column 0 is null.");
             return null;
        }
        return null; // No row selected
    }

    public JComboBox<String> getActivityDropdown() {
        return activityDropdown;
    }

    // --- Population Methods ---
    public void populateActivityDropdown(List<Object[]> activities) {
        activityDropdown.removeAllItems();
        for (Object[] activity : activities) {
             if (activity != null && activity.length > 0 && activity[0] != null) {
                activityDropdown.addItem(activity[0].toString());
             }
        }
    }

    public void populateAgreementTable(List<Object[]> agreements) {
        agreementTableModel.setRowCount(0); // Clear existing rows
        // Add agreements, assuming the first column in the input List is Agreement ID
        for (Object[] agreement : agreements) {
             // Ensure the data matches the table columns: Agreement ID, Sponsor Name, Contact Name, Email, ...
            if (agreement != null && agreement.length >= 9) { // Check if enough columns are provided and not null
                 agreementTableModel.addRow(new Object[]{
                    agreement[0], // Agreement ID (column 0)
                    agreement[1], // Sponsor Name (column 1)
                    agreement[2], // Contact Name (column 2)
                    agreement[3], // Email (column 3)
                    agreement[4], // Amount (column 4)
                    agreement[5], // Event Date (column 5)
                    agreement[6], // Event Location (column 6)
                    agreement[7], // Sponsorship Level (column 7)
                    agreement[8]  // Agreement Status (column 8)
                 });
            } else {
                System.err.println("Warning: Skipping agreement row due to insufficient or null data: " + (agreement == null ? "null" : agreement.length + " columns"));
            }
        }
        // Re-hide the Agreement ID column after populating, as row count changes might affect it
        if (agreementTable.getColumnModel().getColumnCount() > 0) {
            agreementTable.getColumnModel().getColumn(0).setMinWidth(0);
            agreementTable.getColumnModel().getColumn(0).setMaxWidth(0);
            agreementTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    // --- Utility Methods ---

    // This method removes the selected row from the agreement table.
    // The controller will handle subsequent field clearing and button state changes.
    public void removeSelectedAgreement() {
        int selectedRow = agreementTable.getSelectedRow();
        if (selectedRow != -1) {
            agreementTableModel.removeRow(selectedRow);
            // Do NOT clear fields or change button state here.
            // This is the responsibility of the controller flow.
        }
    }

    // Clears all invoice detail input fields
    public void clearInvoiceFields() {
        txtInvoiceNumber.setText("");
        txtInvoiceDate.setText("");
        txtInvoiceVat.setText("21"); // Reset VAT to default
        txtRecipientName.setText("");
        txtRecipientTaxId.setText("");
        txtRecipientAddress.setText("");
    }

    // Method to clear only recipient fields (less relevant in single-button flow, but keep for consistency)
    public void clearRecipientFields() {
        txtRecipientName.setText("");
        txtRecipientTaxId.setText("");
        txtRecipientAddress.setText("");
    }

    // Add the processed invoice to the generated table with its date
    public void addProcessedInvoice(String invoiceNumber, String dateSent, String recipient, String vat, String eventName) {
        // Add row: Invoice Number, Date Sent, Recipient, VAT, Event Name
        generatedInvoicesTableModel.addRow(new Object[]{invoiceNumber, dateSent, recipient, vat, eventName});
    }

     // Removed updateGeneratedInvoiceDate as date is added immediately now

    // --- Message/Dialog Methods ---
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE); // Use 'this' as parent
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE); // Use 'this' as parent
    }

    // Method for warnings (distinct from information messages or errors)
    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE); // Use 'this' as parent
    }

    // --- Other Getters ---
    public String getSelectedActivity() {
        return (String) activityDropdown.getSelectedItem();
    }
}