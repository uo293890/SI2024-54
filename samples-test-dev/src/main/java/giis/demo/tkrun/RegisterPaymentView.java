package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegisterPaymentView extends JDialog {
    private static final long serialVersionUID = 1L;

    // Tablas para acuerdos pendientes, pagados y el historial de pagos
    private JTable tableAgreements;        // Acuerdos pendientes
    private JTable tablePaidAgreements;      // Acuerdos pagados
    private JTable tablePreviousPayments;    // Historial de pagos

    // Campos para el registro de pago
    private JTextField txtPaymentDate;
    private JTextField txtPaymentAmount;
    private JTextField txtPaymentConcept;

    // Etiquetas para mostrar mensajes de validación y resumen
    private JLabel lblValidation;
    private JLabel lblRemainingAmount;
    private JLabel lblSummary;

    // Botones para registrar o cancelar el pago
    private JButton btnRegister;
    private JButton btnCancel;

    // Variables para el control del monto a pagar
    private double currentRemainingAmount = 0.0;
    private Double lastValidAmount = null;

    public RegisterPaymentView() {
        setTitle("Registro de Pago de Patrocinador");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        // Crear JTabbedPane para separar los acuerdos pendientes y pagados
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel para Acuerdos Pendientes
        JPanel pendingPanel = new JPanel(new BorderLayout(10, 10));
        tableAgreements = new JTable();
        JScrollPane scrollPending = new JScrollPane(tableAgreements);
        scrollPending.setBorder(BorderFactory.createTitledBorder("Acuerdos Pendientes"));
        pendingPanel.add(scrollPending, BorderLayout.CENTER);
        tabbedPane.addTab("Pendientes", pendingPanel);

        // Panel para Acuerdos Pagados
        JPanel paidPanel = new JPanel(new BorderLayout(10, 10));
        tablePaidAgreements = new JTable();
        JScrollPane scrollPaid = new JScrollPane(tablePaidAgreements);
        scrollPaid.setBorder(BorderFactory.createTitledBorder("Acuerdos Pagados"));
        paidPanel.add(scrollPaid, BorderLayout.CENTER);
        tabbedPane.addTab("Pagados", paidPanel);

        // Panel para el historial de pagos
        JPanel previousPaymentsPanel = new JPanel(new BorderLayout(10,10));
        tablePreviousPayments = new JTable();
        JScrollPane scrollPayments = new JScrollPane(tablePreviousPayments);
        scrollPayments.setBorder(BorderFactory.createTitledBorder("Historial de Pagos"));
        previousPaymentsPanel.add(scrollPayments, BorderLayout.CENTER);

        // Panel central que agrupa el tabbedPane y el historial de pagos
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);
        centerPanel.add(previousPaymentsPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Panel lateral para el formulario de registro de pago
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(new TitledBorder("Nuevo Pago"));
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Inicializamos la fecha con la fecha actual
        txtPaymentDate = new JTextField(LocalDate.now().toString());
        txtPaymentAmount = new JTextField();
        txtPaymentConcept = new JTextField();

        lblValidation = new JLabel();
        lblRemainingAmount = new JLabel();
        lblSummary = new JLabel();

        addLabelField(formPanel, gbc, row++, "Fecha de Pago (YYYY-MM-DD):", txtPaymentDate);
        addLabelField(formPanel, gbc, row++, "Importe a Pagar (€):", txtPaymentAmount);
        addLabelField(formPanel, gbc, row++, "Concepto de Pago:", txtPaymentConcept);
        addLabelField(formPanel, gbc, row++, "Estado de Validación:", lblValidation);
        addLabelField(formPanel, gbc, row++, "Importe Restante (€):", lblRemainingAmount);
        addLabelField(formPanel, gbc, row++, "Resumen Total:", lblSummary);

        // Botones
        btnRegister = new JButton("Registrar Pago");
        btnRegister.setEnabled(false);
        btnCancel = new JButton("Cancelar");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.add(formPanel, BorderLayout.CENTER);
        paymentPanel.add(buttonPanel, BorderLayout.SOUTH);

        rightPanel.add(paymentPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Validación en tiempo real para el importe y la fecha
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

    // Getters de componentes para el controlador
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
