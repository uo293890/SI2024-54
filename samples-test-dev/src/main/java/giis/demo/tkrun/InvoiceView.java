package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

public class InvoiceView extends JFrame {
    private JTextField txtInvoiceDate;
    private JTextField txtInvoiceNumber;
    private JTextField txtAgreementId;
    private JTextField txtInvoiceVat;
    
    // Campos adicionales para los datos fiscales del receptor (no se almacenan en DB)
    private JTextField txtRecipientName;
    private JTextField txtRecipientFiscalNumber;
    private JTextField txtRecipientAddress;
    private JTextField txtRecipientEmail;
    
    // Campo para la fecha del evento (para validar la regla de 4 semanas)
    private JTextField txtEventDate;
    
    private JButton btnGenerate;
    private JButton btnRegister;
    private JButton btnSend; // Botón para enviar la factura por email

    public InvoiceView() {
        initialize();
    }

    private void initialize() {
        setTitle("Generación y Envío de Facturas");
        setBounds(100, 100, 500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Se usa GridLayout para disponer todos los campos y botones
        setLayout(new GridLayout(11, 2, 5, 5));

        add(new JLabel("Invoice Date (dd/MM/yyyy):"));
        txtInvoiceDate = new JTextField(10);
        add(txtInvoiceDate);

        add(new JLabel("Invoice Number:"));
        txtInvoiceNumber = new JTextField(15);
        txtInvoiceNumber.setEditable(false);
        add(txtInvoiceNumber);

        add(new JLabel("Agreement ID:"));
        txtAgreementId = new JTextField(10);
        add(txtAgreementId);

        add(new JLabel("Invoice VAT:"));
        txtInvoiceVat = new JTextField(10);
        add(txtInvoiceVat);
        
        add(new JLabel("Recipient Name:"));
        txtRecipientName = new JTextField(20);
        add(txtRecipientName);
        
        add(new JLabel("Recipient Fiscal Number:"));
        txtRecipientFiscalNumber = new JTextField(15);
        add(txtRecipientFiscalNumber);
        
        add(new JLabel("Recipient Address:"));
        txtRecipientAddress = new JTextField(30);
        add(txtRecipientAddress);
        
        add(new JLabel("Recipient Email:"));
        txtRecipientEmail = new JTextField(20);
        add(txtRecipientEmail);
        
        add(new JLabel("Event Date (dd/MM/yyyy):"));
        txtEventDate = new JTextField(10);
        add(txtEventDate);

        // Panel para los botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnGenerate = new JButton("Generate");
        btnRegister = new JButton("Register");
        btnSend = new JButton("Send Invoice");
        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnSend);
        add(buttonPanel);
        add(new JLabel("")); // Relleno para completar el grid
    }

    // Getters para los componentes de la UI
    public JButton getGenerateButton() { return btnGenerate; }
    public JButton getRegisterButton() { return btnRegister; }
    public JButton getSendButton() { return btnSend; }
    
    public String getInvoiceDate() { return txtInvoiceDate.getText(); }
    public String getInvoiceNumber() { return txtInvoiceNumber.getText(); }
    public String getAgreementId() { return txtAgreementId.getText(); }
    public String getInvoiceVat() { return txtInvoiceVat.getText(); }
    
    public String getRecipientName() { return txtRecipientName.getText(); }
    public String getRecipientFiscalNumber() { return txtRecipientFiscalNumber.getText(); }
    public String getRecipientAddress() { return txtRecipientAddress.getText(); }
    public String getRecipientEmail() { return txtRecipientEmail.getText(); }
    public String getEventDate() { return txtEventDate.getText(); }
    
    // Setters para actualizar la UI
    public void setInvoiceNumber(String number) { txtInvoiceNumber.setText(number); }
    public void setInvoiceDate(String date) { txtInvoiceDate.setText(date); }
    
    // Métodos para mostrar mensajes
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void clearForm() {
        txtInvoiceDate.setText("");
        txtInvoiceNumber.setText("");
        txtAgreementId.setText("");
        txtInvoiceVat.setText("");
        txtRecipientName.setText("");
        txtRecipientFiscalNumber.setText("");
        txtRecipientAddress.setText("");
        txtRecipientEmail.setText("");
        txtEventDate.setText("");
    }
}
