package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PaymentView extends JDialog {
    private JTextField txtInvoiceId;
    private JTextField txtPaymentDate;
    private JTextField txtAmount;
    private JButton btnRegister;

    public PaymentView() {
        setTitle("Register Payment");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Invoice ID:"));
        txtInvoiceId = new JTextField(10);
        add(txtInvoiceId);

        add(new JLabel("Payment Date (dd/MM/yyyy):"));
        txtPaymentDate = new JTextField(10);
        add(txtPaymentDate);

        add(new JLabel("Amount:"));
        txtAmount = new JTextField(10);
        add(txtAmount);

        btnRegister = new JButton("Register Payment");
        add(btnRegister);
    }

    public int getInvoiceId() { return Integer.parseInt(txtInvoiceId.getText()); }
    public String getPaymentDate() { return txtPaymentDate.getText(); }
    public double getAmount() { return Double.parseDouble(txtAmount.getText()); }
    public JButton getRegisterButton() { return btnRegister; }
}