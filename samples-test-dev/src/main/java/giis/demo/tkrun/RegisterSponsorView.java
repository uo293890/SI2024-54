package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterSponsorView extends JFrame {
    private JTextField sponsorNameField;
    private JButton addContactButton;
    private JButton registerButton;
    private JPanel contactsPanel;
    private List<ContactPanel> contactPanels;

    public RegisterSponsorView() {
        initialize();
    }
    
    private void initialize() {
        setTitle("Sponsor Registration");
        setSize(600, 500); // Adjusted size for better fit
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Sponsor details panel
        JPanel sponsorPanel = new JPanel(new GridBagLayout());
        sponsorPanel.setBorder(BorderFactory.createTitledBorder("Sponsor Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Sponsor Name
        gbc.gridx = 0; gbc.gridy = 0;
        sponsorPanel.add(new JLabel("Sponsor Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow field to expand
        sponsorNameField = new JTextField(20);
        sponsorPanel.add(sponsorNameField, gbc);
        
        mainPanel.add(sponsorPanel, BorderLayout.NORTH);
        
        // Contacts panel
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBorder(BorderFactory.createTitledBorder("Sponsor Contacts"));
        
        JScrollPane scrollPane = new JScrollPane(contactsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addContactButton = new JButton("Add Contact");
        registerButton = new JButton("Register Sponsor");
        buttonPanel.add(addContactButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize contact panels list
        contactPanels = new ArrayList<>();
        
        add(mainPanel);
    }
    
    // Inner class for contact panels
    public class ContactPanel extends JPanel {
        JTextField nameField;
        JTextField emailField;
        JButton removeButton;
        
        public ContactPanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createEtchedBorder());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;
            
            // Contact name
            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Contact Name:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0; // Allow field to expand
            nameField = new JTextField(20);
            add(nameField, gbc);
            
            // Email
            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            emailField = new JTextField(20);
            add(emailField, gbc);
            
            // Remove button - placed to the right of email field
            gbc.gridx = 2; gbc.gridy = 0; 
            gbc.gridheight = 2; // Span both rows
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            removeButton = new JButton("Remove");
            add(removeButton, gbc);
        }
        
        public JTextField getNameField() { return nameField; }
        public JTextField getEmailField() { return emailField; }
    }
    
    // Public methods 
    public void addContactPanel() {
        ContactPanel panel = new ContactPanel();
        contactPanels.add(panel);
        contactsPanel.add(panel);
        contactsPanel.revalidate();
        contactsPanel.repaint();
        
        panel.removeButton.addActionListener(e -> {
            contactsPanel.remove(panel);
            contactPanels.remove(panel);
            contactsPanel.revalidate();
            contactsPanel.repaint();
        });
    }
    
    // Getters for controller
    public JTextField getSponsorNameField() { return sponsorNameField; }
    public JButton getAddContactButton() { return addContactButton; }
    public JButton getRegisterButton() { return registerButton; }
    public List<ContactPanel> getContactPanels() { return contactPanels; }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearForm() {
        sponsorNameField.setText("");
        contactPanels.clear();
        contactsPanel.removeAll();
        contactsPanel.revalidate();
        contactsPanel.repaint();
    }
}