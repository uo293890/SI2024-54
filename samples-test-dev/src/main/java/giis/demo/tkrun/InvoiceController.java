// InvoiceController.java
package giis.demo.tkrun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        //view.getGenerateButton().addActionListener(e -> generateInvoice());
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

    /*private void generateInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            String invoiceDate = view.getInvoiceDate();
            double invoiceVat = Double.parseDouble(view.getInvoiceVat().trim());
            int agreementId = Integer.parseInt(view.getSelectedAgreement());

            String activityName = view.getSelectedActivity();
            Date eventDate = model.getEventStartDate(activityName);
            Date today = new Date();

            long diffInMillis = eventDate.getTime() - today.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

            if (diffInDays < 28) {
                view.showError("The invoice must be generated at least 4 weeks before the event.");
                return;
            }

            model.saveInvoice(invoiceNumber, agreementId, invoiceDate, invoiceVat);

            view.showMessage("Invoice generated successfully.");
            view.removeSelectedAgreement();
        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
        }
    }*/



    private void sendInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            String sentDate = dateFormat.format(new Date());

            model.recordMovementForInvoice(invoiceNumber, sentDate);
            view.showMessage("Invoice sent successfully on " + sentDate);
            view.removeSelectedAgreement();
        } catch (Exception ex) {
            view.showError("Error sending invoice: " + ex.getMessage());
        }
    }
}
