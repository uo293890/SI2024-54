package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

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
            List<List<Object>> activities = model.getAllEditions();
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
            List<List<Object>> agreements = model.getAgreementsForActivity(selectedActivity);
            view.populateAgreementTable(agreements);
        } catch (Exception e) {
            view.showError("Error loading agreements: " + e.getMessage());
        }
    }

    private void generateInvoice() {
        try {
            String invoiceNumber = generateInvoiceNumber();
            String issueDate = dateFormat.format(new Date());
            String invoiceDate = view.getInvoiceDate();
            double invoiceVat = Double.parseDouble(view.getInvoiceVat().trim());
            String recipientName = view.getRecipientName();
            String recipientTaxId = view.getRecipientTaxId();
            String recipientAddress = view.getRecipientAddress();
            int agreementId = Integer.parseInt(view.getSelectedAgreement());

            model.saveInvoice(invoiceNumber, agreementId, issueDate, invoiceDate, invoiceVat, recipientName, recipientTaxId, recipientAddress);

            view.showMessage("Invoice generated successfully.");
            view.removeSelectedAgreement(); // Remove from table after generating invoice
        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
        }
    }

    private void sendInvoice() {
        try {
            String invoiceNumber = view.getInvoiceNumber().trim();
            String sentDate = dateFormat.format(new Date());

            model.updateInvoiceSentDate(invoiceNumber, sentDate);
            view.showMessage("Invoice sent successfully on " + sentDate);
            view.removeSelectedAgreement(); // Remove from table after sending invoice
        } catch (Exception ex) {
            view.showError("Error sending invoice: " + ex.getMessage());
        }
    }

    private String generateInvoiceNumber() {
        return String.valueOf(System.currentTimeMillis()).substring(4);
    }
}