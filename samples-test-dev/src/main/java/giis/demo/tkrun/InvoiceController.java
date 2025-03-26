package giis.demo.tkrun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller class for managing the invoice generation and sending process.
 * It handles user interactions from the InvoiceView and coordinates business logic via InvoiceModel.
 * Ensures that invoices are only generated under valid conditions (e.g., 4 weeks before the event),
 * or earlier if the sponsor requests it, according to Spanish simplified invoice regulation.
 */
public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        initController();
        loadActivities();
    }

    private void initController() {
        view.getGenerateButton().addActionListener(e -> generateInvoice());
        view.getSendButton().addActionListener(e -> sendInvoice());
        view.getActivityDropdown().addActionListener(e -> loadAgreementsForSelectedActivity());
    }

    private void loadActivities() {
        try {
            List<Object[]> activities = model.getAllEvents();
            view.populateActivityDropdown(activities);
        } catch (Exception e) {
            view.showError("Error loading activities: " + e.getMessage());
        }
    }

    /**
     * Loads all agreement data for the selected activity.
     * The query returns 9 columns:
     * [0] Agreement ID (hidden),
     * [1] Sponsor Name,
     * [2] Contact Name,
     * [3] Email,
     * [4] Amount,
     * [5] Event Date,
     * [6] Event Location,
     * [7] Sponsorship Level,
     * [8] Agreement Status.
     */
    private void loadAgreementsForSelectedActivity() {
        try {
            String selectedActivity = view.getSelectedActivity();
            if (selectedActivity == null || selectedActivity.isEmpty()) {
                return;
            }
            // Retrieve full agreement data including new attributes and the hidden agreement ID
            List<Object[]> agreements = model.getAgreementsForActivity(selectedActivity);
            view.populateAgreementTable(agreements);
        } catch (Exception e) {
            view.showError("Error loading agreements: " + e.getMessage());
        }
    }

    private void generateInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            double invoiceVat = Double.parseDouble(view.getInvoiceVat().trim());
            // Retrieve the agreement ID from the hidden column (column index 0) of the selected row
            int agreementId = Integer.parseInt(view.getSelectedAgreement());

            InvoiceDTO invoice = new InvoiceDTO(invoiceNumber, agreementId, invoiceVat);
            invoice.setRecipientName(view.getRecipientName());
            invoice.setRecipientTaxId(view.getRecipientTaxId());
            invoice.setRecipientAddress(view.getRecipientAddress());

            if (!invoice.hasValidRecipientData()) {
                view.showError("Recipient tax data (name, tax ID, address) must be provided.");
                return;
            }

            String activityName = view.getSelectedActivity();
            Date eventDate = model.getEventStartDate(activityName);
            Date today = new Date();

            long diffInMillis = eventDate.getTime() - today.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

            if (diffInDays < 28) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(null,
                        "This invoice is being generated less than 4 weeks before the event.\n" +
                        "Are you sure the sponsor requested it in advance?",
                        "Invoice Warning", javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                    return;
                }
            }

            model.saveInvoice(invoice.getInvoiceNumber(), invoice.getAgreementId(), invoice.getInvoiceVat());

            view.showMessage("Invoice generated successfully. You can now send it.");
            view.addGeneratedInvoice(invoice.getInvoiceNumber(), "", invoice.getRecipientName(), String.valueOf(invoice.getInvoiceVat()));

            view.getSendButton().setEnabled(true);
            view.removeSelectedAgreement();

        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
        }
    }

    private void sendInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            String invoiceDate = dateFormat.format(new Date());

            model.setInvoiceDate(invoiceNumber, invoiceDate);
            model.recordMovementForInvoice(invoiceNumber, invoiceDate);

            view.updateInvoiceDateField(invoiceDate);
            view.showMessage("Invoice sent successfully on " + invoiceDate);

            view.getSendButton().setEnabled(false);
            view.clearInvoiceFields();

        } catch (Exception ex) {
            view.showError("Error sending invoice: " + ex.getMessage());
        }
    }
}
