package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import giis.demo.util.Database;

public class InvoiceView extends JFrame {
    private JTextField txtInvoiceNumber, txtRecipientName, txtRecipientTaxId, txtRecipientAddress, txtContactEmail, txtInvoiceDate, txtInvoiceVat;
    private JComboBox<String> activityDropdown;
    private JComboBox<String> agreementDropdown;
    private JButton btnGenerate, btnSend;
    private JTable invoicesTable;
    private DefaultTableModel invoicesTableModel;
    private Database db = new Database();

    public InvoiceView() {
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadActivitiesFromDatabase();
        loadAgreementsFromDatabase();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Generate Invoice"));

        txtInvoiceNumber = new JTextField();
        txtRecipientName = new JTextField();
        txtRecipientTaxId = new JTextField();
        txtRecipientAddress = new JTextField();
        txtContactEmail = new JTextField();
        txtInvoiceDate = new JTextField();
        txtInvoiceVat = new JTextField();
        activityDropdown = new JComboBox<>();
        agreementDropdown = new JComboBox<>();

        btnGenerate = new JButton("Generate Invoice");
        btnSend = new JButton("Send Invoice");

        formPanel.add(new JLabel("Invoice ID:")); formPanel.add(txtInvoiceNumber);
        formPanel.add(new JLabel("Recipient Name:")); formPanel.add(txtRecipientName);
        formPanel.add(new JLabel("Recipient Tax ID:")); formPanel.add(txtRecipientTaxId);
        formPanel.add(new JLabel("Recipient Address:")); formPanel.add(txtRecipientAddress);
        formPanel.add(new JLabel("Contact Email:")); formPanel.add(txtContactEmail);
        formPanel.add(new JLabel("Invoice Date:")); formPanel.add(txtInvoiceDate);
        formPanel.add(new JLabel("Invoice VAT:")); formPanel.add(txtInvoiceVat);
        formPanel.add(new JLabel("Select Activity:")); formPanel.add(activityDropdown);
        formPanel.add(new JLabel("Select Agreement:")); formPanel.add(agreementDropdown);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnSend);
        
        formPanel.add(buttonPanel);

        JPanel invoicesPanel = new JPanel(new BorderLayout());
        invoicesPanel.setBorder(BorderFactory.createTitledBorder("Invoices"));
        invoicesTableModel = new DefaultTableModel(new Object[]{
                "Invoice ID", "Recipient", "Tax ID", "Address", "Email", "Date", "VAT", "Actions"
        }, 0);
        invoicesTable = new JTable(invoicesTableModel);
        JScrollPane invoicesScroll = new JScrollPane(invoicesTable);
        invoicesPanel.add(invoicesScroll, BorderLayout.CENTER);

        mainPanel.add(formPanel);
        mainPanel.add(invoicesPanel);
        add(mainPanel);
    }

    private void loadActivitiesFromDatabase() {
        try {
            List<List<Object>> results = db.executeQuery("SELECT activity_id, activity_name FROM Activity");
            activityDropdown.removeAllItems();
            for (List<Object> row : results) {
                activityDropdown.addItem(row.get(0) + " - " + row.get(1));
            }
        } catch (Exception e) {
            showError("Error loading activities: " + e.getMessage());
        }
    }

    private void loadAgreementsFromDatabase() {
        try {
            List<List<Object>> results = db.executeQuery("SELECT agreement_id, agreement_name FROM Agreement");
            agreementDropdown.removeAllItems();
            for (List<Object> row : results) {
                agreementDropdown.addItem(row.get(0) + " - " + row.get(1));
            }
        } catch (Exception e) {
            showError("Error loading agreements: " + e.getMessage());
        }
    }

    public JButton getGenerateButton() { return btnGenerate; }
    public JButton getSendButton() { return btnSend; }
    public String getInvoiceNumber() { return txtInvoiceNumber.getText(); }
    public void setInvoiceNumber(String number) { txtInvoiceNumber.setText(number); }
    public String getInvoiceDate() { return txtInvoiceDate.getText(); }
    public void setInvoiceDate(String date) { txtInvoiceDate.setText(date); }
    public String getRecipientName() { return txtRecipientName.getText(); }
    public String getRecipientTaxId() { return txtRecipientTaxId.getText(); }
    public String getRecipientAddress() { return txtRecipientAddress.getText(); }
    public String getContactEmail() { return txtContactEmail.getText(); }
    public String getInvoiceVat() { return txtInvoiceVat.getText(); }
    public String getSelectedActivity() { return (String) activityDropdown.getSelectedItem(); }
    public String getSelectedAgreement() { return (String) agreementDropdown.getSelectedItem(); }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
