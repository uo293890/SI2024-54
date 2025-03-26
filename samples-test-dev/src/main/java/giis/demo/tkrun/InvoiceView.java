package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;



/**
 * Graphical user interface (GUI) for managing invoices.
 * Allows users to select activities and agreements, generate invoices, and send them.
 * Displays invoice details and a list of already generated invoices.
 */
public class InvoiceView extends JFrame {
    private JComboBox<String> activityDropdown;
    private JTable agreementTable, generatedInvoicesTable;
    private DefaultTableModel agreementTableModel, generatedInvoicesTableModel;
    private JTextField txtInvoiceNumber, txtInvoiceDate, txtInvoiceVat;
    private JTextField txtRecipientName, txtRecipientTaxId, txtRecipientAddress;
    private JButton btnGenerate, btnSend;

    /**
     * Initializes the view, creating all UI components and layout.
     */
    public InvoiceView() {
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        initComponents();
    }

    private void initComponents() {
        // Top: activity selector
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        selectionPanel.setBorder(new TitledBorder("Select Activity"));
        activityDropdown = new JComboBox<>();
        activityDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionPanel.add(activityDropdown);
        add(selectionPanel, BorderLayout.NORTH);

        // Center: Agreements + Invoice Details + Generated Invoices
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Agreements table
        agreementTableModel = new DefaultTableModel(new Object[]{"Agreement ID", "Sponsor Name", "Contact Name", "Email", "Amount", "Status"}, 0);
        agreementTable = new JTable(agreementTableModel);
        agreementTable.setFont(new Font("Arial", Font.PLAIN, 14));
        agreementTable.setRowHeight(25);
        agreementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        agreementTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = agreementTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtInvoiceNumber.setText(generateInvoiceNumber());
                    txtInvoiceDate.setText(""); // se genera al enviar
                    txtRecipientName.setText(agreementTableModel.getValueAt(selectedRow, 1).toString());
                    txtRecipientTaxId.setText(agreementTableModel.getValueAt(selectedRow, 2).toString());
                    txtRecipientAddress.setText(agreementTableModel.getValueAt(selectedRow, 3).toString());
                    btnGenerate.setEnabled(true);
                    btnSend.setEnabled(false); // aún no se puede mandar hasta que se genere
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(agreementTable);
        tableScrollPane.setBorder(new TitledBorder("Select Agreement"));
        centerPanel.add(tableScrollPane);

        // Right panel: Invoice Details + Generated Invoices
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Invoice Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        detailsPanel.setBorder(new TitledBorder("Invoice Details"));

        txtInvoiceNumber = new JTextField();
        txtInvoiceDate = new JTextField();
        txtInvoiceVat = new JTextField("21");
        txtInvoiceVat.setEditable(false);
        txtRecipientName = new JTextField();
        txtRecipientTaxId = new JTextField();
        txtRecipientAddress = new JTextField();

        txtInvoiceNumber.setEditable(false);
        txtInvoiceDate.setEditable(false);
        txtRecipientName.setEditable(false);
        txtRecipientTaxId.setEditable(false);
        txtRecipientAddress.setEditable(false);

        detailsPanel.add(new JLabel("Invoice Number:")); detailsPanel.add(txtInvoiceNumber);
        detailsPanel.add(new JLabel("Invoice Date:")); detailsPanel.add(txtInvoiceDate);
        detailsPanel.add(new JLabel("VAT (%):")); detailsPanel.add(txtInvoiceVat);
        detailsPanel.add(new JLabel("Recipient Name:")); detailsPanel.add(txtRecipientName);
        detailsPanel.add(new JLabel("Recipient Tax ID:")); detailsPanel.add(txtRecipientTaxId);
        detailsPanel.add(new JLabel("Recipient Address:")); detailsPanel.add(txtRecipientAddress);

        // Generated Invoices Table
        generatedInvoicesTableModel = new DefaultTableModel(
                new Object[]{"Invoice Number", "Date Sent", "Recipient", "VAT"}, 0);
        generatedInvoicesTable = new JTable(generatedInvoicesTableModel);
        generatedInvoicesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        generatedInvoicesTable.setRowHeight(25);
        JScrollPane invoiceScrollPane = new JScrollPane(generatedInvoicesTable);
        invoiceScrollPane.setBorder(new TitledBorder("Generated Invoices"));

        rightPanel.add(detailsPanel, BorderLayout.NORTH);
        rightPanel.add(invoiceScrollPane, BorderLayout.CENTER);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGenerate = new JButton("Generate Invoice");
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 16));
        btnGenerate.setEnabled(false);

        btnSend = new JButton("Send Invoice");
        btnSend.setFont(new Font("Arial", Font.BOLD, 16));
        btnSend.setEnabled(false); // se activa después de generar

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnSend);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String generateInvoiceNumber() {
        return String.valueOf(System.currentTimeMillis()).substring(4);
    }

    // Get current date (used for sending)
    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    // GETTERS
    public JButton getGenerateButton() { return btnGenerate; }
    public JButton getSendButton() { return btnSend; }
    public String getInvoiceNumber() { return txtInvoiceNumber.getText(); }
    public String getInvoiceVat() { return txtInvoiceVat.getText(); }
    public String getInvoiceDate() { return txtInvoiceDate.getText(); }
    public String getRecipientName() { return txtRecipientName.getText(); }
    public String getRecipientTaxId() { return txtRecipientTaxId.getText(); }
    public String getRecipientAddress() { return txtRecipientAddress.getText(); }

    public String getSelectedAgreement() {
        int selectedRow = agreementTable.getSelectedRow();
        if (selectedRow != -1) {
            return agreementTableModel.getValueAt(selectedRow, 0).toString();
        }
        return null;
    }

    public JComboBox<String> getActivityDropdown() {
        return activityDropdown;
    }

    
    /**
     * Populates the activity dropdown with a list of event names.
     *
     * @param activities List of Object arrays containing event names.
     */
    public void populateActivityDropdown(List<Object[]> activities) {
        activityDropdown.removeAllItems();
        for (Object[] activity : activities) {
            activityDropdown.addItem(activity[0].toString());
        }
    }

    
    /**
     * Populates the agreement table with agreement data.
     *
     * @param agreements List of Object arrays containing agreement information.
     */
    public void populateAgreementTable(List<Object[]> agreements) {
        agreementTableModel.setRowCount(0);
        for (Object[] agreement : agreements) {
            agreementTableModel.addRow(agreement);
        }
    }

    
    /**
     * Removes the currently selected agreement from the table.
     * Typically called after an invoice is generated.
     */
    public void removeSelectedAgreement() {
        int selectedRow = agreementTable.getSelectedRow();
        if (selectedRow != -1) {
            agreementTableModel.removeRow(selectedRow);
            btnGenerate.setEnabled(false);
            btnSend.setEnabled(false);
        }
    }

    
    /**
     * Clears all invoice detail fields in the view.
     */
    public void clearInvoiceFields() {
        txtInvoiceNumber.setText("");
        txtInvoiceDate.setText("");
        txtInvoiceVat.setText("21");
        txtRecipientName.setText("");
        txtRecipientTaxId.setText("");
        txtRecipientAddress.setText("");
    }

    
    /**
     * Adds a new row to the table of generated invoices.
     *
     * @param invoiceNumber Invoice identifier.
     * @param date          Date the invoice was sent.
     * @param recipient     Name of the recipient.
     * @param vat           VAT percentage.
     */
    public void addGeneratedInvoice(String invoiceNumber, String date, String recipient, String vat) {
        generatedInvoicesTableModel.addRow(new Object[]{invoiceNumber, date, recipient, vat});
    }

    /**
     * Updates the invoice date field in the UI.
     *
     * @param date The sending date in string format.
     */
    public void updateInvoiceDateField(String date) {
        txtInvoiceDate.setText(date);
    }

    /**
     * Displays a generic information message dialog.
     *
     * @param message The message to show.
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error dialog with the given message.
     *
     * @param message The error message to show.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getSelectedActivity() {
        return (String) activityDropdown.getSelectedItem();
    }
}
