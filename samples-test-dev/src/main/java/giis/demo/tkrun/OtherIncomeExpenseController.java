package giis.demo.tkrun;

import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementStatus;
import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementType;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class OtherIncomeExpenseController {

    private final OtherIncomeExpenseModel model;
    private final OtherIncomeExpenseView view;

    public OtherIncomeExpenseController(OtherIncomeExpenseModel model, OtherIncomeExpenseView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        // Register button
        view.getBtnRegister().addActionListener(e -> onRegisterMovement());

        // Cancel button
        view.getBtnCancel().addActionListener(e -> onCancel());
    }

    /**
     * Invoked when the user clicks "Register" to store a new income or expense.
     */
    private void onRegisterMovement() {
        try {
            // 1. Retrieve data from the view
            MovementType movementType = (MovementType) view.getComboType().getSelectedItem();
            MovementStatus movementStatus = (MovementStatus) view.getComboStatus().getSelectedItem();

            // Event ID could be obtained from a combo box or a hidden field
            // For example, if your view has a method getSelectedEventId():
            int eventId = view.getSelectedEventId(); 

            // Amount (could be negative for expense or positive for income, 
            // or simply a numeric field if type is chosen in the combo)
            double estimatedAmount = Double.parseDouble(view.getTxtAmount().getText());

            // If paid, the user might enter a paid amount different from the estimate
            double paidAmount = 0.0;
            LocalDate paidDate = null;
            if (movementStatus == MovementStatus.PAID) {
                paidAmount = Double.parseDouble(view.getTxtPaidAmount().getText());
                // For the paid date, assume user enters "yyyy-MM-dd" in a text field or uses a date picker
                try {
                    paidDate = LocalDate.parse(view.getTxtPaidDate().getText());
                } catch (DateTimeParseException dtpe) {
                    // If the user leaves the date blank or types it incorrectly, handle accordingly
                    showError("Invalid paid date format. Please use yyyy-MM-dd.");
                    return;
                }
            }

            // Concept / short description
            String concept = view.getTxtConcept().getText().trim();
            if (concept.isEmpty()) {
                showError("Please enter a concept/description for this movement.");
                return;
            }

            // 2. Create the DTO
            OtherIncomeExpenseDTO dto = new OtherIncomeExpenseDTO(
                0,                  // movementId (0 if this is a new record)
                eventId,            // The event ID
                movementType,       // INCOME or EXPENSE
                movementStatus,     // ESTIMATED or PAID
                estimatedAmount,    // The estimated amount
                paidAmount,         // The final paid amount (only if MovementStatus == PAID)
                paidDate,           // The date when it was paid (if applicable)
                concept             // e.g. "Room fee"
            );

            // 3. Save it via the model
            model.saveMovement(dto);

            // 4. Notify user
            JOptionPane.showMessageDialog(view, "Movement registered successfully!", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);

            // 5. Optionally clear the form or close the dialog
            clearForm();

        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric amount.");
        } catch (Exception ex) {
            showError("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * Invoked when the user clicks "Cancel".
     * Here we simply close the dialog/form, but you can do something else if needed.
     */
    private void onCancel() {
        view.dispose();
    }

    /**
     * Clears the form fields after successfully registering a movement.
     * You can also call view.dispose() if you want to close the window.
     */
    private void clearForm() {
        view.getComboType().setSelectedIndex(0);
        view.getComboStatus().setSelectedIndex(0);
        view.getTxtAmount().setText("");
        view.getTxtPaidAmount().setText("");
        view.getTxtPaidDate().setText("");
        view.getTxtConcept().setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
