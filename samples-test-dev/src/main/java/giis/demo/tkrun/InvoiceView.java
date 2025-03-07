package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

public class InvoiceView extends JFrame {
    private JTextField txtInvoiceDate;
    private JTextField txtInvoiceNumber;
    private JTextField txtAgreementId;
    private JTextField txtInvoiceVat;
    private JButton btnGenerate;
    private JButton btnRegister;

    public InvoiceView() {
        initialize();
    }

    private void initialize() {
        setTitle("Generar Factura");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Fecha (dd/MM/yyyy):"));
        txtInvoiceDate = new JTextField(10);
        add(txtInvoiceDate);

        add(new JLabel("Número de Factura:"));
        txtInvoiceNumber = new JTextField(15);
        txtInvoiceNumber.setEditable(false);
        add(txtInvoiceNumber);

        add(new JLabel("ID de Acuerdo:"));
        txtAgreementId = new JTextField(10);
        add(txtAgreementId);

        add(new JLabel("IVA Factura:"));
        txtInvoiceVat = new JTextField(10);
        add(txtInvoiceVat);

        btnGenerate = new JButton("Generar");
        btnRegister = new JButton("Registrar");
        add(btnGenerate);
        add(btnRegister);
    }

    // Getters para componentes de la UI
    public JButton getGenerateButton() {
        return btnGenerate;
    }
    public JButton getRegisterButton() {
        return btnRegister;
    }
    public String getInvoiceDate() {
        return txtInvoiceDate.getText();
    }
    public String getInvoiceNumber() {
        return txtInvoiceNumber.getText();
    }
    public String getAgreementId() {
        return txtAgreementId.getText();
    }
    public String getInvoiceVat() {
        return txtInvoiceVat.getText();
    }

    // Setters
    public void setInvoiceNumber(String number) {
        txtInvoiceNumber.setText(number);
    }
    public void setInvoiceDate(String date) {
        txtInvoiceDate.setText(date);
    }

    // Métodos para mensajes
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void clearForm() {
        txtInvoiceDate.setText("");
        txtInvoiceNumber.setText("");
        txtAgreementId.setText("");
        txtInvoiceVat.setText("");
    }
}
