package giis.demo.tkrun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller class for managing the invoice generation and sending process.
 * It handles user interactions from the InvoiceView and coordinates business logic via InvoiceModel.
 * Ensures that invoices are only generated under valid conditions (e.g., 4 weeks before the event).
 */

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructs a new InvoiceController.
     *
     * @param model The InvoiceModel responsible for data access.
     * @param view  The InvoiceView representing the UI.
     */
    
    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        initController();
        loadActivities();
    }

    
    /**
     * Initializes UI listeners and control flow.
     * Connects buttons and dropdowns to their corresponding controller actions.
     */
    private void initController() {
        view.getGenerateButton().addActionListener(e -> generateInvoice());
        view.getSendButton().addActionListener(e -> sendInvoice());
        view.getActivityDropdown().addActionListener(e -> loadAgreementsForSelectedActivity());
    }

    
    /**
     * Loads all available event activities from the database and populates the activity dropdown.
     */

    private void loadActivities() {
        try {
            List<Object[]> activities = model.getAllEvents();
            view.populateActivityDropdown(activities);
        } catch (Exception e) {
            view.showError("Error loading activities: " + e.getMessage());
        }
    }

    
    /**
     * Loads all sponsorship agreements for the currently selected activity.
     * Populates the agreement table in the view with the relevant data.
     */
    private void loadAgreementsForSelectedActivity() {
        try {
            String selectedActivity = view.getSelectedActivity();
            if (selectedActivity == null || selectedActivity.isEmpty()) {
                return;
            }
            List<Object[]> agreements = model.getAgreementsForActivity(selectedActivity);
            view.populateAgreementTable(agreements);
        } catch (Exception e) {
            view.showError("Error loading agreements: " + e.getMessage());
        }
    }

    
    /**
     * Validates invoice generation constraints, such as the 4-week requirement before event start.
     * If valid, saves a new invoice in the database and updates the UI accordingly.
     */
    private void generateInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            double invoiceVat = Double.parseDouble(view.getInvoiceVat().trim());
            int agreementId = Integer.parseInt(view.getSelectedAgreement());

            String activityName = view.getSelectedActivity();
            Date eventDate = model.getEventStartDate(activityName);
            Date today = new Date();

            long diffInMillis = eventDate.getTime() - today.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

            // DEBUG: imprime fechas
            System.out.println("Event date: " + eventDate);
            System.out.println("Today: " + today);
            System.out.println("Days between: " + diffInDays);

            if (diffInDays < 28) {
                view.showError("The invoice must be generated at least 4 weeks before the event.");
                return;
            }

            model.saveInvoice(invoiceNumber, agreementId, invoiceVat);

            view.showMessage("Invoice generated successfully. You can now send it.");
            view.addGeneratedInvoice(invoiceNumber, "", view.getRecipientName(), view.getInvoiceVat());

            view.getSendButton().setEnabled(true);
            view.removeSelectedAgreement();

        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
        }
    }


    
    /**
     * Finalizes the invoice process by recording the send date and registering a financial movement.
     * Updates the view with the sending date and disables further interactions.
     */
    private void sendInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            String invoiceDate = dateFormat.format(new Date());

            // Registrar fecha de envío en la factura
            model.setInvoiceDate(invoiceNumber, invoiceDate);

            // Registrar movimiento de envío
            model.recordMovementForInvoice(invoiceNumber, invoiceDate);

            // Mostrar fecha en el campo de la vista
            view.updateInvoiceDateField(invoiceDate);

            view.showMessage("Invoice sent successfully on " + invoiceDate);

            // Opcional: podrías actualizar la fila en la tabla de facturas generadas si se desea
            // (por ejemplo, actualizar columna "Date Sent")

            view.getSendButton().setEnabled(false);
            view.clearInvoiceFields();

        } catch (Exception ex) {
            view.showError("Error sending invoice: " + ex.getMessage());
        }
    }
}
