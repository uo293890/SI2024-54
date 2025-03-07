package giis.demo.tkrun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * This class acts as the controller for the OtherMovementView.
 * It handles user interactions and communicates with the model to register movements.
 */
public class OtherMovementController {
    private OtherMovementModel model; // Reference to the model
    private OtherMovementView view; // Reference to the view

    /**
     * Constructor to initialize the controller.
     * @param model The model for registering movements.
     * @param view The view for user interaction.
     */
    public OtherMovementController(OtherMovementModel model, OtherMovementView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    /**
     * Initializes the controller by setting up event listeners.
     */
    private void initController() {
        // Add an action listener to the register button
        view.getRegisterButton().addActionListener(e -> registerMovement());
    }

    /**
     * Handles the registration of a movement.
     */
    private void registerMovement() {
        try {
            // Parse the payment date if the status is "Paid"
            Date paymentDate = null;
            String paymentDateStr = view.getPaymentDate();
            if (!paymentDateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                paymentDate = sdf.parse(paymentDateStr);
            }

            // Create a new OtherMovementDTO object with user input
            OtherMovementDTO movement = new OtherMovementDTO();
            movement.setEditionId(view.getEditionId());
            movement.setConcept(view.getConcept());
            movement.setAmount(view.getAmount());
            movement.setType(view.getMovementType());
            movement.setStatus(paymentDate != null ? "Paid" : "Estimated");
            movement.setPaymentDate(paymentDate);

            // Register the movement using the model
            model.registerMovement(movement);

            // Show a success message
            JOptionPane.showMessageDialog(view, "Movement registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException ex) {
            // Handle invalid date format
            JOptionPane.showMessageDialog(view, "Invalid date format (Use dd/MM/yyyy).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Handle other errors
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}