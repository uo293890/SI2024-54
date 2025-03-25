package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class RegisterSponsorshipAgreementView extends JFrame {
    private JComboBox<String> eventComboBox;
    private JComboBox<String> sponsorComboBox;
    private JComboBox<String> sponsorshipLevelComboBox;
    private JComboBox<String> gbMemberComboBox;
    private JComboBox<String> sponsorContactComboBox;
    private JTextField agreementDateField;
    private JTextField agreedAmountField;
    private JButton registerButton;
    
    private List<RegisterSponsorshipAgreementDTO> currentSponsorshipLevels;
    private List<RegisterSponsorshipAgreementDTO> currentSponsorContacts;

    public RegisterSponsorshipAgreementView() {
        initialize();
    }

    private void initialize() {
        setTitle("Sponsorship Agreement Registration");
        setSize(800, 600); // Increased window size for additional components
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Event Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Event:"), gbc);

        gbc.gridx = 1;
        eventComboBox = new JComboBox<>();
        mainPanel.add(eventComboBox, gbc);

        // Sponsor Selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Sponsor:"), gbc);

        gbc.gridx = 1;
        sponsorComboBox = new JComboBox<>();
        mainPanel.add(sponsorComboBox, gbc);

        // Sponsor Contact Selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Sponsor Contact:"), gbc);

        gbc.gridx = 1;
        sponsorContactComboBox = new JComboBox<>();
        mainPanel.add(sponsorContactComboBox, gbc);

        // GB Member Selection
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("GB Member Negotiator:"), gbc);

        gbc.gridx = 1;
        gbMemberComboBox = new JComboBox<>();
        mainPanel.add(gbMemberComboBox, gbc);

        // Sponsorship Level Selection
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Sponsorship Level:"), gbc);

        gbc.gridx = 1;
        sponsorshipLevelComboBox = new JComboBox<>();
        mainPanel.add(sponsorshipLevelComboBox, gbc);

        // Agreement Details Panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Agreement Details", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));

        GridBagConstraints gbcDetails = new GridBagConstraints();
        gbcDetails.insets = new Insets(5, 5, 5, 5);
        gbcDetails.fill = GridBagConstraints.HORIZONTAL;

        // Agreement Date
        gbcDetails.gridx = 0;
        gbcDetails.gridy = 0;
        detailsPanel.add(new JLabel("Agreement Date (YYYY-MM-DD):"), gbcDetails);

        gbcDetails.gridx = 1;
        agreementDateField = new JTextField(20);
        detailsPanel.add(agreementDateField, gbcDetails);

        // Agreed Amount
        gbcDetails.gridx = 0;
        gbcDetails.gridy = 1;
        detailsPanel.add(new JLabel("Agreed Amount:"), gbcDetails);

        gbcDetails.gridx = 1;
        agreedAmountField = new JTextField(20);
        detailsPanel.add(agreedAmountField, gbcDetails);

        // Add details panel to main panel
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(detailsPanel, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        registerButton = new JButton("Register Agreement");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 153, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        mainPanel.add(registerButton, gbc);

        add(mainPanel);
    }

    // Getters for UI components
    public JComboBox<String> getEventComboBox() { return eventComboBox; }
    public JComboBox<String> getSponsorComboBox() { return sponsorComboBox; }
    public JComboBox<String> getSponsorshipLevelComboBox() { return sponsorshipLevelComboBox; }
    public JComboBox<String> getGbMemberComboBox() { return gbMemberComboBox; }
    public JComboBox<String> getSponsorContactComboBox() { return sponsorContactComboBox; }
    public JTextField getAgreementDateField() { return agreementDateField; }
    public JTextField getAgreedAmountField() { return agreedAmountField; }
    public JButton getRegisterButton() { return registerButton; }

    // Methods to update combo boxes
    public void updateSponsorshipLevels(List<RegisterSponsorshipAgreementDTO> levels) {
        sponsorshipLevelComboBox.removeAllItems();
        this.currentSponsorshipLevels = levels;
        for (RegisterSponsorshipAgreementDTO level : levels) {
            sponsorshipLevelComboBox.addItem(level.getLevelName() + " (Min: " + level.getLevelMinAmount() + ")");
        }
    }

    public void updateSponsorContacts(List<RegisterSponsorshipAgreementDTO> contacts) {
        sponsorContactComboBox.removeAllItems();
        this.currentSponsorContacts = contacts;
        for (RegisterSponsorshipAgreementDTO contact : contacts) {
            sponsorContactComboBox.addItem(contact.getSpContactName() + " - " + contact.getSpContactEmail());
        }
    }

    // Methods to get current data
    public List<RegisterSponsorshipAgreementDTO> getCurrentSponsorshipLevels() {
        return currentSponsorshipLevels;
    }

    public List<RegisterSponsorshipAgreementDTO> getCurrentSponsorContacts() {
        return currentSponsorContacts;
    }

    // Message display methods
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Clear form method
    public void clearForm() {
        eventComboBox.setSelectedIndex(0);
        sponsorComboBox.setSelectedIndex(0);
        sponsorshipLevelComboBox.removeAllItems();
        gbMemberComboBox.setSelectedIndex(0);
        sponsorContactComboBox.removeAllItems();
        agreementDateField.setText("");
        agreedAmountField.setText("");
        currentSponsorshipLevels = null;
        currentSponsorContacts = null;
    }
}