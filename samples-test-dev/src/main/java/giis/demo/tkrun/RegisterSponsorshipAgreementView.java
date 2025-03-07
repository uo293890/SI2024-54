package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RegisterSponsorshipAgreementView extends JFrame {
    private JComboBox<String> eventComboBox;
    private JComboBox<String> sponsorComboBox;
    private JTextField negotiatorField;
    private JTextField contactWorkerField;
    private JTextField contactNumberField;
    private JTextField contactEmailField;
    private JTextField agreementDateField;
    private JTextField agreedAmountField;
    private JButton registerButton;

    public RegisterSponsorshipAgreementView() {
        initialize();
    }

    private void initialize() {
        setTitle("Sponsorship Agreement Registration");
        setSize(700, 500); // Adjusted window size for better spacing
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Use GridBagLayout for flexible and organized layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Add Event ComboBox
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Event:"), gbc);

        gbc.gridx = 1;
        eventComboBox = new JComboBox<>();
        add(eventComboBox, gbc);

        // Add Sponsor ComboBox
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Sponsor:"), gbc);

        gbc.gridx = 1;
        sponsorComboBox = new JComboBox<>();
        add(sponsorComboBox, gbc);

        // Add Negotiator Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Negotiator:"), gbc);

        gbc.gridx = 1;
        negotiatorField = new JTextField(20);
        add(negotiatorField, gbc);

        // Create a panel for Sponsor Contact Information
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new GridBagLayout());
        contactPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Sponsor Contact Information", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));

        GridBagConstraints gbcContact = new GridBagConstraints();
        gbcContact.insets = new Insets(5, 5, 5, 5); // Add padding
        gbcContact.fill = GridBagConstraints.HORIZONTAL;

        // Add Contact Worker Field
        gbcContact.gridx = 0;
        gbcContact.gridy = 0;
        contactPanel.add(new JLabel("Contact Worker:"), gbcContact);

        gbcContact.gridx = 1;
        contactWorkerField = new JTextField(20);
        contactPanel.add(contactWorkerField, gbcContact);

        // Add Contact Number Field
        gbcContact.gridx = 0;
        gbcContact.gridy = 1;
        contactPanel.add(new JLabel("Contact Number:"), gbcContact);

        gbcContact.gridx = 1;
        contactNumberField = new JTextField(20);
        contactPanel.add(contactNumberField, gbcContact);

        // Add Contact Email Field
        gbcContact.gridx = 0;
        gbcContact.gridy = 2;
        contactPanel.add(new JLabel("Contact Email:"), gbcContact);

        gbcContact.gridx = 1;
        contactEmailField = new JTextField(20);
        contactPanel.add(contactEmailField, gbcContact);

        // Add the Contact Panel to the main layout
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across two columns
        add(contactPanel, gbc);

        // Add Agreement Date Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1; // Reset gridwidth
        add(new JLabel("Agreement Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        agreementDateField = new JTextField(20);
        add(agreementDateField, gbc);

        // Add Agreed Amount Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Agreed Amount:"), gbc);

        gbc.gridx = 1;
        agreedAmountField = new JTextField(20);
        add(agreedAmountField, gbc);

        // Add Register Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Span across two columns
        registerButton = new JButton("Register Agreement");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 153, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        add(registerButton, gbc);
    }

    // Getters for UI components
    public JComboBox<String> getEventComboBox() { return eventComboBox; }
    public JComboBox<String> getSponsorComboBox() { return sponsorComboBox; }
    public JTextField getNegotiatorField() { return negotiatorField; }
    public JTextField getContactWorkerField() { return contactWorkerField; }
    public JTextField getContactNumberField() { return contactNumberField; }
    public JTextField getContactEmailField() { return contactEmailField; }
    public JTextField getAgreementDateField() { return agreementDateField; }
    public JTextField getAgreedAmountField() { return agreedAmountField; }
    public JButton getRegisterButton() { return registerButton; }

    // Method to display a success message
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to display an error message
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Method to clear all input fields
    public void clearForm() {
        eventComboBox.setSelectedIndex(0);
        sponsorComboBox.setSelectedIndex(0);
        negotiatorField.setText("");
        contactWorkerField.setText("");
        contactNumberField.setText("");
        contactEmailField.setText("");
        agreementDateField.setText("");
        agreedAmountField.setText("");
    }
}