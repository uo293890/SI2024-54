package giis.demo.tkrun;

import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementStatus;
import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A simple Swing dialog for registering "other income or expense" movements.
 */
public class OtherIncomeExpenseView extends JDialog {

    private static final long serialVersionUID = 1L;

    // Combo box for Movement Type: INCOME or EXPENSE
    private JComboBox<MovementType> comboType;

    // Combo box for Movement Status: ESTIMATED or PAID
    private JComboBox<MovementStatus> comboStatus;

    // Combo box for selecting an Event (placeholder for demonstration)
    // In a real scenario, you might fill it with EventDTO objects or IDs
    private JComboBox<String> comboEvent;

    // Text fields
    private JTextField txtAmount;       // Estimated Amount
    private JTextField txtPaidAmount;   // Paid Amount (only relevant if status = PAID)
    private JTextField txtPaidDate;     // Date of payment (only if status = PAID)
    private JTextField txtConcept;      // Short description of the movement

    // Buttons
    private JButton btnRegister;
    private JButton btnCancel;

    /**
     * Constructs the dialog for registering a new "other income or expense."
     */
    public OtherIncomeExpenseView(Frame parent) {
        super(parent, "Register Other Income or Expense", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * Initialize GUI components and layout.
     */
    private void initComponents() {
        // Basic settings
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10));
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 0: Movement Type
        JLabel lblType = new JLabel("Movement Type:");
        gbc.gridx = 0; 
        gbc.gridy = 0;
        contentPanel.add(lblType, gbc);

        comboType = new JComboBox<>(MovementType.values());
        gbc.gridx = 1; 
        gbc.gridy = 0;
        contentPanel.add(comboType, gbc);

        // Row 1: Movement Status
        JLabel lblStatus = new JLabel("Movement Status:");
        gbc.gridx = 0; 
        gbc.gridy = 1;
        contentPanel.add(lblStatus, gbc);

        comboStatus = new JComboBox<>(MovementStatus.values());
        gbc.gridx = 1; 
        gbc.gridy = 1;
        contentPanel.add(comboStatus, gbc);

        // Row 2: Event selection
        JLabel lblEvent = new JLabel("Event:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(lblEvent, gbc);

        // For demo, we'll just add a few placeholder items in the combo
        // In real usage, you'd populate it dynamically (in the controller or via a method)
        comboEvent = new JComboBox<>(new String[]{"Event 1", "Event 2", "Event 3"});
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(comboEvent, gbc);

        // Row 3: Estimated Amount
        JLabel lblAmount = new JLabel("Estimated Amount:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(lblAmount, gbc);

        txtAmount = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(txtAmount, gbc);

        // Row 4: Paid Amount
        JLabel lblPaidAmount = new JLabel("Paid Amount:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(lblPaidAmount, gbc);

        txtPaidAmount = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(txtPaidAmount, gbc);

        // Row 5: Paid Date
        JLabel lblPaidDate = new JLabel("Paid Date (yyyy-MM-dd):");
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(lblPaidDate, gbc);

        txtPaidDate = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(txtPaidDate, gbc);

        // Row 6: Concept
        JLabel lblConcept = new JLabel("Concept:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(lblConcept, gbc);

        txtConcept = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPanel.add(txtConcept, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegister = new JButton("Register");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * If your system identifies events by an integer ID, you could store these IDs in the combo box
     * (perhaps as an EventDTO or a simple map). For demonstration, we return a fixed ID or the index.
     */
    public int getSelectedEventId() {
        // For demonstration, return the index as the event ID
        // or adapt this to your real data model
        return comboEvent.getSelectedIndex() + 1; // e.g., 1,2,3
    }

    // Getters for the components that the controller needs

    public JComboBox<MovementType> getComboType() {
        return comboType;
    }

    public JComboBox<MovementStatus> getComboStatus() {
        return comboStatus;
    }

    public JComboBox<String> getComboEvent() {
        return comboEvent;
    }

    public JTextField getTxtAmount() {
        return txtAmount;
    }

    public JTextField getTxtPaidAmount() {
        return txtPaidAmount;
    }

    public JTextField getTxtPaidDate() {
        return txtPaidDate;
    }

    public JTextField getTxtConcept() {
        return txtConcept;
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
}
