package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;

public class InvoiceView extends JDialog {
    private JTextField txtInvoiceDate;
    private JTextField txtInvoiceNumber;
    private JTextField txtRecipientName;
    private JTextField txtRecipientTaxId;
    private JTextField txtRecipientAddress;
    private JTextField txtBaseAmount;
    private JTextField txtVat;
    private JTextField txtAgreementId; // Campo para agreement_id
    private JButton btnGenerateInvoice;
    private JButton btnSendInvoice;

    public InvoiceView(JFrame parent) {
        super(parent, "Invoice Management", true);
        initialize();
    }

    private void initialize() {
        setSize(500, 400);
        setLayout(new GridLayout(0, 2, 10, 10));

        // Campos de entrada
        add(new JLabel("Agreement ID:"));
        txtAgreementId = new JTextField(); // Campo para agreement_id
        add(txtAgreementId);

        add(new JLabel("Invoice Date (YYYY-MM-DD):"));
        txtInvoiceDate = new JTextField();
        add(txtInvoiceDate);

        add(new JLabel("Invoice Number:"));
        txtInvoiceNumber = new JTextField();
        add(txtInvoiceNumber);

        add(new JLabel("Recipient Name:"));
        txtRecipientName = new JTextField();
        add(txtRecipientName);

        add(new JLabel("Recipient Tax ID:"));
        txtRecipientTaxId = new JTextField();
        add(txtRecipientTaxId);

        add(new JLabel("Recipient Address:"));
        txtRecipientAddress = new JTextField();
        add(txtRecipientAddress);

        add(new JLabel("Base Amount:"));
        txtBaseAmount = new JTextField();
        add(txtBaseAmount);

        add(new JLabel("VAT (%):"));
        txtVat = new JTextField();
        add(txtVat);

        // Botón para generar la factura
        btnGenerateInvoice = new JButton("Generate Invoice");
        add(btnGenerateInvoice);

        // Botón para enviar la factura
        btnSendInvoice = new JButton("Send Invoice");
        add(btnSendInvoice);

        setLocationRelativeTo(null); // Centrar la ventana
    }

    // Métodos getter para los campos de entrada
    public int getAgreementId() {
        return Integer.parseInt(txtAgreementId.getText()); // Obtener el agreement_id
    }

    public JButton getBtnGenerateInvoice() {
        return btnGenerateInvoice;
    }

    public JButton getBtnSendInvoice() {
        return btnSendInvoice;
    }

    public String getInvoiceDate() {
        return txtInvoiceDate.getText();
    }

    public String getInvoiceNumber() {
        return txtInvoiceNumber.getText();
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

    public double getBaseAmount() {
        return Double.parseDouble(txtBaseAmount.getText());
    }

    public double getVat() {
        return Double.parseDouble(txtVat.getText());
    }
}