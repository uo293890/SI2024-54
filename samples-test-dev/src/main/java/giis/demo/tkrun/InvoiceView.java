package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import giis.demo.util.Database;
import java.util.List;

public class InvoiceView extends JFrame {
    private JTextField txtInvoiceNumber, txtInvoiceDate, txtInvoiceVat, txtRecipientName, txtRecipientTaxId, txtRecipientAddress;
    private JComboBox<String> agreementDropdown;
    private JButton btnGenerate, btnSend;
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private Database db = new Database();

    public InvoiceView() {
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadAgreementsFromDatabase();
        loadInvoicesIntoTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Generate Invoice"));

        txtInvoiceNumber = new JTextField();
        txtInvoiceDate = new JTextField();
        txtInvoiceVat = new JTextField();
        txtRecipientName = new JTextField();
        txtRecipientTaxId = new JTextField();
        txtRecipientAddress = new JTextField();
        agreementDropdown = new JComboBox<>();
        btnGenerate = new JButton("Generate Invoice");
        btnSend = new JButton("Send Invoice");

        formPanel.add(new JLabel("Invoice ID:")); formPanel.add(txtInvoiceNumber);
        formPanel.add(new JLabel("Invoice Date:")); formPanel.add(txtInvoiceDate);
        formPanel.add(new JLabel("Invoice VAT:")); formPanel.add(txtInvoiceVat);
        formPanel.add(new JLabel("Recipient Name:")); formPanel.add(txtRecipientName);
        formPanel.add(new JLabel("Recipient Tax ID:")); formPanel.add(txtRecipientTaxId);
        formPanel.add(new JLabel("Recipient Address:")); formPanel.add(txtRecipientAddress);
        formPanel.add(new JLabel("Select Agreement:")); formPanel.add(agreementDropdown);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnSend);
        
        formPanel.add(buttonPanel);
        mainPanel.add(formPanel);

        // Table for displaying invoices with recipient details
        tableModel = new DefaultTableModel(new Object[]{"Invoice ID", "Agreement ID", "Activity Name", "Date", "VAT", "Recipient Name", "Tax ID", "Address"}, 0);
        invoiceTable = new JTable(tableModel);
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = invoiceTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtInvoiceNumber.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    agreementDropdown.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());
                    txtInvoiceDate.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtInvoiceVat.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    txtRecipientName.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    txtRecipientTaxId.setText(tableModel.getValueAt(selectedRow, 6).toString());
                    txtRecipientAddress.setText(tableModel.getValueAt(selectedRow, 7).toString());
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(invoiceTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Select Invoice"));
        mainPanel.add(tableScrollPane);

        add(mainPanel);
    }

    private void loadAgreementsFromDatabase() {
        try {
            List<List<Object>> results = db.executeQuery("SELECT agreement_id FROM Agreement");
            agreementDropdown.removeAllItems();
            for (List<Object> row : results) {
                agreementDropdown.addItem(row.get(0).toString());
            }
        } catch (Exception e) {
            showError("Error loading agreements: " + e.getMessage());
        }
    }

    public void loadInvoicesIntoTable() {
        try {
            List<List<Object>> results = db.executeQuery(
                "SELECT i.invoice_number, i.agreement_id, e.edition_title, i.invoice_date, i.invoice_vat, i.recipient_name, i.recipient_tax_id, i.recipient_address " +
                "FROM Invoice i " +
                "JOIN Agreement a ON i.agreement_id = a.agreement_id " +
                "JOIN Edition e ON a.edition_id = e.edition_id"
            );
            tableModel.setRowCount(0);
            for (List<Object> row : results) {
                tableModel.addRow(new Object[]{row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6), row.get(7)});
            }
        } catch (Exception e) {
            showError("Error loading invoices: " + e.getMessage());
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JButton getGenerateButton() { return btnGenerate; }
    public JButton getSendButton() { return btnSend; }
    public String getInvoiceNumber() { return txtInvoiceNumber.getText(); }
    public void setInvoiceNumber(String number) { txtInvoiceNumber.setText(number); }
    public String getInvoiceDate() { return txtInvoiceDate.getText(); }
    public void setInvoiceDate(String date) { txtInvoiceDate.setText(date); }
    public String getInvoiceVat() { return txtInvoiceVat.getText(); }
    public void setInvoiceVat(String vat) { txtInvoiceVat.setText(vat); }
    public String getSelectedAgreement() { return (String) agreementDropdown.getSelectedItem(); }
    public String getRecipientName() { return txtRecipientName.getText(); }
    public String getRecipientTaxId() { return txtRecipientTaxId.getText(); }
    public String getRecipientAddress() { return txtRecipientAddress.getText(); }
}