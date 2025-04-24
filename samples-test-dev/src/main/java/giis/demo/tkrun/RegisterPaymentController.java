package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel; // Import TableColumnModel
import javax.swing.table.TableColumn; // Import TableColumn
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class RegisterPaymentController {
    private RegisterPaymentModel model;
    private RegisterPaymentView view;
    private LocalDate workingDate;

    // Modify the constructor to accept workingDate
    public RegisterPaymentController(RegisterPaymentModel model, RegisterPaymentView view, LocalDate workingDate) {
        this.model = Objects.requireNonNull(model, "RegisterPaymentModel cannot be null");
        this.view = Objects.requireNonNull(view, "RegisterPaymentView cannot be null");
        this.workingDate = Objects.requireNonNull(workingDate, "workingDate cannot be null");

        initController();
    }

    private void initController() {
        // Load both Pending and Paid agreements at startup
        // These calls now refer to the methods defined later in the class
        loadPendingAgreements();
        loadPaidAgreements();

        // Set the initial payment date to the working date
        view.getTxtPaymentDate().setText(workingDate.toString());

        // Set custom cell renderer for pending agreements (overpayment highlighting)
        view.getTableAgreements().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override // Use @Override annotation
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                    boolean isSelected, boolean hasFocus,
                                                                    int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Get agreement ID from the model, column 0 (which is hidden)
                // Ensure the row index is valid for the model
                 if (row < 0 || row >= table.getModel().getRowCount()) {
                     c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                     return c;
                 }

                Object idValue = table.getModel().getValueAt(row, 0); // ID is at model index 0
                if (!(idValue instanceof Integer)) {
                     c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                     return c;
                }
                int agreementId = (int) idValue;

                // Fetch expected and paid amounts
                double expected = model.getAgreementExpectedAmount(agreementId);
                double paid = model.getAgreementTotalPaid(agreementId);

                // Highlight with orange if total paid exceeds expected amount (overpayment)
                if (paid > expected) {
                    c.setBackground(Color.ORANGE);
                } else {
                    // Reset background if not overpaid, respecting selection
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
                return c;
            }
        });


        // Selection listener for BOTH tables: show details and payment history, enable payment registration.
        view.getTableAgreements().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                 // Clear selection in the other table when one is selected
                 if(view.getTableAgreements().getSelectedRow() != -1) {
                     view.getTablePaidAgreements().clearSelection();
                     updateSelectedAgreementDetails();
                 } else if (view.getTableAgreements().getSelectedRow() == -1 && view.getTablePaidAgreements().getSelectedRow() == -1) {
                     // If selection is cleared in this table and the other is also not selected
                     clearAgreementDetailsView();
                 }
            }
        });

         view.getTablePaidAgreements().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                 // Clear selection in the other table when one is selected
                 if(view.getTablePaidAgreements().getSelectedRow() != -1) {
                     view.getTableAgreements().clearSelection();
                     updateSelectedAgreementDetails();
                 } else if (view.getTableAgreements().getSelectedRow() == -1 && view.getTablePaidAgreements().getSelectedRow() == -1) {
                      // If selection is cleared in this table and the other is also not selected
                     clearAgreementDetailsView();
                 }
            }
        });

        // Real-time validation for payment amount and date
        view.getTxtPaymentAmount().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void changedUpdate(DocumentEvent e) { updateValidation(); }
            @Override public void removeUpdate(DocumentEvent e) { updateValidation(); }
            @Override public void insertUpdate(DocumentEvent e) { updateValidation(); }
        });

        view.getTxtPaymentDate().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void changedUpdate(DocumentEvent e) { updateValidation(); }
            @Override public void removeUpdate(DocumentEvent e) { updateValidation(); }
            @Override public void insertUpdate(DocumentEvent e) { updateValidation(); }
        });

        view.getBtnRegister().addActionListener(e -> registerPayment());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }


    // Load agreement details and payment history for the currently selected row
    private void updateSelectedAgreementDetails() {
         int selectedRowAgreed = view.getTableAgreements().getSelectedRow();
         int selectedRowPaid = view.getTablePaidAgreements().getSelectedRow();

         // Determine the selected agreement ID and its status
         int agreementId = -1;
         JTable selectedTable = null;

         if (selectedRowAgreed != -1) {
             selectedTable = view.getTableAgreements();
             // Get ID from the model's first column (index 0)
             agreementId = (int) selectedTable.getModel().getValueAt(selectedRowAgreed, 0);
         } else if (selectedRowPaid != -1) {
             selectedTable = view.getTablePaidAgreements();
             // Get ID from the model's first column (index 0)
             agreementId = (int) selectedTable.getModel().getValueAt(selectedRowPaid, 0);
         } else {
             // No row selected
             clearAgreementDetailsView();
             return;
         }

        try {
            double expected = model.getAgreementExpectedAmount(agreementId);
            double paid = model.getAgreementTotalPaid(agreementId);
            double remaining = expected - paid;

            List<Object[]> previousPayments = model.getPreviousPayments(agreementId);
            DefaultTableModel paymentsTableModel = new DefaultTableModel(
                    new String[]{"Date", "Amount (€)", "Concept"}, 0
            ) {
                 @Override // Make history table read-only
                 public boolean isCellEditable(int row, int column) { return false; }
            };
            for (Object[] row : previousPayments) {
                 // Ensure concept is handled if null
                 Object concept = row[2];
                 paymentsTableModel.addRow(new Object[]{row[0], row[1], concept == null ? "" : concept});
            }
            view.setTableModelPayments(paymentsTableModel);

            view.setCurrentRemainingAmount(remaining); // Set for initial remaining display
            view.getLblSummary().setText(String.format("Total Paid: %.2f € / Total Expected: %.2f €", paid, expected));
            view.clearPaymentFieldsExceptDate(); // Clear input fields but keep the date

            // Enable registration for any selected agreement (Pending or Paid)
            view.getBtnRegister().setEnabled(true);

            updateValidation(); // Trigger validation for the newly selected agreement
        } catch (Exception ex) {
            showError("Error loading agreement details: " + ex.getMessage());
            // Ensure fields are cleared and button is disabled on error
            clearAgreementDetailsView();
        }
    }

     // Helper to clear the details view area
     private void clearAgreementDetailsView() {
        view.clearPaymentFields(); // This clears amount, concept, labels and disables button
        // Date field is NOT cleared by clearPaymentFields
        view.setTableModelPayments(new DefaultTableModel(new String[]{"Date", "Amount (€)", "Concept"}, 0)); // Clear payments table
        view.getLblValidation().setText("Select an agreement"); // Clear validation message
        view.getLblValidation().setForeground(Color.BLACK);
        view.getLblRemainingAmount().setText(""); // Clear remaining amount label
        view.getLblSummary().setText(""); // Clear summary label
        view.setCurrentRemainingAmount(0.0); // Reset internal remaining amount
     }


    // Real-time validation: informs whether the new payment results in a partial payment, exact payment, overpayment, or a refund.
    // Now also validates the date format and applies to both tables.
    private void updateValidation() {
         int selectedRowAgreed = view.getTableAgreements().getSelectedRow();
         int selectedRowPaid = view.getTablePaidAgreements().getSelectedRow();

        // Determine the selected agreement ID
         int agreementId = -1;
         JTable selectedTable = null;

         if (selectedRowAgreed != -1) {
             selectedTable = view.getTableAgreements();
             agreementId = (int) selectedTable.getModel().getValueAt(selectedRowAgreed, 0); // ID is at model index 0
         } else if (selectedRowPaid != -1) {
             selectedTable = view.getTablePaidAgreements();
             agreementId = (int) selectedTable.getModel().getValueAt(selectedRowPaid, 0); // ID is at model index 0
         } else {
             // No row selected (should be handled by selection listener clearing view)
             return; // Do nothing if no agreement is selected
         }

        try {
            String dateStr = view.getTxtPaymentDate().getText().trim();
            LocalDate paymentDate;
            try {
                paymentDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                view.getTxtPaymentDate().setForeground(Color.BLACK); // Reset color if valid
            } catch (DateTimeParseException ex) {
                view.getLblValidation().setText("Invalid date format (YYYY-MM-DD)");
                view.getLblValidation().setForeground(Color.RED);
                view.getTxtPaymentDate().setForeground(Color.RED); // Indicate invalid format
                view.getBtnRegister().setEnabled(false); // Disable button if date is invalid
                // Show current remaining if date is invalid
                view.getLblRemainingAmount().setText(String.format("%.2f €", model.getAgreementExpectedAmount(agreementId) - model.getAgreementTotalPaid(agreementId)));
                view.getLblSummary().setText(String.format("Total Paid (Before): %.2f € / Total Expected: %.2f €", model.getAgreementTotalPaid(agreementId), model.getAgreementExpectedAmount(agreementId)));
                return; // Stop validation if date is invalid
            }

            double amount;
            try {
                amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
                if (amount == 0) {
                    // If amount is 0, clear validation message related to amount status
                     view.getLblValidation().setText(""); // Clear specific validation message
                     view.getLblRemainingAmount().setText(String.format("%.2f €", model.getAgreementExpectedAmount(agreementId) - model.getAgreementTotalPaid(agreementId))); // Show current remaining
                     view.getLblSummary().setText(String.format("Total Paid (Before): %.2f € / Total Expected: %.2f €", model.getAgreementTotalPaid(agreementId), model.getAgreementExpectedAmount(agreementId)));
                     view.getBtnRegister().setEnabled(false); // Disable button for zero amount
                     return; // Stop validation for zero amount
                }

            } catch (NumberFormatException ex) {
                view.getLblValidation().setText("Invalid payment amount"); // Remove "zero" from message here
                view.getLblValidation().setForeground(Color.RED);
                // Show current remaining if amount is invalid
                view.getLblRemainingAmount().setText(String.format("%.2f €", model.getAgreementExpectedAmount(agreementId) - model.getAgreementTotalPaid(agreementId)));
                view.getLblSummary().setText(String.format("Total Paid (Before): %.2f € / Total Expected: %.2f €", model.getAgreementTotalPaid(agreementId), model.getAgreementExpectedAmount(agreementId)));
                view.getBtnRegister().setEnabled(false); // Disable button if amount is invalid
                return; // Stop validation if amount is invalid
            }

            double expected = model.getAgreementExpectedAmount(agreementId);
            double paidBefore = model.getAgreementTotalPaid(agreementId);
            double newTotal = paidBefore + amount; // Calculation handles negative amounts correctly

            String paymentStatusMsg;
            if (amount < 0) {
                 paymentStatusMsg = "Refund/Correction";
            } else if (Math.abs(newTotal - expected) < 0.01) { // Use tolerance for exact comparison
                paymentStatusMsg = "Exact Payment (Agreement will be Paid)";
            } else if (newTotal < expected) {
                paymentStatusMsg = "Partial Payment (Agreement will be Pending)";
            } else { // newTotal > expected
                paymentStatusMsg = "Overpayment (Agreement will be Pending)";
            }

            String dateStatusMsg;
            if (paymentDate.isBefore(workingDate)) {
                dateStatusMsg = "Date is in the Past (" + paymentDate + ")";
            } else if (paymentDate.isEqual(workingDate)) {
                dateStatusMsg = "Date is Present (" + paymentDate + ")";
            } else { // paymentDate.isAfter(workingDate)
                dateStatusMsg = "Date is in the Future (" + paymentDate + ")";
            }

            // Combine messages
            view.getLblValidation().setForeground(Color.BLACK); // Default color
            if (amount < 0) view.getLblValidation().setForeground(Color.MAGENTA); // Refund color
            else if (Math.abs(newTotal - expected) < 0.01) view.getLblValidation().setForeground(new Color(0, 128, 0)); // Exact payment color
            else if (newTotal > expected) view.getLblValidation().setForeground(Color.RED); // Overpayment color


            view.getLblValidation().setText(paymentStatusMsg + " | " + dateStatusMsg);


             // Update remaining amount display based on the *new* total
             view.getLblRemainingAmount().setText(String.format("%.2f €", expected - newTotal));

             // Enable register button only if date format and amount are valid (non-zero handled above)
             view.getBtnRegister().setEnabled(true);

             // Update summary display with current paid vs expected (before this new payment)
             view.getLblSummary().setText(String.format("Total Paid (Before): %.2f € / Total Expected: %.2f €", paidBefore, expected));


        } catch (Exception ex) {
            // Catch any other unexpected errors during validation
            view.getLblValidation().setText("Validation error: " + ex.getMessage());
            view.getLblValidation().setForeground(Color.RED);
             view.getBtnRegister().setEnabled(false); // Disable button on unexpected error
             view.getLblRemainingAmount().setText("");
             view.getLblSummary().setText("");
        }
    }


    // Payment registration
    private void registerPayment() {
        SwingUtil.exceptionWrapper(() -> {
            int selectedRowAgreed = view.getTableAgreements().getSelectedRow();
            int selectedRowPaid = view.getTablePaidAgreements().getSelectedRow();

             int agreementId = -1;
             JTable selectedTable = null;

             if (selectedRowAgreed != -1) {
                 selectedTable = view.getTableAgreements();
                 agreementId = (int) selectedTable.getModel().getValueAt(selectedRowAgreed, 0); // ID is at model index 0
             } else if (selectedRowPaid != -1) {
                 selectedTable = view.getTablePaidAgreements();
                 agreementId = (int) selectedTable.getModel().getValueAt(selectedRowPaid, 0); // ID is at model index 0
             } else {
                 // Should not happen if button is enabled correctly, but good practice
                 throw new RuntimeException("Please select an agreement");
             }

            try {
                int invoiceId = model.getInvoiceIdByAgreement(agreementId);
                if (invoiceId == -1) throw new RuntimeException("No invoice found for the selected agreement");

                String dateStr = view.getTxtPaymentDate().getText().trim();
                String concept = view.getTxtPaymentConcept().getText().trim();
                // Concept is now optional

                LocalDate paymentDate;
                try {
                    paymentDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ex) {
                    throw new RuntimeException("Invalid date format. Use YYYY-MM-DD"); // Corrected message
                }
                // Allow past, present, or future dates

                double amount;
                try {
                    amount = Double.parseDouble(view.getTxtPaymentAmount().getText());
                    if (amount == 0) throw new NumberFormatException(); // zero not allowed
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Invalid payment amount (cannot be zero)");
                }

                double expected = model.getAgreementExpectedAmount(agreementId);
                double paidBefore = model.getAgreementTotalPaid(agreementId); // Total before this payment
                double newTotal = paidBefore + amount; // Calculate new total (handles negative amounts)

                String message;
                String dateStatusMsg;
                if (paymentDate.isBefore(workingDate)) {
                    dateStatusMsg = "This payment date is in the PAST (" + paymentDate + " vs Working Date " + workingDate + ").\n";
                } else if (paymentDate.isEqual(workingDate)) {
                    dateStatusMsg = "This payment date is the WORKING DATE (" + paymentDate + ").\n";
                } else { // paymentDate.isAfter(workingDate)
                    dateStatusMsg = "This payment date is in the FUTURE (" + paymentDate + " vs Working Date " + workingDate + ").\n";
                }


                if (amount < 0) {
                     // Refund/Correction confirmation
                     String totalAfter = String.format("%.2f €", newTotal);
                     if (newTotal < 0) totalAfter += " (Resulting in Negative Balance)";
                     message = dateStatusMsg +
                            "This refund/correction will reduce the total paid by " +
                            String.format("%.2f €", Math.abs(amount)) +
                            ". Total paid will become " + totalAfter + "." +
                            "\nDo you wish to continue?";

                } else if (Math.abs(newTotal - expected) < 0.01) {
                    message = dateStatusMsg +
                            "This payment will complete the agreement (Total: " + String.format("%.2f €", newTotal) + ")." +
                            "\nConfirm payment?";
                } else if (newTotal < expected) {
                     message = dateStatusMsg +
                            "You are recording a partial payment. Total paid will be " + String.format("%.2f €", newTotal) +
                            " (" + String.format("%.2f €", expected - newTotal) + " remaining)." +
                            "\nThe agreement will remain pending. Do you wish to continue?";
                } else { // newTotal > expected
                    message = dateStatusMsg +
                            "This payment will result in an overpayment by " +
                            String.format("%.2f €", newTotal - expected) +
                            ". Total paid will be " + String.format("%.2f €", newTotal) + "." +
                            "\nThe agreement status will remain as Pending. Do you wish to continue?";
                }


                int confirm = JOptionPane.showConfirmDialog(view, message, "Confirm Payment", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                // Insert the payment movement
                // Pass null if concept is empty string
                RegisterPaymentDTO dto = new RegisterPaymentDTO(dateStr, concept.isEmpty() ? null : concept, amount, invoiceId);
                model.insertPayment(dto);

                // Determine the NEW status based on the total paid *after* this movement
                String newStatus;
                 // Recalculate paid after insertion just to be safe, although newTotal should be correct
                 double totalPaidAfterInsert = model.getAgreementTotalPaid(agreementId);
                 if (Math.abs(totalPaidAfterInsert - expected) < 0.01) { // Using tolerance for exact comparison
                     newStatus = "Paid";
                 } else {
                     newStatus = "Agreed"; // Any amount not exactly equal to expected means it's pending/overpaid/underpaid
                 }

                 // Update the agreement status in the database
                 model.updateAgreementStatus(agreementId, newStatus);


                // Reload both tables to reflect the changes
                loadPendingAgreements();
                loadPaidAgreements();

                // Reselect the agreement in the appropriate table based on the NEW status
                 if ("Paid".equals(newStatus)) {
                     selectAgreementInPaidTable(agreementId);
                 } else { // Status is "Agreed"
                    reselectPendingAgreement(agreementId);
                }

                // Clear the payment input fields after successful registration
                view.clearPaymentFields(); // This clears amount, concept, labels, disables button
                 // Re-set the default date to the working date after clearing fields
                 view.getTxtPaymentDate().setText(workingDate.toString());


                JOptionPane.showMessageDialog(view, "Payment registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Unexpected error during payment registration: " + ex.getMessage());
                 ex.printStackTrace();
            }
        });
    }

    // Reselect the agreement row in the Pending table
    private void reselectPendingAgreement(int agreementId) {
        JTable table = view.getTableAgreements();
        DefaultTableModel modelTable = (DefaultTableModel) table.getModel();
        for (int i = 0; i < modelTable.getRowCount(); i++) {
             // Get ID from the model's column 0 (hidden)
             Object cellValue = modelTable.getValueAt(i, 0);
             if (cellValue instanceof Integer && (Integer) cellValue == agreementId) {
                table.setRowSelectionInterval(i, i);
                // Trigger updateSelectedAgreementDetails to refresh details panel
                 SwingUtilities.invokeLater(() -> updateSelectedAgreementDetails());
                break;
            }
        }
    }

    // Select the agreement in the Paid Agreements table
    private void selectAgreementInPaidTable(int agreementId) {
        JTable table = view.getTablePaidAgreements();
        DefaultTableModel modelTable = (DefaultTableModel) table.getModel();
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            // Get ID from the model's column 0 (hidden)
            Object cellValue = modelTable.getValueAt(i, 0);
            if (cellValue instanceof Integer && (Integer) cellValue == agreementId) {
                table.setRowSelectionInterval(i, i);
                 // Trigger updateSelectedAgreementDetails to refresh details panel
                 SwingUtilities.invokeLater(() -> updateSelectedAgreementDetails());
                break;
            }
        }
    }

    // Show error messages using a dialog
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --- Helper methods for loading and hiding columns ---

     private void loadAgreementsByStatus(String status, JTable table) {
         List<Object[]> agreements = model.getAgreementsByStatus(status);
         DefaultTableModel tableModel = new DefaultTableModel(
                 // Model headers include ID for internal use
                 new Object[]{"Agreement ID", "Sponsor", "Contact", "Amount (€)", "Status"}, 0
         ) {
             // Override isCellEditable to make table read-only
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }

             // Optional: Override getColumnClass for sorting/rendering hints
              @Override
              public Class<?> getColumnClass(int columnIndex) {
                  if (columnIndex == 0) return Integer.class; // Agreement ID
                  if (columnIndex == 3) return Double.class; // Amount
                  return String.class; // Other columns
              }
         };

         for (Object[] rowData : agreements) {
             // Add the entire rowData including ID to the model
             tableModel.addRow(rowData);
         }

         if ("Agreed".equals(status)) {
             view.setTableModelAgreements(tableModel);
         } else { // Status is "Paid"
             view.setTableModelPaidAgreements(tableModel);
         }

         // Hide the Agreement ID column (index 0) after setting the model
         hideTableColumn(table, 0);
     }

     // Helper to hide a specific column by its model index
     private void hideTableColumn(JTable table, int columnModelIndex) {
         // Ensure columnModelIndex is valid
         if (columnModelIndex < 0 || columnModelIndex >= table.getColumnModel().getColumnCount()) {
             System.err.println("Error: Cannot hide column. Invalid column model index: " + columnModelIndex);
             return;
         }

         TableColumn column = table.getColumnModel().getColumn(columnModelIndex);
         column.setMinWidth(0);
         column.setMaxWidth(0);
         column.setPreferredWidth(0); // Use preferred width too
         column.setResizable(false);
     }

     // The actual methods called to load tables - they use the helper now
     private void loadPendingAgreements() {
         loadAgreementsByStatus("Agreed", view.getTableAgreements());
     }

     private void loadPaidAgreements() {
         loadAgreementsByStatus("Paid", view.getTablePaidAgreements());
     }
}