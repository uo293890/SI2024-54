package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

public class SponsorshipAgreementRegistrationView extends JFrame {
    private JComboBox<String> eventComboBox;
    private JComboBox<String> sponsorComboBox;
    private JTextField negotiatorField;
    private JTextField agreementDateField;
    private JTextField agreedAmountField;
    private JTextField sponsorContactNameField;
    private JTextField sponsorContactEmailField;
    private JTextField sponsorContactPhoneField;
    private JButton registerButton;

    public SponsorshipAgreementRegistrationView() {
        initialize();
    }

    private void initialize() {
        setTitle("Sponsorship Agreement Registration");
        setSize(700, 400); // Wider window to accommodate longer text boxes
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen (optional)

        // Use a GridLayout for a simple, aligned layout
        setLayout(new GridLayout(9, 2, 10, 10)); // 9 rows, 2 columns, with spacing

        // Add components to the form
        add(new JLabel("Event:"));
        eventComboBox = new JComboBox<>();
        add(eventComboBox);

        add(new JLabel("Sponsor:"));
        sponsorComboBox = new JComboBox<>();
        add(sponsorComboBox);

        add(new JLabel("Negotiator:"));
        negotiatorField = new JTextField(20); // Longer text field
        add(negotiatorField);

        add(new JLabel("Agreement Date (YYYY-MM-DD):"));
        agreementDateField = new JTextField(20); // Longer text field
        add(agreementDateField);

        add(new JLabel("Agreed Amount:"));
        agreedAmountField = new JTextField(20); // Longer text field
        add(agreedAmountField);

        add(new JLabel("Sponsor Contact Name:"));
        sponsorContactNameField = new JTextField(20); // Longer text field
        add(sponsorContactNameField);

        add(new JLabel("Sponsor Contact Email:"));
        sponsorContactEmailField = new JTextField(20); // Longer text field
        add(sponsorContactEmailField);

        add(new JLabel("Sponsor Contact Phone:"));
        sponsorContactPhoneField = new JTextField(20); // Longer text field
        add(sponsorContactPhoneField);

        // Add a placeholder to align the button properly
        add(new JLabel("")); // Empty label for spacing
        registerButton = new JButton("Register Agreement");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set a nicer font
        registerButton.setBackground(new Color(0, 153, 204)); // Set a blue background
        registerButton.setForeground(Color.WHITE); // Set white text
        registerButton.setFocusPainted(false); // Remove the focus border
        add(registerButton);
    }

    // Getters for UI components
    public JComboBox<String> getEventComboBox() { return eventComboBox; }
    public JComboBox<String> getSponsorComboBox() { return sponsorComboBox; }
    public JTextField getNegotiatorField() { return negotiatorField; }
    public JTextField getAgreementDateField() { return agreementDateField; }
    public JTextField getAgreedAmountField() { return agreedAmountField; }
    public JTextField getSponsorContactNameField() { return sponsorContactNameField; }
    public JTextField getSponsorContactEmailField() { return sponsorContactEmailField; }
    public JTextField getSponsorContactPhoneField() { return sponsorContactPhoneField; }
    public JButton getRegisterButton() { return registerButton; }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}