package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegisterPaymentView extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTable tableAgreements;
    private JTable tablePreviousPayments;
    private JTextField txtPaymentAmount;
    private JTextField txtPaymentConcept;
    private JTextField txtPaymentDate;
    private JLabel lblValidation;
    private JLabel lblRemainingAmount;
    private JLabel lblSummary;
    private JButton btnRegister;
    private JButton btnCancel;
    private JComboBox<String> filterComboBox;

    private double currentRemainingAmount = 0.0;
    private Double lastValidAmount = null;

    public RegisterPaymentView() {
        setTitle("Register Sponsor Payment");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(15, 15));
        this.getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFilter = new JLabel("Show:");
        filterComboBox = new JComboBox<>(new String[]{"Pending Payments", "Already Paid"});
        filterComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(lblFilter);
        filterPanel.add(filterComboBox);
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        tableAgreements = new JTable();
        JScrollPane scrollAgreements = new JScrollPane(tableAgreements);
        scrollAgreements.setBorder(new TitledBorder("Sponsor Agreements"));
        centerPanel.add(scrollAgreements);

        tablePreviousPayments = new JTable();
        JScrollPane scrollPayments = new JScrollPane(tablePreviousPayments);
        scrollPayments.setBorder(new TitledBorder("Previous Payments"));
        centerPanel.add(scrollPayments);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        JPanel panelPayment = new JPanel(new GridBagLayout());
        panelPayment.setBorder(new TitledBorder("New Payment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        txtPaymentDate = new JTextField(LocalDate.now().toString());
        txtPaymentAmount = new JTextField();
        txtPaymentConcept = new JTextField();
        lblValidation = new JLabel();
        lblRemainingAmount = new JLabel();
        lblSummary = new JLabel();

        addLabelField(panelPayment, gbc, row++, "Payment Date (YYYY-MM-DD):", txtPaymentDate);
        addLabelField(panelPayment, gbc, row++, "Amount to Pay (€):", txtPaymentAmount);
        addLabelField(panelPayment, gbc, row++, "Payment Concept:", txtPaymentConcept);
        addLabelField(panelPayment, gbc, row++, "Validation Status:", lblValidation);
        addLabelField(panelPayment, gbc, row++, "Remaining after Payment (€):", lblRemainingAmount);
        addLabelField(panelPayment, gbc, row++, "Total Summary:", lblSummary);

        btnRegister = new JButton("Register Payment");
        btnRegister.setEnabled(false);
        btnCancel = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.add(panelPayment, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        rightPanel.add(formPanel, BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.EAST);

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

    public JTable getTableAgreements() { return tableAgreements; }
    public JTable getTablePreviousPayments() { return tablePreviousPayments; }
    public JTextField getTxtPaymentAmount() { return txtPaymentAmount; }
    public JTextField getTxtPaymentConcept() { return txtPaymentConcept; }
    public JTextField getTxtPaymentDate() { return txtPaymentDate; }
    public JLabel getLblValidation() { return lblValidation; }
    public JLabel getLblRemainingAmount() { return lblRemainingAmount; }
    public JLabel getLblSummary() { return lblSummary; }
    public JButton getBtnRegister() { return btnRegister; }
    public JButton getBtnCancel() { return btnCancel; }
    public JComboBox<String> getFilterComboBox() { return filterComboBox; }

    public void setTableModelAgreements(DefaultTableModel model) {
        tableAgreements.setModel(model);
        tableAgreements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

    public void setExtendedState(int state) {
        if (state == JFrame.MAXIMIZED_BOTH) {
            this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        }
    }
}
