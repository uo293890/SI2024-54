package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

public class InvoiceView {
    private JFrame frame;
    private JComboBox<String> activityComboBox;
    private JTextField txtInvoiceDate;
    private JTextField txtInvoiceId;
    private JTextField txtName;
    private JTextField txtTaxId;
    private JTextField txtAddress;
    private JButton btnGenerateInvoice;
    private JButton btnSendInvoice;
    private JLabel lblSentDate;

    public InvoiceView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Generate and Send Invoice");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 2, 10, 10));

        // Activity Dropdown
        frame.add(new JLabel("Activity:"));
        activityComboBox = new JComboBox<>(new String[]{"AI Congress", "Hackathon", "Workshop"});
        frame.add(activityComboBox);

        // Invoice Date
        frame.add(new JLabel("Invoice Date:"));
        txtInvoiceDate = new JTextField();
        frame.add(txtInvoiceDate);

        // Invoice ID
        frame.add(new JLabel("Invoice ID:"));
        txtInvoiceId = new JTextField();
        frame.add(txtInvoiceId);

        // Recipient Information
        frame.add(new JLabel("Name:"));
        txtName = new JTextField();
        frame.add(txtName);

        frame.add(new JLabel("Tax ID (NIF):"));
        txtTaxId = new JTextField();
        frame.add(txtTaxId);

        frame.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        frame.add(txtAddress);

        // Buttons
        btnGenerateInvoice = new JButton("Generate Invoice");
        frame.add(btnGenerateInvoice);

        btnSendInvoice = new JButton("Send via Email");
        frame.add(btnSendInvoice);

        // Sent Date
        frame.add(new JLabel("Sent date recorded:"));
        lblSentDate = new JLabel("__/__/____");
        frame.add(lblSentDate);

        frame.setVisible(true);
    }

    public JButton getBtnGenerateInvoice() {
        return btnGenerateInvoice;
    }

    public JButton getBtnSendInvoice() {
        return btnSendInvoice;
    }

    public String getActivity() {
        return (String) activityComboBox.getSelectedItem();
    }

    public String getInvoiceDate() {
        return txtInvoiceDate.getText();
    }

    public String getInvoiceId() {
        return txtInvoiceId.getText();
    }

    public String getName() {
        return txtName.getText();
    }

    public String getTaxId() {
        return txtTaxId.getText();
    }

    public String getAddress() {
        return txtAddress.getText();
    }

    public void setSentDate(String date) {
        lblSentDate.setText(date);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}