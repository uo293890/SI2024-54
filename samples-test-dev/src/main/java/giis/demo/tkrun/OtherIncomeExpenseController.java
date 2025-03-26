package giis.demo.tkrun;

import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementType;
import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementStatus;

import javax.swing.*;

/**
 * Controlador para la pantalla de "Register other income or expenses".
 */
public class OtherIncomeExpenseController {

    private OtherIncomeExpenseModel model;
    private OtherIncomeExpenseView view;
    private Object[][] eventsData; // Para cargar la lista de eventos

    public OtherIncomeExpenseController(OtherIncomeExpenseModel model, OtherIncomeExpenseView view) {
        this.model = model;
        this.view = view;
        initController();
        loadEventsCombo();
    }

    private void initController() {
        // Botón de Register
        view.getBtnRegister().addActionListener(e -> onRegister());
        // Botón de Cancel
        view.getBtnCancel().addActionListener(e -> onCancel());
    }

    /**
     * Carga los eventos desde la BD y los pone en el combo "Event".
     */
    private void loadEventsCombo() {
        try {
            eventsData = model.getAllEvents();
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            for (Object[] row : eventsData) {
                int id = (Integer) row[0];
                String name = (String) row[1];
                comboModel.addElement(id + " - " + name);
            }
            view.getComboEvent().setModel(comboModel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error loading events: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Se llama al presionar "Register" en la pantalla.
     * - Verifica que el tipo no sea "--None--".
     * - Obtiene el monto y lo ajusta (positivo si INCOME, negativo si EXPENSE).
     * - Fuerza el estado "ESTIMATED".
     * - Guarda en la BD.
     */
    private void onRegister() {
        try {
            // 1) Tipo de movimiento
            String selectedType = (String) view.getComboType().getSelectedItem();
            if ("--None--".equals(selectedType)) {
                throw new IllegalArgumentException("Please select a movement type.");
            }

            MovementType movementType = MovementType.valueOf(selectedType); 
            // Esto convierte "INCOME" o "EXPENSE" a MovementType.INCOME/EXPENSE

            // 2) Evento
            int eventIndex = view.getComboEvent().getSelectedIndex();
            if (eventIndex < 0) {
                throw new IllegalArgumentException("Please select an event.");
            }
            int eventId = (Integer) eventsData[eventIndex][0];

            // 3) Monto
            double amount = Double.parseDouble(view.getTxtAmount().getText().trim());
            if (movementType == MovementType.EXPENSE) {
                // Guardar como negativo
                amount = -Math.abs(amount);
            } else {
                // INCOME se guarda en positivo
                amount = Math.abs(amount);
            }

            // 4) Concept
            String concept = view.getTxtConcept().getText().trim();
            if (concept.isEmpty()) {
                throw new IllegalArgumentException("Please enter a concept (description).");
            }

            // Construir DTO
            OtherIncomeExpenseDTO dto = new OtherIncomeExpenseDTO(
                0,                   // movementId
                eventId,             // eventId
                movementType,        // INCOME / EXPENSE
                MovementStatus.ESTIMATED, // Forzamos "Estimated"
                amount,              // estimatedAmount
                0.0,                 // paidAmount (no se usa)
                null,                // paidDate (no se usa)
                concept
            );

            // Guardar en BD
            model.saveMovement(dto);

            JOptionPane.showMessageDialog(view, "Movement registered successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Invalid amount. Please enter a numeric value.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Unexpected error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Se llama al presionar "Cancel".
     * En este ejemplo simplemente limpia los campos. 
     * Podrías cerrar la ventana si prefieres.
     */
    private void onCancel() {
        clearForm();
    }

    private void clearForm() {
        view.getComboType().setSelectedIndex(0); // "--None--"
        view.getComboEvent().setSelectedIndex(0);
        view.getTxtAmount().setText("");
        view.getTxtConcept().setText("");
    }
}
