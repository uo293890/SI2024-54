package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RegisterPaymentController {
    private RegisterPaymentModel model;
    private RegisterPaymentView view;

    public RegisterPaymentController(RegisterPaymentModel model, RegisterPaymentView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        // Load both Pending and Paid agreements at startup
        loadPendingAgreements();
        loadPaidAgreements();

        // Set a custom cell renderer for pending agreements (inline, without a new class)
        view.getTableAgreements().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                    boolean isSelected, boolean hasFocus,
                                                                    int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int agreementId = (int) table.getValueAt(row, 0);
                double expected = model.getAgreementExpectedAmount(agreementId);
                double paid = model.getAgreementTotalPaid(agreementId);
                // Highlight with orange if total paid exceeds expected amount (overpayment)
                if (paid > expected) {
                    c.setBackground(Color.ORANGE);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
                return c;
            }
        });

        // Selection listener for Pending Agreements: show details and payment history, enable payment registration.
        view.getTableAgreements().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedAgreement();
            }
        });

        // Selection listener for Paid Agreements: show payment history (registration disabled).
        view.getTablePaidAgreements().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedPaidAgreement();
            }
        });

        // Real-time validation for payment amount
        view.getTxtPaymentAmount().getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateValidation(); }
            public void removeUpdate(DocumentEvent e) { updateValidation(); }
            public void insertUpdate(DocumentEvent e) { updateValidation(); }
        });

        view.getBtnRegister().addActionListener(e -> registerPayment());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    private void loadPendingAgreements() {
        List<Object[]> agreements = model.getAgreementsByStatus("Agreed");
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Agreement ID", "Sponsor", "Contact", "Amount (€)", "Status"}, 0
        );
        for (Object[] row : agreements) {
            tableModel.addRow(row);
        }
        view.setTableModelAgreements(tableModel);
        view.clearPaymentFields();
    }

    private void loadPaidAgreements() {
        List<Object[]> agreements = model.getAgreementsByStatus("Paid");
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Agreement ID", "Sponsor", "Contact", "Amount (€)", "Status"}, 0
        );
        for (Object[] row : agreements) {
            tableModel.addRow(row);
        }
        view.setTableModelPaidAgreements(tableModel);
    }

    // When a pending agreement is selected, show its details and payment history
    private void updateSelectedAgreement() {
        try {
            int selectedRow = view.getTableAgreements().getSelectedRow();
            if (selectedRow == -1) return;

            int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
            double expected = model.getAgreementExpectedAmount(agreementId);
            double paid = model.getAgreementTotalPaid(agreementId);
            double remaining = expected - paid;

            List<Object[]> previousPayments = model.getPreviousPayments(agreementId);
            DefaultTableModel paymentsTableModel = new DefaultTableModel(
                new String[]{"Date", "Amount (€)", "Concept"}, 0
            );
            for (Object[] row : previousPayments) {
                paymentsTableModel.addRow(row);
            }
            view.setTableModelPayments(paymentsTableModel);

            view.setCurrentRemainingAmount(remaining);
            view.getLblSummary().setText(String.format("Total Paid: %.2f € / Total Expected: %.2f €", paid, expected));
            view.clearPaymentFields();

            // Enable registration if there is any remaining amount (even if negative overpayment exists)
            view.getBtnRegister().setEnabled(remaining != 0);

            updateValidation();
        } catch (Exception ex) {
            showError("Error loading agreement details: " + ex.getMessage());
        }
    }

    // When a paid agreement is selected, show its payment history (registration disabled)
    private void updateSelectedPaidAgreement() {
        try {
            int selectedRow = view.getTablePaidAgreements().getSelectedRow();
            if (selectedRow == -1) return;

            int agreementId = (int) view.getTablePaidAgreements().getValueAt(selectedRow, 0);
            List<Object[]> previousPayments = model.getPreviousPayments(agreementId);
            DefaultTableModel paymentsTableModel = new DefaultTableModel(
                new String[]{"Date", "Amount (€)", "Concept"}, 0
            );
            for (Object[] row : previousPayments) {
                paymentsTableModel.addRow(row);
            }
            view.setTableModelPayments(paymentsTableModel);
            // Disable registration when viewing paid agreements
            view.getBtnRegister().setEnabled(false);
        } catch (Exception ex) {
            showError("Error loading payment history: " + ex.getMessage());
        }
    }

    // Real-time validation: informs whether the new payment results in a partial payment, exact payment, overpayment, or a refund.
    private void updateValidation() {
        try {
            int selectedRow = view.getTableAgreements().getSelectedRow();
            if (selectedRow == -1) return;
            int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
            double expected = model.getAgreementExpectedAmount(agreementId);
            double paid = model.getAgreementTotalPaid(agreementId);

            double amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
            double newTotal = paid + amount;

            if (amount < 0) {
                view.getLblValidation().setForeground(Color.MAGENTA);
                view.getLblValidation().setText("Refund Payment: Reduces total by " + String.format("%.2f €", Math.abs(amount)));
            } else if (newTotal < expected) {
                view.getLblValidation().setForeground(Color.BLACK);
                view.getLblValidation().setText("Partial Payment: " + String.format("%.2f € remaining", expected - newTotal));
            } else if (newTotal == expected) {
                view.getLblValidation().setForeground(new Color(0, 128, 0)); // dark green
                view.getLblValidation().setText("Exact Payment: Agreement will be marked as Paid");
            } else { // newTotal > expected
                view.getLblValidation().setForeground(Color.RED);
                view.getLblValidation().setText("Overpayment: Exceeds expected amount by " + String.format("%.2f €", newTotal - expected));
            }
            view.getLblRemainingAmount().setText(String.format("%.2f €", expected - newTotal));
            view.getLblSummary().setText(String.format("Total Paid: %.2f € / Total Expected: %.2f €", paid, expected));
        } catch (NumberFormatException ex) {
            view.getLblValidation().setText("Invalid amount");
        } catch (Exception ex) {
            showError("Error during payment validation: " + ex.getMessage());
        }
    }

    // Payment registration: allows partial payments, exact payments (which update the status to Paid), overpayments, or refunds.
    private void registerPayment() {
        SwingUtil.exceptionWrapper(() -> {
            try {
                int selectedRow = view.getTableAgreements().getSelectedRow();
                if (selectedRow == -1) throw new RuntimeException("Please select a pending agreement");

                int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
                int invoiceId = model.getInvoiceIdByAgreement(agreementId);
                if (invoiceId == -1) throw new RuntimeException("No invoice found for the selected agreement");

                String dateStr = view.getTxtPaymentDate().getText().trim();
                String concept = view.getTxtPaymentConcept().getText().trim();
                if (concept.isEmpty()) throw new RuntimeException("Payment concept is required");

                LocalDate paymentDate;
                try {
                    paymentDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ex) {
                    throw new RuntimeException("Invalid date format. Use YYYY-MM-DD");
                }
                if (paymentDate.isAfter(LocalDate.now())) {
                    throw new RuntimeException("Payment date cannot be in the future");
                }

                double amount;
                try {
                    amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
                    if (amount == 0) throw new NumberFormatException(); // zero not allowed
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Invalid payment amount (cannot be zero)");
                }

                double expected = model.getAgreementExpectedAmount(agreementId);
                double paid = model.getAgreementTotalPaid(agreementId);
                double newTotal = paid + amount;

                String message;
                if (amount < 0) {
                    message = "This refund/correction will reduce the total paid by " +
                              String.format("%.2f €", Math.abs(amount)) +
                              ". Do you wish to continue?";
                } else if (newTotal < expected) {
                    message = "You are recording a partial payment. The agreement will remain pending until exactly " +
                              String.format("%.2f €", expected) + " is paid. Do you wish to continue?";
                } else if (newTotal == expected) {
                    message = "This payment will complete the agreement. Confirm payment?";
                } else {
                    message = "This payment will result in an overpayment by " +
                              String.format("%.2f €", newTotal - expected) +
                              ". The agreement status will remain as Pending. Do you wish to continue?";
                }

                int confirm = JOptionPane.showConfirmDialog(view, message, "Confirm Payment", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                RegisterPaymentDTO dto = new RegisterPaymentDTO(dateStr, concept, amount, invoiceId);
                model.insertPayment(dto);

                // Only update status to "Paid" if the total equals the expected amount exactly
                if (newTotal == expected) {
                    model.updateAgreementStatusToPaid(agreementId);
                }

                // Reload both tables to reflect the changes
                loadPendingAgreements();
                loadPaidAgreements();

                // Reselect the agreement in the appropriate table based on the payment result
                if (newTotal == expected) {
                    selectAgreementInPaidTable(agreementId);
                } else {
                    reselectPendingAgreement(agreementId);
                }

                view.clearPaymentFields();
                JOptionPane.showMessageDialog(view, "Payment registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Unexpected error: " + ex.getMessage());
            }
        });
    }

    // Reselect the pending agreement row after a partial payment, overpayment, or refund
    private void reselectPendingAgreement(int agreementId) {
        JTable table = view.getTableAgreements();
        DefaultTableModel modelTable = (DefaultTableModel) table.getModel();
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            if ((int) modelTable.getValueAt(i, 0) == agreementId) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    // Select the agreement in the Paid Agreements table if the payment is exact
    private void selectAgreementInPaidTable(int agreementId) {
        JTable table = view.getTablePaidAgreements();
        DefaultTableModel modelTable = (DefaultTableModel) table.getModel();
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            if ((int) modelTable.getValueAt(i, 0) == agreementId) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    // Show error messages using a dialog
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
