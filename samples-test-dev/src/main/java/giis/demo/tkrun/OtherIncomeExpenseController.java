package giis.demo.tkrun;

import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementStatus;
import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementType;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Controlador para la gestión de otros movimientos de ingreso/gasto.
 * 
 * - Permite registrar tanto estimados como pagados (aunque la BD
 *   restringe a 'Estimated' o 'Paid' con esa ortografía).
 */
public class OtherIncomeExpenseController {

    private final OtherIncomeExpenseModel model;
    private final OtherIncomeExpenseView view;
    // Almacena los eventos recuperados de la base de datos.
    private Object[][] eventsData;

    public OtherIncomeExpenseController(OtherIncomeExpenseModel model, OtherIncomeExpenseView view) {
        this.model = model;
        this.view = view;
        initController();
        populateEventCombo();
    }

    private void initController() {
        view.getBtnRegister().addActionListener(e -> onRegisterMovement());
        view.getBtnClear().addActionListener(e -> clearForm());
    }

    /**
     * Llena el combo de eventos desde la BD (Event con status 'Planned' o 'Closed').
     */
    private void populateEventCombo() {
        try {
            eventsData = model.getAllEvents();
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            for (int i = 0; i < eventsData.length; i++) {
                int id = (Integer) eventsData[i][0];
                String name = (String) eventsData[i][1];
                comboModel.addElement(id + " - " + name);
            }
            view.getComboEvent().setModel(comboModel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cargar eventos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registra un nuevo movimiento (estimado o pagado), validando:
     * - Campos numéricos
     * - Para "PAID": que se indique monto y fecha de pago válidos
     * - Concepto obligatorio
     */
    private void onRegisterMovement() {
        try {
            MovementType movementType = (MovementType) view.getComboType().getSelectedItem();
            MovementStatus movementStatus = (MovementStatus) view.getComboStatus().getSelectedItem();

            // Obtenemos el eventId desde el combo
            int selectedIndex = view.getComboEvent().getSelectedIndex();
            if (selectedIndex < 0) {
                throw new IllegalArgumentException("No se ha seleccionado ningún evento.");
            }
            int eventId = (Integer) eventsData[selectedIndex][0];

            double estimatedAmount = Double.parseDouble(view.getTxtEstimatedAmount().getText().trim());
            double paidAmount = 0.0;
            LocalDate paidDate = null;

            if (movementStatus == MovementStatus.PAID) {
                String paidAmountText = view.getTxtPaidAmount().getText().trim();
                if (paidAmountText.isEmpty()) {
                    throw new IllegalArgumentException("Debe ingresar un monto pagado para estado PAID.");
                }
                paidAmount = Double.parseDouble(paidAmountText);

                String paidDateText = view.getTxtPaidDate().getText().trim();
                if (paidDateText.isEmpty()) {
                    throw new IllegalArgumentException("Debe ingresar una fecha de pago para estado PAID.");
                }
                try {
                    paidDate = LocalDate.parse(paidDateText);
                } catch (DateTimeParseException dtpe) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd.");
                }
                LocalDate today = LocalDate.now();
                if (paidDate.isAfter(today)) {
                    throw new IllegalArgumentException("La fecha de pago no puede ser posterior a hoy (" + today + ").");
                }
            }

            String concept = view.getTxtConcept().getText().trim();
            if (concept.isEmpty()) {
                throw new IllegalArgumentException("El concepto es obligatorio.");
            }

            // Creamos el DTO
            OtherIncomeExpenseDTO dto = new OtherIncomeExpenseDTO(
                0, // nuevo
                eventId,
                movementType,
                movementStatus,
                estimatedAmount,
                paidAmount,
                paidDate,
                concept
            );

            // Guardamos en la BD
            model.saveMovement(dto);
            JOptionPane.showMessageDialog(view, "Movimiento registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Actualizamos la tabla
            refreshMovementsList();
            // Limpiamos el formulario
            clearForm();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "Valor numérico inválido. Verifique los montos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(view, iae.getMessage(), "Error de entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        view.getComboType().setSelectedIndex(0);
        view.getComboStatus().setSelectedIndex(0);
        view.getComboEvent().setSelectedIndex(0);
        view.getTxtEstimatedAmount().setText("");
        view.getTxtPaidAmount().setText("");
        view.getTxtPaidDate().setText("");
        view.getTxtConcept().setText("");
    }

    private void refreshMovementsList() {
        try {
            Object[][] data = model.getAllIncomesExpensesFull();
            view.updateMovementsTable(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al actualizar la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
