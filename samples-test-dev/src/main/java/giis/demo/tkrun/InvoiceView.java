package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;

public class InvoiceView extends JFrame {
    private JTextField txtInvoiceDate;
    private JTextField txtInvoiceNumber;
    private JTextField txtRecipientName;
    private JFormattedTextField txtTaxId;
    private JTextField txtAddress;
    private JButton btnGenerate;
    private JButton btnSend;

    public InvoiceView() {
        initialize();
    }

    private void initialize() {
        setTitle("Generate Invoice");
        setBounds(100, 100, 500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Date (dd/MM/yyyy):"));
        txtInvoiceDate = new JTextField(10);
        add(txtInvoiceDate);

        add(new JLabel("Invoice Number:"));
        txtInvoiceNumber = new JTextField(15);
        txtInvoiceNumber.setEditable(false);
        add(txtInvoiceNumber);

        add(new JLabel("Recipient Name:"));
        txtRecipientName = new JTextField(20);
        add(txtRecipientName);

        add(new JLabel("Tax ID:"));
        try {
            txtTaxId = new JFormattedTextField(new MaskFormatter("U########A"));
        } catch (ParseException e) {
            txtTaxId = new JFormattedTextField();
        }
        add(txtTaxId);

        add(new JLabel("Address:"));
        txtAddress = new JTextField(20);
        add(txtAddress);

        btnGenerate = new JButton("Generate");
        btnSend = new JButton("Send");
        add(btnGenerate);
        add(btnSend);
    }

    public String getInvoiceDate() { return txtInvoiceDate.getText(); }
    public String getInvoiceNumber() { return txtInvoiceNumber.getText(); }
    public String getRecipientName() { return txtRecipientName.getText(); }
    public String getTaxId() { return txtTaxId.getText(); }
    public String getAddress() { return txtAddress.getText(); }
    public JButton getGenerateButton() { return btnGenerate; }
    public JButton getSendButton() { return btnSend; }

    public void setInvoiceNumber(String number) { txtInvoiceNumber.setText(number); }
    public void setInvoiceDate(String date) { txtInvoiceDate.setText(date); }
}