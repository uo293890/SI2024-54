package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class RegisterPaymentController {
    private RegisterPaymentModel model;
    private RegisterPaymentView view;

    public RegisterPaymentController(RegisterPaymentModel model, RegisterPaymentView view) {
        this.model = model;
        this.view = view;
        this.initController();
    }

    private void initController() {
        loadPendingAgreements();

        view.getTableAgreements().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedAgreement();
            }
        });

        view.getTxtPaymentAmount().getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateValidation(); }
            public void removeUpdate(DocumentEvent e) { updateValidation(); }
            public void insertUpdate(DocumentEvent e) { updateValidation(); }
        });

        view.getBtnRegister().addActionListener(e -> registerPayment());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    private void loadPendingAgreements() {
        DefaultTableModel modelTable = new DefaultTableModel(
            new String[] {"Agreement ID", "Event", "Sponsor", "Agreement Date", "Total Amount (€)", "Status"}, 0
        );
        for (Object[] row : model.getPendingAgreements()) {
            modelTable.addRow(row);
        }
        view.setTableModelAgreements(modelTable);
    }

    private void updateSelectedAgreement() {
        try {
            int selectedRow = view.getTableAgreements().getSelectedRow();
            if (selectedRow == -1) return;

            int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
            double expected = model.getAgreementExpectedAmount(agreementId);
            double paid = model.getAgreementTotalPaid(agreementId);

            DefaultTableModel modelTable = new DefaultTableModel(new String[] {"Date", "Amount (€)", "Concept"}, 0);
            for (Object[] row : model.getPreviousPayments(agreementId)) {
                modelTable.addRow(row);
            }
            view.setTableModelPayments(modelTable);

            view.getLblRemainingAmount().setText(String.format("%.2f € remaining", expected - paid));
            view.getLblSummary().setText(String.format("Total Paid: %.2f € / Total Expected: %.2f €", paid, expected));
            view.getBtnRegister().setEnabled(true);
            updateValidation();
        } catch (Exception ex) {
            showError("Error loading agreement data: " + ex.getMessage());
        }
    }

    private void updateValidation() {
        try {
            int selectedRow = view.getTableAgreements().getSelectedRow();
            if (selectedRow == -1) return;

            int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
            double expected = model.getAgreementExpectedAmount(agreementId);
            double paid = model.getAgreementTotalPaid(agreementId);

            double amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
            double newTotal = paid + amount;

            if (newTotal < expected) {
                view.getLblValidation().setText("⚠️ Underpayment: " + String.format("%.2f € remaining", expected - newTotal));
            } else if (newTotal > expected) {
                view.getLblValidation().setText("⚠️ Overpayment: " + String.format("%.2f € over", newTotal - expected));
            } else {
                view.getLblValidation().setText("✅ Exact payment");
            }

            view.getLblRemainingAmount().setText(String.format("%.2f € remaining", expected - newTotal));
        } catch (NumberFormatException ex) {
            view.getLblValidation().setText("❌ Invalid amount");
        } catch (Exception ex) {
            showError("Error validating payment: " + ex.getMessage());
        }
    }

    private void registerPayment() {
        SwingUtil.exceptionWrapper(() -> {
            try {
                int selectedRow = view.getTableAgreements().getSelectedRow();
                if (selectedRow == -1) throw new RuntimeException("Select a pending agreement");

                int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
                int invoiceId = model.getInvoiceIdByAgreement(agreementId);
                if (invoiceId == -1) throw new RuntimeException("No invoice found for this agreement");

                String date = view.getTxtPaymentDate().getText();
                String concept = view.getTxtPaymentConcept().getText().trim();
                if (concept.isEmpty()) throw new RuntimeException("Concept is required");

                double amount;
                try {
                    amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Invalid amount format");
                }

                double expected = model.getAgreementExpectedAmount(agreementId);
                double paid = model.getAgreementTotalPaid(agreementId);
                double newTotal = paid + amount;

                String message;
                if (newTotal < expected) {
                    message = "This is a partial payment. Are you sure you want to proceed?";
                } else if (newTotal > expected) {
                    message = "This is an overpayment. Are you sure you want to proceed?";
                } else {
                    message = "This will complete the payment. Confirm?";
                }

                int confirm = JOptionPane.showConfirmDialog(view, message, "Confirm Payment", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                RegisterPaymentDTO dto = new RegisterPaymentDTO(date, concept, amount, invoiceId);
                model.insertPayment(dto);

                if (newTotal >= expected) {
                    model.updateAgreementStatusToPaid(agreementId);
                }

                loadPendingAgreements();
                view.clearPaymentFields();
                view.getBtnRegister().setEnabled(false);
                JOptionPane.showMessageDialog(view, "✅ Payment registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Unexpected error: " + ex.getMessage());
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}