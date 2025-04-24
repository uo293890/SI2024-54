package giis.demo.tkrun;

// Removed import giis.demo.util.Database; // Database instance is managed by the Model

import java.sql.SQLException; // Import SQLException to catch specific database errors
// import java.text.SimpleDateFormat; // No longer strictly needed for this flow
import java.time.LocalDate; // Using modern date/time API
import java.time.ZoneId; // Needed for converting old Date objects if still returned by model
import java.time.format.DateTimeParseException; // For parsing exceptions
import java.time.format.DateTimeFormatter; // For formatting/parsing dates
import java.util.Date; // Still needed for interaction with existing model method signature
import java.util.List;
import java.util.Objects; // For Objects.requireNonNull

/**
 * Controller class for managing the invoice processing process (generate and send).
 * It handles user interactions from the InvoiceView and coordinates business logic via InvoiceModel.
 * Ensures that invoices are processed under valid conditions according to business rules
 * and Spanish simplified invoice regulations.
 */
public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    // Using modern DateTimeFormatter for consistent date parsing and formatting (yyyy-MM-dd)
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private LocalDate workingDate; // The date the application considers "today"

    // Removed generatedInvoiceNumber field as it's not needed for single-step process state management

    /**
     * Constructs an InvoiceController.
     *
     * @param model       The InvoiceModel for data access and business logic.
     * @param view        The InvoiceView for user interface interactions.
     * @param workingDate The date considered "today" for application logic (e.g., LocalDate.now()).
     */
    public InvoiceController(InvoiceModel model, InvoiceView view, LocalDate workingDate) {
        // Use Objects.requireNonNull for basic null checks on dependencies
        this.model = Objects.requireNonNull(model, "InvoiceModel cannot be null");
        this.view = Objects.requireNonNull(view, "InvoiceView cannot be null");
        this.workingDate = Objects.requireNonNull(workingDate, "WorkingDate cannot be null");

        initController(); // Set up event listeners
        loadActivities(); // Load initial data into the view
    }

    /**
     * Initializes action listeners for the view components.
     */
    private void initController() {
        // Add action listener to the single 'Process Invoice' button
        view.getProcessButton().addActionListener(e -> processInvoice());
        // Add listener to the activity dropdown to reload agreements when activity changes
        view.getActivityDropdown().addActionListener(e -> loadAgreementsForSelectedActivity());

        // Initial state: process button is disabled until an agreement is selected
        view.getProcessButton().setEnabled(false);
    }

    /**
     * Loads distinct activity names from the model and populates the activity dropdown in the view.
     * Handles potential database errors during data retrieval.
     */
    private void loadActivities() {
        try {
            List<Object[]> activities = model.getAllEvents();
            view.populateActivityDropdown(activities);
            // After loading activities, attempt to load agreements for the initially selected one (if any)
            loadAgreementsForSelectedActivity(); // Call this after populating activities
        } catch (SQLException e) {
            // Improved error reporting for SQL exceptions
            view.showError("Database error loading activities: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to console for debugging
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            view.showError("Unexpected error loading activities: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to console for debugging
        }
    }

    /**
     * Loads all agreement data for the selected activity from the model
     * and populates the agreement table in the view.
     * Handles potential database errors during data retrieval.
     */
    private void loadAgreementsForSelectedActivity() {
        try {
            String selectedActivity = view.getSelectedActivity();
            if (selectedActivity == null || selectedActivity.isEmpty()) {
                // Clear agreements table if no activity is selected or activity changes to empty
                view.populateAgreementTable(java.util.Collections.emptyList());
                // Note: Generated invoices table is not cleared here as it persists across activity selections

                // Clear invoice detail fields and disable the process button
                view.clearInvoiceFields();
                view.getProcessButton().setEnabled(false);
                return;
            }
            // Retrieve agreements for the selected activity from the model
            List<Object[]> agreements = model.getAgreementsForActivity(selectedActivity);
            view.populateAgreementTable(agreements);

            // After loading agreements, clear invoice detail fields and disable the process button
            // The button will be re-enabled by the table's mouse listener when an agreement is selected.
            view.clearInvoiceFields();
            view.getProcessButton().setEnabled(false);

        } catch (SQLException e) {
            // Improved error reporting for SQL exceptions
            view.showError("Database error loading agreements: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to console for debugging
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            view.showError("Unexpected error loading agreements: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to console for debugging
        }
    }

    /**
     * Handles the single action triggered by the "Generate and Send Invoice" button.
     * This method validates user input, performs date checks and warnings,
     * saves the invoice data to the database, records the sending movement,
     * and updates the view by removing the agreement and adding the invoice to the processed list.
     */
    private void processInvoice() {
        try {
            // --- 1. Retrieve and Validate User Input ---
            String invoiceNumber = view.getInvoiceNumber().trim();
            String invoiceDateStr = view.getInvoiceDate().trim(); // Get the date entered by the user
            String vatStr = view.getInvoiceVat().trim(); // Get VAT (should be 21)
            double invoiceVat;

            // Check mandatory fields
            if (invoiceNumber.isEmpty()) {
                view.showError("Invoice Number cannot be empty.");
                return;
            }
            if (invoiceDateStr.isEmpty()) {
                view.showError("Invoice Date cannot be empty.");
                return;
            }
             // Validate VAT format
             try {
                 invoiceVat = Double.parseDouble(vatStr);
            } catch (NumberFormatException e) {
                 view.showError("Invalid VAT value: '" + vatStr + "'. Please enter a number.");
                 return;
            }

            // Get the selected Agreement ID from the table in the view
            Integer agreementId = view.getSelectedAgreementId();

            // Check if an agreement is actually selected (should be handled by button state, but belt+suspenders)
            if (agreementId == null) {
                view.showError("Please select an agreement first.");
                return;
            }

            // Parse and validate Invoice Date format (yyyy-MM-dd) using java.time
            LocalDate invoiceDate;
            try {
                invoiceDate = LocalDate.parse(invoiceDateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                view.showError("Invalid date format for Invoice Date. Please use YYYY-MM-DD.");
                return;
            }

            // Create an Invoice DTO for data transfer and recipient validation
            InvoiceDTO invoice = new InvoiceDTO(invoiceNumber, agreementId, invoiceVat);
            // Populate recipient data from the view fields (which were populated from the selected agreement row)
            invoice.setRecipientName(view.getRecipientName());
            invoice.setRecipientTaxId(view.getRecipientTaxId());
            invoice.setRecipientAddress(view.getRecipientAddress());

            // Validate recipient tax data according to simplified invoice rules
            if (!invoice.hasValidRecipientData()) {
                // This check relies on the correct data being populated into the view fields from the agreement table
                view.showError("Recipient tax data (name, tax ID, address) must be provided and not empty to generate a simplified invoice.");
                return;
            }

            // --- 2. Perform Date Checks and Warnings ---

            // Warning based on Invoice Date relative to Working Date (today)
            if (invoiceDate.isBefore(workingDate)) {
                view.showWarning("Invoice date (" + invoiceDateStr + ") is in the past relative to the working date (" + workingDate.format(dateFormatter) + ").");
            } else if (invoiceDate.isAfter(workingDate)) {
                view.showWarning("Invoice date (" + invoiceDateStr + ") is in the future relative to the working date (" + workingDate.format(dateFormatter) + ").");
            } else { // invoiceDate.isEqual(workingDate)
                view.showWarning("Invoice date (" + invoiceDateStr + ") is the same as the working date (" + workingDate.format(dateFormatter) + ").");
            }

            // Retrieve Event Start Date for the selected activity
            String activityName = view.getSelectedActivity();
            if (activityName == null || activityName.isEmpty()) {
                 view.showError("Please select an activity."); // Should not happen if button is enabled, but safety check
                 return;
             }
            Date eventDateUtil = model.getEventStartDate(activityName); // Model method returns java.util.Date

            // Ensure event date was successfully retrieved and is not null
            if (eventDateUtil == null) {
                 view.showError("Could not retrieve event start date for activity: " + activityName);
                 return;
            }

            // Convert java.sql.Date (expected type of eventDateUtil) to java.time.LocalDate
            // Use the correct method to avoid java.lang.UnsupportedOperationException
            LocalDate eventDate = ((java.sql.Date) eventDateUtil).toLocalDate();

            // Warning/Confirmation based on Event Date relative to Working Date (advance sending rule)
            long diffWorkingDateToEvent = java.time.temporal.ChronoUnit.DAYS.between(workingDate, eventDate);

            // According to the rule: invoices are generated max 4 weeks before, UNLESS sponsor requests advance.
            // 'Advance' means *earlier* than 4 weeks before the event.
            // So, if the event date is MORE than 4 weeks away from TODAY (workingDate), we need confirmation for advance sending.
            if (diffWorkingDateToEvent > 28) {
                 int confirm = javax.swing.JOptionPane.showConfirmDialog(view, // Use 'view' as the parent component for the dialog
                         "The event date (" + eventDate.format(dateFormatter) + ") is more than 4 weeks from the working date (" + workingDate.format(dateFormatter) + ").\n" +
                         "Are you sure the sponsor requested this invoice in advance?",
                         "Advance Invoice Warning", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
                 if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                     return; // User cancelled the advance request confirmation
                 }
            }
            // If diffWorkingDateToEvent <= 28, the event is within the standard 4-week window, no special advance warning needed.

            // --- 3. Database Actions (Generate and Send - Combined) ---
            // Perform database operations within a single try block.
            // If any step fails (e.g., SQLException on INSERT or UPDATE), the catch block will be triggered.

            // Save the initial invoice data (invoice_number, agreement_id, invoice_vat).
            // Note: The model's saveInvoice doesn't initially set the date in the DB according to its structure.
            model.saveInvoice(invoice.getInvoiceNumber(), invoice.getAgreementId(), invoice.getInvoiceVat());

            // Set the invoice date in the database for the newly saved invoice.
            // This effectively "dates" the invoice.
            model.setInvoiceDate(invoice.getInvoiceNumber(), invoiceDateStr); // Use the user-provided date

            // Record the movement associated with sending the invoice.
            // This marks the invoice as "sent" on the specified date.
            model.recordMovementForInvoice(invoice.getInvoiceNumber(), invoiceDateStr); // Use the invoice date as the sent date

            // --- 4. Update View ---
            // Add the processed invoice to the 'Processed Invoices' table in the view, including its date.
            view.addProcessedInvoice(invoice.getInvoiceNumber(), invoiceDateStr, invoice.getRecipientName(), String.valueOf(invoice.getInvoiceVat()), activityName);

            // Show success message to the user.
            view.showMessage("Invoice processed (generated and sent) successfully.");

            // Remove the processed agreement from the 'Select Agreement' table in the view.
            // The View's method removeSelectedAgreement() should now ONLY remove the row.
            view.removeSelectedAgreement();

            // Clear all input fields in the invoice details section for the next operation.
            view.clearInvoiceFields();

            // Disable the process button as no agreement is currently selected.
            // The button will be re-enabled by the table's mouse listener if another agreement is selected.
            view.getProcessButton().setEnabled(false);


        } catch (SQLException e) {
            // Catch specific SQL exceptions for database-related errors during processing.
            e.printStackTrace(); // Print stack trace to console for detailed debugging.
            view.showError("Database error processing invoice: " + e.getMessage());
            // Fields and button state remain as they were before the error, allowing the user to see input data.
        } catch (Exception ex) {
            // Catch any other unexpected exceptions that might occur.
            ex.printStackTrace(); // Print stack trace to console for detailed debugging.

            String errorMessage = "Error processing invoice";
            // Provide more detail in the error message if the exception message is null or empty.
            if (ex.getMessage() != null && !ex.getMessage().trim().isEmpty()) {
                errorMessage += ": " + ex.getMessage();
            } else {
                // Include the exception type for better diagnosis if no specific message is available.
                errorMessage += ". An unexpected error occurred (" + ex.getClass().getName() + ")";
            }
            view.showError(errorMessage);
            // Fields and button state remain as they were before the error.
        }
    }

    // The 'sendInvoice' method is removed as its logic is merged into 'processInvoice'.
    // The example 'main' method is removed as the controller should be instantiated by the main application.

}