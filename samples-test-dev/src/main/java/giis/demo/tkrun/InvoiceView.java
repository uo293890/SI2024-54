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

    public InvoiceView() {
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        initComponents();
    }

    private void initComponents() {
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        selectionPanel.setBorder(new TitledBorder("Select Activity"));
        activityDropdown = new JComboBox<>();
        activityDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
        selectionPanel.add(activityDropdown);
        add(selectionPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        agreementTableModel = new DefaultTableModel(
                new Object[]{"Sponsor Name", "Contact Name", "Email", "Amount", "Event Date", "Event Location", "Sponsorship Level", "Agreement Status"}, 0);
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
                    txtInvoiceDate.setText("");
                    txtRecipientName.setText(agreementTableModel.getValueAt(selectedRow, 0).toString());
                    txtRecipientTaxId.setText(agreementTableModel.getValueAt(selectedRow, 1).toString());
                    txtRecipientAddress.setText(agreementTableModel.getValueAt(selectedRow, 2).toString());
                    btnGenerate.setEnabled(true);
                    btnSend.setEnabled(false);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(agreementTable);
        tableScrollPane.setBorder(new TitledBorder("Select Agreement"));
        centerPanel.add(tableScrollPane);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

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

        generatedInvoicesTableModel = new DefaultTableModel(
                new Object[]{"Invoice Number", "Date Sent", "Recipient", "VAT", "Event"}, 0);
        generatedInvoicesTable = new JTable(generatedInvoicesTableModel);
        generatedInvoicesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        generatedInvoicesTable.setRowHeight(25);
        JScrollPane invoiceScrollPane = new JScrollPane(generatedInvoicesTable);
        invoiceScrollPane.setBorder(new TitledBorder("Generated Invoices"));

        rightPanel.add(detailsPanel, BorderLayout.NORTH);
        rightPanel.add(invoiceScrollPane, BorderLayout.CENTER);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGenerate = new JButton("Generate Invoice");
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 16));
        btnGenerate.setEnabled(false);

        btnSend = new JButton("Send Invoice");
        btnSend.setFont(new Font("Arial", Font.BOLD, 16));
        btnSend.setEnabled(false);

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnSend);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String generateInvoiceNumber() {
        return String.valueOf(System.currentTimeMillis()).substring(4);
    }

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
        return selectedRow != -1 ? String.valueOf(selectedRow + 1) : null;
    }

    public JComboBox<String> getActivityDropdown() {
        return activityDropdown;
    }

    public void populateActivityDropdown(List<Object[]> activities) {
        activityDropdown.removeAllItems();
        for (Object[] activity : activities) {
            activityDropdown.addItem(activity[0].toString());
        }
    }

    public void populateAgreementTable(List<Object[]> agreements) {
        agreementTableModel.setRowCount(0);
        for (Object[] agreement : agreements) {
            agreementTableModel.addRow(agreement);
        }
    }

    public void removeSelectedAgreement() {
        int selectedRow = agreementTable.getSelectedRow();
        if (selectedRow != -1) {
            agreementTableModel.removeRow(selectedRow);
            btnGenerate.setEnabled(false);
            btnSend.setEnabled(false);
        }
    }

    public void clearInvoiceFields() {
        txtInvoiceNumber.setText("");
        txtInvoiceDate.setText("");
        txtInvoiceVat.setText("21");
        txtRecipientName.setText("");
        txtRecipientTaxId.setText("");
        txtRecipientAddress.setText("");
    }

    public void addGeneratedInvoice(String invoiceNumber, String date, String recipient, String vat) {
        generatedInvoicesTableModel.addRow(new Object[]{invoiceNumber, date, recipient, vat, getSelectedActivity()});
    }

    public void updateInvoiceDateField(String date) {
        txtInvoiceDate.setText(date);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getSelectedActivity() {
        return (String) activityDropdown.getSelectedItem();
    }
}
