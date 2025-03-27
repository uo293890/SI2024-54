package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
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
        // Cargar los acuerdos pendientes y pagados al iniciar la vista
        loadPendingAgreements();
        loadPaidAgreements();

        // Al seleccionar un acuerdo pendiente se muestran sus detalles y el historial de pagos
        view.getTableAgreements().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedAgreement();
            }
        });

        // Actualización en tiempo real de la validación al modificar el importe
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
            new Object[]{"ID Acuerdo", "Patrocinador", "Contacto", "Monto (€)", "Estado"}, 0
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
            new Object[]{"ID Acuerdo", "Patrocinador", "Contacto", "Monto (€)", "Estado"}, 0
        );
        for (Object[] row : agreements) {
            tableModel.addRow(row);
        }
        view.setTableModelPaidAgreements(tableModel);
    }

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
                    new String[]{"Fecha", "Importe (€)", "Concepto"}, 0
            );
            for (Object[] row : previousPayments) {
                paymentsTableModel.addRow(row);
            }
            view.setTableModelPayments(paymentsTableModel);

            view.setCurrentRemainingAmount(remaining);
            view.getLblSummary().setText(String.format("Total Pagado: %.2f € / Total Acordado: %.2f €", paid, expected));
            view.clearPaymentFields();

            // Habilitar el botón de registrar mientras quede saldo por abonar
            view.getBtnRegister().setEnabled(remaining > 0);

            updateValidation();
        } catch (Exception ex) {
            showError("Error al cargar los datos del acuerdo: " + ex.getMessage());
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
                view.getLblValidation().setText("Pago parcial: Falta " + String.format("%.2f €", expected - newTotal) + " para completar el acuerdo");
            } else if (newTotal > expected) {
                view.getLblValidation().setText("Error: El pago excede el monto acordado en " + String.format("%.2f €", newTotal - expected));
            } else {
                view.getLblValidation().setText("Pago exacto: El importe coincide con el monto acordado");
            }

            view.getLblRemainingAmount().setText(String.format("%.2f €", expected - newTotal));
            view.getLblSummary().setText(String.format("Total Pagado: %.2f € / Total Acordado: %.2f €", paid, expected));
        } catch (NumberFormatException ex) {
            view.getLblValidation().setText("❌ Importe inválido");
        } catch (Exception ex) {
            showError("Error en la validación del pago: " + ex.getMessage());
        }
    }

    private void registerPayment() {
        SwingUtil.exceptionWrapper(() -> {
            try {
                int selectedRow = view.getTableAgreements().getSelectedRow();
                if (selectedRow == -1) throw new RuntimeException("Seleccione un acuerdo pendiente");

                int agreementId = (int) view.getTableAgreements().getValueAt(selectedRow, 0);
                int invoiceId = model.getInvoiceIdByAgreement(agreementId);
                if (invoiceId == -1) throw new RuntimeException("No se encontró una factura para este acuerdo");

                String dateStr = view.getTxtPaymentDate().getText().trim();
                String concept = view.getTxtPaymentConcept().getText().trim();
                if (concept.isEmpty()) throw new RuntimeException("El concepto es obligatorio");

                LocalDate paymentDate;
                try {
                    paymentDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ex) {
                    throw new RuntimeException("Formato de fecha inválido. Use YYYY-MM-DD");
                }
                if (paymentDate.isAfter(LocalDate.now())) {
                    throw new RuntimeException("La fecha de pago no puede ser futura");
                }

                double amount;
                try {
                    amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
                    if (amount <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Importe inválido");
                }

                double expected = model.getAgreementExpectedAmount(agreementId);
                double paid = model.getAgreementTotalPaid(agreementId);
                double newTotal = paid + amount;

                // Validación: no se permite registrar un pago que haga que se supere el monto acordado
                if (newTotal > expected) {
                    throw new RuntimeException("El importe total pagado supera el monto acordado. Por favor, ingrese un importe correcto.");
                }

                String message;
                if (newTotal < expected) {
                    message = "Está registrando un pago parcial. El acuerdo permanecerá pendiente hasta que se abone exactamente " +
                              String.format("%.2f €", expected) + ". ¿Desea continuar?";
                } else { // newTotal == expected
                    message = "Este pago completará el acuerdo. ¿Confirma el pago?";
                }

                int confirm = JOptionPane.showConfirmDialog(view, message, "Confirmar Pago", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                RegisterPaymentDTO dto = new RegisterPaymentDTO(dateStr, concept, amount, invoiceId);
                model.insertPayment(dto);

                // Solo si la suma es exactamente igual al monto acordado se actualiza el estado a 'Paid'
                if (newTotal == expected) {
                    model.updateAgreementStatusToPaid(agreementId);
                }

                // Recargar ambas tablas para reflejar los cambios
                loadPendingAgreements();
                loadPaidAgreements();

                // Si se completó el pago, se selecciona el acuerdo en la pestaña de pagados; de lo contrario, se mantiene en pendientes
                if (newTotal == expected) {
                    selectAgreementInPaidTable(agreementId);
                } else {
                    reselectPendingAgreement(agreementId);
                }

                view.clearPaymentFields();
                JOptionPane.showMessageDialog(view, "Pago registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Error inesperado: " + ex.getMessage());
            }
        });
    }

    // Método para mantener la selección del acuerdo en la tabla de pendientes (para pagos parciales)
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

    // Método para seleccionar el acuerdo en la tabla de pagados (cuando se completa el pago)
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
