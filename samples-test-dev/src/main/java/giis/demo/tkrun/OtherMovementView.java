package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the view for registering other income and expenses.
 * It provides a user interface for entering movement details such as edition ID,
 * concept, amount, type (Income/Expense), and payment date (if applicable).
 */
public class OtherMovementView extends JDialog {
    private JTextField txtEditionId; // Field for entering edition ID
    private JTextField txtConcept; // Field for entering the concept/description
    private JTextField txtAmount; // Field for entering the amount
    private JComboBox<String> typeCombo; // Dropdown for selecting type (Income/Expense)
    private JTextField txtPaymentDate; // Field for entering the payment date
    private JButton btnRegister; // Button to register the movement

    /**
     * Constructor to initialize the view.
     */
    public OtherMovementView() {
        setTitle("Register Other Income/Expense");
        setSize(500, 400);
        setLayout(new GridLayout(6, 2, 5, 5));

        // Add components to the view
        add(new JLabel("Edition ID:"));
        txtEditionId = new JTextField(10);
        add(txtEditionId);

        add(new JLabel("Concept:"));
        txtConcept = new JTextField(20);
        add(txtConcept);

        add(new JLabel("Amount:"));
        txtAmount = new JTextField(10);
        add(txtAmount);

        add(new JLabel("Type:"));
        typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
        add(typeCombo);

        // Payment date field (initially hidden)
        add(new JLabel("Payment Date (dd/MM/yyyy):"));
        txtPaymentDate = new JTextField(10);
        txtPaymentDate.setEnabled(false); // Disabled by default
        add(txtPaymentDate);

        btnRegister = new JButton("Register Movement");
        add(btnRegister);

        // Add action listener to the register button
        btnRegister.addActionListener(e -> registerMovement());
    }

    // Getters for retrieving user input
    public int getEditionId() { return Integer.parseInt(txtEditionId.getText()); }
    public String getConcept() { return txtConcept.getText(); }
    public double getAmount() { return Double.parseDouble(txtAmount.getText()); }
    public String getMovementType() { return (String) typeCombo.getSelectedItem(); }
    public String getPaymentDate() { return txtPaymentDate.getText(); }
    public JButton getRegisterButton() { return btnRegister; }

    /**
     * Handles the registration of a movement.
     */
    private void registerMovement() {
        // Ask the user if the movement has been paid
        int option = JOptionPane.showConfirmDialog(
            this,
            "Has this movement been paid?",
            "Payment Status",
            JOptionPane.YES_NO_OPTION
        );

        String status;
        if (option == JOptionPane.YES_OPTION) {
            // If paid, enable the payment date field and prompt the user to enter the date
            status = "Paid";
            txtPaymentDate.setEnabled(true);
            String paymentDate = JOptionPane.showInputDialog(
                this,
                "Enter the payment date (dd/MM/yyyy):",
                "Payment Date",
                JOptionPane.QUESTION_MESSAGE
            );

            if (paymentDate != null && !paymentDate.isEmpty()) {
                txtPaymentDate.setText(paymentDate);
            } else {
                JOptionPane.showMessageDialog(this, "Payment date is required for paid movements.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop registration if payment date is not provided
            }
        } else {
            // If not paid, set status to "Estimated" and clear the payment date field
            status = "Estimated";
            txtPaymentDate.setText("");
            txtPaymentDate.setEnabled(false);
        }

        // Notify the user that the movement is being registered
        JOptionPane.showMessageDialog(this, "Movement registered successfully with status: " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}