package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
    private JTable tablePaidAgreements;       // Paid agreements
    private JTable tablePreviousPayments;     // Payment history

    // Fields for payment registration
    private JTextField txtPaymentDate;
    private JTextField txtPaymentAmount;
    private JTextField txtPaymentConcept;

    // Labels for validation messages and summary
    private JLabel lblValidation;
    private JLabel lblRemainingAmount;
    private JLabel lblSummary;

    // Buttons for actions
    private JButton btnRegister;
    private JButton btnCancel;

    // Variables for managing payment amounts
    private double currentRemainingAmount = 0.0;
    private Double lastValidAmount = null;

    public RegisterPaymentView() {
        setTitle("Sponsor Payment Registration");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        // Create a JTabbedPane to separate Pending and Paid agreements
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Pending Agreements
        JPanel pendingPanel = new JPanel(new BorderLayout(10, 10));
        tableAgreements = new JTable();
        JScrollPane scrollPending = new JScrollPane(tableAgreements);
        scrollPending.setBorder(BorderFactory.createTitledBorder("Pending Agreements"));
        pendingPanel.add(scrollPending, BorderLayout.CENTER);
        tabbedPane.addTab("Pending", pendingPanel);

        // Panel for Paid Agreements
        JPanel paidPanel = new JPanel(new BorderLayout(10, 10));
        tablePaidAgreements = new JTable();
        JScrollPane scrollPaid = new JScrollPane(tablePaidAgreements);
        scrollPaid.setBorder(BorderFactory.createTitledBorder("Paid Agreements"));
        paidPanel.add(scrollPaid, BorderLayout.CENTER);
        tabbedPane.addTab("Paid", paidPanel);

        // Panel for Payment History
        JPanel previousPaymentsPanel = new JPanel(new BorderLayout(10, 10));
        tablePreviousPayments = new JTable();
        JScrollPane scrollPayments = new JScrollPane(tablePreviousPayments);
        scrollPayments.setBorder(BorderFactory.createTitledBorder("Payment History"));
        previousPaymentsPanel.add(scrollPayments, BorderLayout.CENTER);

        // Central panel that groups the tabbed pane and payment history
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        centerPanel.add(previousPaymentsPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Right panel for the payment registration form
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(new TitledBorder("New Payment"));
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Initialize the payment date with the current date
        txtPaymentDate = new JTextField(LocalDate.now().toString());
        txtPaymentAmount = new JTextField();
        txtPaymentConcept = new JTextField();

        lblValidation = new JLabel();
        lblRemainingAmount = new JLabel();
        lblSummary = new JLabel();

        addLabelField(formPanel, gbc, row++, "Payment Date (YYYY-MM-DD):", txtPaymentDate);
        addLabelField(formPanel, gbc, row++, "Payment Amount (€):", txtPaymentAmount);
        addLabelField(formPanel, gbc, row++, "Payment Concept:", txtPaymentConcept);
        addLabelField(formPanel, gbc, row++, "Validation Status:", lblValidation);
        addLabelField(formPanel, gbc, row++, "Remaining Amount (€):", lblRemainingAmount);
        addLabelField(formPanel, gbc, row++, "Total Summary:", lblSummary);

        // Buttons
        btnRegister = new JButton("Register Payment");
        btnRegister.setEnabled(false);
        btnCancel = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.add(formPanel, BorderLayout.CENTER);
        paymentPanel.add(buttonPanel, BorderLayout.SOUTH);

        rightPanel.add(paymentPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Real-time validation for the amount and date fields
        txtPaymentAmount.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateRemainingLabel();
            }
        });
        txtPaymentDate.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateDateFormat();
            }
        });
    }

    private void updateRemainingLabel() {
        String input = txtPaymentAmount.getText().trim();
        if (input.isEmpty()) {
            lblRemainingAmount.setText(String.format("%.2f", currentRemainingAmount));
            lastValidAmount = null;
            return;
        }
        try {
            double amount = Double.parseDouble(input);
            if (amount < 0) throw new NumberFormatException();
            lastValidAmount = amount;
            double remaining = currentRemainingAmount - amount;
            lblRemainingAmount.setText(String.format("%.2f", remaining));
        } catch (NumberFormatException ex) {
            lblRemainingAmount.setText(String.format("%.2f", currentRemainingAmount));
            lastValidAmount = null;
        }
    }

    private void validateDateFormat() {
        String input = txtPaymentDate.getText().trim();
        try {
            LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
            txtPaymentDate.setForeground(Color.BLACK);
        } catch (DateTimeParseException e) {
            txtPaymentDate.setForeground(Color.RED);
        }
    }

    public void setCurrentRemainingAmount(double amount) {
        this.currentRemainingAmount = amount;
        this.lastValidAmount = null;
        lblRemainingAmount.setText(String.format("%.2f", amount));
    }

    public Double getEnteredAmount() {
        return lastValidAmount;
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
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
        tableAgreements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void setTableModelPaidAgreements(DefaultTableModel model) {
        tablePaidAgreements.setModel(model);
        tablePaidAgreements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void setTableModelPayments(DefaultTableModel model) {
        tablePreviousPayments.setModel(model);
    }

    public void clearPaymentFields() {
        txtPaymentAmount.setText("");
        txtPaymentConcept.setText("");
        lblValidation.setText("");
        lblRemainingAmount.setText(String.format("%.2f", currentRemainingAmount));
        lblSummary.setText("");
        lastValidAmount = null;
    }
}
