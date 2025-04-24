package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegisterPaymentView extends JDialog {
    private static final long serialVersionUID = 1L;

    // Tables for Pending Agreements, Paid Agreements, and Payment History
    private JTable tableAgreements;         // Pending agreements
    private JTable tablePaidAgreements;        // Paid agreements
    private JTable tablePreviousPayments;     // Payment history

    // Fields for payment registration
    private JTextField txtPaymentDate;
    private JTextField txtPaymentAmount;
    private JTextField txtPaymentConcept; // Optional field

    // Labels for validation messages and summary
    private JLabel lblValidation;
    private JLabel lblRemainingAmount;
    private JLabel lblSummary;

    // Buttons for actions
    private JButton btnRegister;
    private JButton btnCancel;

    // Variable for managing payment amounts - used by controller to track remaining
    private double currentRemainingAmount = 0.0;

    public RegisterPaymentView() {
        setTitle("Sponsor Payment Registration");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600)); // Added minimum size
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding

        // Create a JTabbedPane to separate Pending and Paid agreements
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(700, 400)); // Preferred size for tables

        // Panel for Pending Agreements
        JPanel pendingPanel = new JPanel(new BorderLayout(10, 10));
        tableAgreements = new JTable();
        tableAgreements.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPending = new JScrollPane(tableAgreements);
        scrollPending.setBorder(BorderFactory.createTitledBorder("Pending Agreements"));
        pendingPanel.add(scrollPending, BorderLayout.CENTER);
        tabbedPane.addTab("Pending", pendingPanel);

        // Panel for Paid Agreements
        JPanel paidPanel = new JPanel(new BorderLayout(10, 10));
        tablePaidAgreements = new JTable();
        tablePaidAgreements.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPaid = new JScrollPane(tablePaidAgreements);
        scrollPaid.setBorder(BorderFactory.createTitledBorder("Paid Agreements"));
        paidPanel.add(scrollPaid, BorderLayout.CENTER);
        tabbedPane.addTab("Paid", paidPanel);

        // Panel for Payment History
        JPanel previousPaymentsPanel = new JPanel(new BorderLayout(10, 10));
        tablePreviousPayments = new JTable();
        tablePreviousPayments.setEnabled(false); // Make history table non-interactive

        JScrollPane scrollPayments = new JScrollPane(tablePreviousPayments);
        scrollPayments.setBorder(BorderFactory.createTitledBorder("Payment History"));
        scrollPayments.setPreferredSize(new Dimension(700, 200)); // Give history table some size
        previousPaymentsPanel.add(scrollPayments, BorderLayout.CENTER);

        // Central panel that groups the tabbed pane and payment history
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        centerPanel.add(previousPaymentsPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Right panel for the payment registration form
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(new TitledBorder("New Payment"));
        rightPanel.setPreferredSize(new Dimension(350, 0)); // Fixed width for the right panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Adjusted insets
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Initialize the payment date - CONTROLLER WILL SET THE ACTUAL WORKING DATE
        txtPaymentDate = new JTextField();
        txtPaymentAmount = new JTextField();
        txtPaymentConcept = new JTextField(); // Concept is optional

        lblValidation = new JLabel();
        lblValidation.setPreferredSize(new Dimension(300, 20)); // Give validation label size
        lblRemainingAmount = new JLabel();
        lblRemainingAmount.setPreferredSize(new Dimension(300, 20)); // Give remaining label size
        lblSummary = new JLabel();
        lblSummary.setPreferredSize(new Dimension(300, 20)); // Give summary label size

        addLabelField(formPanel, gbc, row++, "Payment Date (YYYY-MM-DD):", txtPaymentDate);
        addLabelField(formPanel, gbc, row++, "Payment Amount (€):", txtPaymentAmount);
        addLabelField(formPanel, gbc, row++, "Payment Concept (Optional):", txtPaymentConcept); // Updated label text

        // Add labels directly without label fields to control gridbag layout better
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(new JSeparator(), gbc); // Separator
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(new JLabel("Validation Status:"), gbc);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(lblValidation, gbc);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(new JSeparator(), gbc); // Separator
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(new JLabel("Calculated Remaining (€):"), gbc);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(lblRemainingAmount, gbc);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(new JSeparator(), gbc); // Separator
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(new JLabel("Agreement Summary:"), gbc);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; formPanel.add(lblSummary, gbc);


        // Buttons
        btnRegister = new JButton("Register Payment");
        btnRegister.setEnabled(false); // Start disabled until an agreement is selected and inputs are valid
        btnCancel = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Adjusted flow layout
        buttonPanel.setBorder(new EmptyBorder(10,0,0,0)); // Add space above buttons
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.add(formPanel, BorderLayout.NORTH); // Use NORTH to prevent fields from stretching vertically
        paymentPanel.add(buttonPanel, BorderLayout.SOUTH);

        rightPanel.add(paymentPanel, BorderLayout.NORTH); // Use NORTH to prevent the panel from stretching vertically
        add(rightPanel, BorderLayout.EAST);

        // Ensure remaining amount label is updated when currentRemainingAmount is set
        lblRemainingAmount.setText(String.format("%.2f", currentRemainingAmount));

    }

    // Helper method for adding labels and fields using GridBagLayout
    private void addLabelField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.0; // Labels don't grow
        gbc.anchor = GridBagConstraints.WEST; // Align label to the west
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Field takes remaining space
        gbc.anchor = GridBagConstraints.EAST; // Align field to the east
        panel.add(field, gbc);
    }


    public void setCurrentRemainingAmount(double amount) {
        this.currentRemainingAmount = amount;
        // Update the label immediately when this is set (typically on agreement selection)
        // Note: updateValidation will also update this based on entered amount
        // lblRemainingAmount.setText(String.format("%.2f €", amount));
    }


    // Getters for components (used by the controller)
    public JTable getTableAgreements() {
        return tableAgreements;
    }

    public JTable getTablePaidAgreements() {
        return tablePaidAgreements;
    }

    public JTable getTablePreviousPayments() {
        return tablePreviousPayments;
    }

    public JTextField getTxtPaymentDate() {
        return txtPaymentDate;
    }

    public JTextField getTxtPaymentAmount() {
        return txtPaymentAmount;
    }

    public JTextField getTxtPaymentConcept() {
        return txtPaymentConcept;
    }

    public JLabel getLblValidation() {
        return lblValidation;
    }

    public JLabel getLblRemainingAmount() {
        return lblRemainingAmount;
    }

    public JLabel getLblSummary() {
        return lblSummary;
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public void setTableModelAgreements(DefaultTableModel model) {
        tableAgreements.setModel(model);
        // Column rendering and selection mode is set in the Controller init
    }

    public void setTableModelPaidAgreements(DefaultTableModel model) {
        tablePaidAgreements.setModel(model);
         // Selection mode is set in the Controller init
    }

    public void setTableModelPayments(DefaultTableModel model) {
        tablePreviousPayments.setModel(model);
         // Disable editing and column reordering
         if (tablePreviousPayments.getColumnModel().getColumnCount() > 0) {
             for (int i = 0; i < tablePreviousPayments.getColumnCount(); i++) {
                 tablePreviousPayments.getColumnModel().getColumn(i).setResizable(false);
             }
             tablePreviousPayments.getTableHeader().setReorderingAllowed(false);
         }
    }

    public void clearPaymentFields() {
        // Clear amount and concept
        txtPaymentAmount.setText("");
        txtPaymentConcept.setText("");
        // Validation and remaining labels are updated by updateValidation
        lblValidation.setText("");
        lblRemainingAmount.setText(""); // Clear remaining on full clear
        lblSummary.setText(""); // Clear summary on full clear
        // Keep the register button disabled until validation passes again
        btnRegister.setEnabled(false);
    }

     // Clear fields except the date
     public void clearPaymentFieldsExceptDate() {
         txtPaymentAmount.setText("");
         txtPaymentConcept.setText("");
         // Validation and remaining labels are updated by updateValidation
         lblValidation.setText("");
         lblRemainingAmount.setText(""); // Clear remaining as it depends on entered amount
         lblSummary.setText(""); // Clear summary initially, updateValidation will set it
         btnRegister.setEnabled(false); // Disable until validation
     }


    // Method to show the dialog
    public void open() {
        this.setVisible(true);
    }
}