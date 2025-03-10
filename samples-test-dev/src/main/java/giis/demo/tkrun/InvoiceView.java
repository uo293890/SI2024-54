package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InvoiceView extends JFrame {

    private JTextField txtInvoiceNumber;
    private JTextField txtRecipientName;
    private JTextField txtRecipientTaxId;
    private JTextField txtRecipientAddress;
    private JTextField txtContactEmail;
    private JTextField txtInvoiceDate;
    private JTextField txtEventDate;
    private JTextField txtAgreementId;
    private JTextField txtInvoiceVat;

    private JButton btnGenerate;
    private JButton btnSend;

    private JTable invoicesTable;
    private DefaultTableModel invoicesTableModel;

    private JTable availableIdsTable;
    private DefaultTableModel availableIdsTableModel;

    public InvoiceView() {
        setTitle("Generate and Send Invoices");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Generate and Send Invoices to Sponsors"));

        txtInvoiceNumber = new JTextField();
        txtInvoiceNumber.setPreferredSize(new Dimension(200, 30));
        txtInvoiceNumber.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtInvoiceNumber.setToolTipText("Invoice ID (can be entered manually or selected from the table)");

        txtRecipientName = new JTextField();
        txtRecipientName.setPreferredSize(new Dimension(200, 30));
        txtRecipientName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtRecipientName.setToolTipText("Recipient Name");

        txtRecipientTaxId = new JTextField();
        txtRecipientTaxId.setPreferredSize(new Dimension(200, 30));
        txtRecipientTaxId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtRecipientTaxId.setToolTipText("Recipient NIF/CIF");

        txtRecipientAddress = new JTextField();
        txtRecipientAddress.setPreferredSize(new Dimension(200, 30));
        txtRecipientAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtRecipientAddress.setToolTipText("Recipient Fiscal Address");

        txtContactEmail = new JTextField();
        txtContactEmail.setPreferredSize(new Dimension(200, 30));
        txtContactEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtContactEmail.setToolTipText("Contact Email");

        txtInvoiceDate = new JTextField();
        txtInvoiceDate.setPreferredSize(new Dimension(200, 30));
        txtInvoiceDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtInvoiceDate.setToolTipText("Invoice Date (dd/MM/yyyy)");

        txtEventDate = new JTextField();
        txtEventDate.setPreferredSize(new Dimension(200, 30));
        txtEventDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtEventDate.setToolTipText("Event Date (dd/MM/yyyy) - Optional");

        txtAgreementId = new JTextField();
        txtAgreementId.setPreferredSize(new Dimension(200, 30));
        txtAgreementId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtAgreementId.setToolTipText("Agreement ID");

        txtInvoiceVat = new JTextField();
        txtInvoiceVat.setPreferredSize(new Dimension(200, 30));
        txtInvoiceVat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtInvoiceVat.setToolTipText("Invoice VAT");

        btnGenerate = new JButton("Generate/Register Invoice");
        btnSend = new JButton("Send Invoice");

        formPanel.add(createFieldPanel("Invoice ID:", txtInvoiceNumber));
        formPanel.add(createFieldPanel("Recipient Name:", txtRecipientName));
        formPanel.add(createFieldPanel("NIF/CIF:", txtRecipientTaxId));
        formPanel.add(createFieldPanel("Fiscal Address:", txtRecipientAddress));
        formPanel.add(createFieldPanel("Contact Email:", txtContactEmail));
        formPanel.add(createFieldPanel("Invoice Date:", txtInvoiceDate));
        formPanel.add(createFieldPanel("Event Date (Optional):", txtEventDate));
        formPanel.add(createFieldPanel("Agreement ID:", txtAgreementId));
        formPanel.add(createFieldPanel("Invoice VAT:", txtInvoiceVat));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.add(btnGenerate);
        buttonsPanel.add(btnSend);
        formPanel.add(buttonsPanel);

        JPanel invoicesPanel = new JPanel(new BorderLayout());
        invoicesPanel.setBorder(BorderFactory.createTitledBorder("Generated Invoices"));
        invoicesTableModel = new DefaultTableModel(new Object[]{
                "Invoice ID", "Name", "NIF/CIF", "Address", "Contact Email", "Invoice Date", "Sent Date", "Actions"
        }, 0);
        invoicesTable = new JTable(invoicesTableModel);
        JScrollPane invoicesScroll = new JScrollPane(invoicesTable);
        invoicesPanel.add(invoicesScroll, BorderLayout.CENTER);

        JPanel idsPanel = new JPanel(new BorderLayout());
        idsPanel.setBorder(BorderFactory.createTitledBorder("Available IDs (Excel-like View)"));
        availableIdsTableModel = new DefaultTableModel(new Object[]{"ID", "Description"}, 0);
        availableIdsTable = new JTable(availableIdsTableModel);
        JScrollPane idsScroll = new JScrollPane(availableIdsTable);
        idsPanel.add(idsScroll, BorderLayout.CENTER);

        availableIdsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = availableIdsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object id = availableIdsTableModel.getValueAt(selectedRow, 0);
                    txtInvoiceNumber.setText(id.toString());
                }
            }
        });

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(invoicesPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(idsPanel);

        add(mainPanel);
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(180, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // Getter para acceder a la tabla de facturas
    public JTable getInvoicesTable() {
        return invoicesTable;
    }

    public JButton getGenerateButton() {
        return btnGenerate;
    }

    public JButton getSendButton() {
        return btnSend;
    }

    public String getInvoiceNumber() {
        return txtInvoiceNumber.getText();
    }
    public void setInvoiceNumber(String number) {
        txtInvoiceNumber.setText(number);
    }

    public String getInvoiceDate() {
        return txtInvoiceDate.getText();
    }
    public void setInvoiceDate(String date) {
        txtInvoiceDate.setText(date);
    }

    public String getRecipientName() {
        return txtRecipientName.getText();
    }

    public String getRecipientTaxId() {
        return txtRecipientTaxId.getText();
    }

    public String getRecipientAddress() {
        return txtRecipientAddress.getText();
    }

    public String getContactEmail() {
        return txtContactEmail.getText();
    }

    public String getEventDate() {
        return txtEventDate.getText();
    }

    public String getAgreementId() {
        return txtAgreementId.getText();
    }

    public String getInvoiceVat() {
        return txtInvoiceVat.getText();
    }

    public void addInvoiceToTable(String id, String name, String taxId, String address, String email, String invoiceDate, String sentDate) {
        invoicesTableModel.addRow(new Object[]{id, name, taxId, address, email, invoiceDate, sentDate, "Enviar"});
    }

    /**
     * Actualiza la tabla de ID Disponibles con nuevos datos.
     * @param data Matriz de datos, donde cada fila es un array con el ID y su descripción.
     */
    public void updateAvailableIdsTable(Object[][] data) {
        availableIdsTableModel.setRowCount(0);  // Limpia la tabla
        for (Object[] row : data) {
            availableIdsTableModel.addRow(row);
        }
    }
    
    
    

    /**
     * Método getter para acceder a la tabla de IDs disponibles.
     */
    public JTable getAvailableIdsTable() {
        return availableIdsTable;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearForm() {
        txtInvoiceNumber.setText("");
        txtRecipientName.setText("");
        txtRecipientTaxId.setText("");
        txtRecipientAddress.setText("");
        txtContactEmail.setText("");
        txtInvoiceDate.setText("");
        txtEventDate.setText("");
        txtAgreementId.setText("");
        txtInvoiceVat.setText("");
    }
}
