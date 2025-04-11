package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegisterEventView extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField nameField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField locationField;
    private JButton addLevelButton;
    private JButton registerButton;
    private JPanel levelsPanel;
    private List<LevelPanel> levelPanels;

	public RegisterEventView() {
        initialize();
    }
    
    private void initialize() {
        setTitle("Event Registration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Event details panel
        JPanel eventPanel = new JPanel(new GridBagLayout());
        eventPanel.setBorder(BorderFactory.createTitledBorder("Event Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Type
        gbc.gridx = 0; gbc.gridy = 0;
        eventPanel.add(new JLabel("Event Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        typeComboBox = new JComboBox<>();
        eventPanel.add(typeComboBox, gbc);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        eventPanel.add(new JLabel("Event Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        eventPanel.add(nameField, gbc);
        
        // Start Date
        gbc.gridx = 0; gbc.gridy = 2;
        eventPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        startDateField = new JTextField(LocalDate.now().toString(), 20);
        eventPanel.add(startDateField, gbc);
        
        // End Date
        gbc.gridx = 0; gbc.gridy = 3;
        eventPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        endDateField = new JTextField(LocalDate.now().toString(), 20);
        eventPanel.add(endDateField, gbc);
        
        // Location
        gbc.gridx = 0; gbc.gridy = 4;
        eventPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(20);
        eventPanel.add(locationField, gbc);
        
        mainPanel.add(eventPanel, BorderLayout.NORTH);
        
        // Levels panel
        levelsPanel = new JPanel();
        levelsPanel.setLayout(new BoxLayout(levelsPanel, BoxLayout.Y_AXIS));
        levelsPanel.setBorder(BorderFactory.createTitledBorder("Sponsorship Levels"));
        
        JScrollPane scrollPane = new JScrollPane(levelsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addLevelButton = new JButton("Add Sponsorship Level");
        registerButton = new JButton("Register Event");
        buttonPanel.add(addLevelButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize level panels list
        levelPanels = new ArrayList<>();
        
        add(mainPanel);
    }
    
    // Inner class for level panels
    public class LevelPanel extends JPanel {
        JTextField nameField;
        JTextField minAmountField;
        JButton removeButton;
        
        public LevelPanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createEtchedBorder());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;
            
            // Level name
            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Level Name:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            nameField = new JTextField(15);
            add(nameField, gbc);
            
            // Minimum amount
            gbc.gridx = 2; gbc.gridy = 0;
            add(new JLabel("Minimum Amount:"), gbc);
            gbc.gridx = 3;
            minAmountField = new JTextField(10);
            add(minAmountField, gbc);
            
            // Remove button
            gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            removeButton = new JButton("Remove");
            add(removeButton, gbc);
        }
        
        public JTextField getNameField() { return nameField; }
        public JTextField getMinAmountField() { return minAmountField; }
    }
    
    // Public methods 
    public void addLevelPanel() {
        LevelPanel panel = new LevelPanel();
        levelPanels.add(panel);
        levelsPanel.add(panel);
        levelsPanel.revalidate();
        levelsPanel.repaint();
        
        panel.removeButton.addActionListener(e -> {
            levelsPanel.remove(panel);
            levelPanels.remove(panel);
            levelsPanel.revalidate();
            levelsPanel.repaint();
        });
    }
    
    // Getters for controller
    public JComboBox<String> getTypeComboBox() { return typeComboBox; }
    public JTextField getNameField() { return nameField; }
    public JTextField getStartDateField() { return startDateField; }
    public JTextField getEndDateField() { return endDateField; }
    public JTextField getLocationField() { return locationField; }
    public JButton getAddLevelButton() { return addLevelButton; }
    public JButton getRegisterButton() { return registerButton; }
	public List<LevelPanel> getLevelPanels() { return levelPanels; }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

	public void clearForm() {
		// TODO Auto-generated method stub
		
	}
}