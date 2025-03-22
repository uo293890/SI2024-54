package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

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

    public RegisterPaymentView() {
        setTitle("Register Sponsor Payment");
        setModal(true);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Top: Agreements Pending Payment
        tableAgreements = new JTable();
        JScrollPane scrollAgreements = new JScrollPane(tableAgreements);
        scrollAgreements.setBorder(BorderFactory.createTitledBorder("Sponsor Agreements Pending Payment"));
        mainPanel.add(scrollAgreements, BorderLayout.NORTH);

        // Center: Previous Payments
        tablePreviousPayments = new JTable();
        JScrollPane scrollPayments = new JScrollPane(tablePreviousPayments);
        scrollPayments.setBorder(BorderFactory.createTitledBorder("Previous Payments"));
        mainPanel.add(scrollPayments, BorderLayout.CENTER);

        // Bottom: Payment Entry Panel
        JPanel panelPayment = new JPanel(new GridBagLayout());
        panelPayment.setBorder(BorderFactory.createTitledBorder("New Payment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(panelPayment, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
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
        lblRemainingAmount.setText("");
        lblSummary.setText("");
    }
}