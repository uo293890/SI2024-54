package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RegisterSponsorshipAgreementView extends JFrame {
    private JComboBox<String> eventComboBox;
    private JComboBox<String> sponsorComboBox;
    private JTextField nameField;
    private JTextField numberField;
    private JTextField emailField;
    private JComboBox<String> gbMemberComboBox;
    private JTextField agreementDateField;
    private JTextField agreedAmountField;
    private JCheckBox sendEmailCheckBox;
    private JButton registerButton;

    public RegisterSponsorshipAgreementView() {
        initialize();
    }

    private void initialize() {
        setTitle("Register Sponsorship Agreement");
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

        // Add Contact Name Field
        gbcContact.gridx = 0;
        gbcContact.gridy = 0;
        contactPanel.add(new JLabel("Name:"), gbcContact);

        gbcContact.gridx = 1;
        nameField = new JTextField(20);
        contactPanel.add(nameField, gbcContact);

        // Add Contact Number Field
        gbcContact.gridx = 0;
        gbcContact.gridy = 1;
        contactPanel.add(new JLabel("Number:"), gbcContact);

        gbcContact.gridx = 1;
        numberField = new JTextField(20);
        contactPanel.add(numberField, gbcContact);

        // Add Contact Email Field
        gbcContact.gridx = 0;
        gbcContact.gridy = 2;
        contactPanel.add(new JLabel("Email:"), gbcContact);

        gbcContact.gridx = 1;
        emailField = new JTextField(20);
        contactPanel.add(emailField, gbcContact);

        // Add the Contact Panel to the main layout
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span across two columns
        add(contactPanel, gbc);
        
        // Add GB Member ComboBox
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Governing Board Member:"), gbc);

        gbc.gridx = 1;
        gbMemberComboBox = new JComboBox<>();
        add(gbMemberComboBox, gbc);

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
        
        /* FUTURE SPRINT ADDITION
        // Add the "Send Email" Checkbox
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Span across two columns
        sendEmailCheckBox = new JCheckBox("Send Email to GB Member");
        sendEmailCheckBox.setSelected(true); // Default to checked
        add(sendEmailCheckBox, gbc);
        */

        // Add Register Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Span across two columns
        registerButton = new JButton("Register Agreement");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 153, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        add(registerButton, gbc);
        
        numberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '+') {
                	showError("Phone number must be +00123456789");
                    evt.consume(); // Ignore the event if it's not a digit
                }
            }
        });

        agreementDateField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '-') {
                	showError("Date must be YYYY-MM-DD");
                    evt.consume(); // Allow only digits and hyphen (-) for date
                }
            }
        });

        agreedAmountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                	showError("You can only type numbers in format 0 or 0.0");
                    evt.consume(); // Allow only numbers and decimal point
                }
            }
        });

    }

    // Getters for UI components
    public JComboBox<String> getEventComboBox() { return eventComboBox; }
    public JComboBox<String> getSponsorComboBox() { return sponsorComboBox; }
    public JTextField getContactWorkerField() { return nameField; }
    public JTextField getContactNumberField() { return numberField; }
    public JTextField getContactEmailField() { return emailField; }
    public JComboBox<String> getGBMemberComboBox() { return gbMemberComboBox; }
    public JTextField getAgreementDateField() { return agreementDateField; }
    public JTextField getAgreedAmountField() { return agreedAmountField; }
    public JCheckBox getSendEmailCheckBox() { return sendEmailCheckBox; }
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
        nameField.setText("");
        numberField.setText("");
        emailField.setText("");
        gbMemberComboBox.setSelectedIndex(0);
        agreementDateField.setText("");
        agreedAmountField.setText("");
    }
}