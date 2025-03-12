package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.swing.JOptionPane;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        initView();
        initController();
        scheduleAutomaticInvoices();
    }

    private void initView() {
        view.setVisible(true);
    }

    private void initController() {
        view.getGenerateButton().addActionListener(e -> {
            if (generateInvoice()) {
                registerInvoice();
            }
        });
        
        view.getSendButton().addActionListener(e -> sendInvoice());
    }

    private boolean generateInvoice() {
        try {
            String invoiceId = UUID.randomUUID().toString().substring(0, 9).toUpperCase();
            view.setInvoiceNumber(invoiceId);
            view.setInvoiceDate(dateFormat.format(new Date()));
            return true;
        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
            return false;
        }
    }

    private void registerInvoice() {
        try {
            int agreementId = Integer.parseInt(view.getSelectedAgreement());
            model.saveInvoice(
                view.getInvoiceNumber().trim(),
                agreementId,
                new Date(),
                Double.parseDouble(view.getInvoiceVat().trim()),
                view.getRecipientName(),
                view.getRecipientTaxId(),
                view.getRecipientAddress()
            );
            
            view.showMessage("Invoice registered successfully.");
            view.loadInvoicesIntoTable(); // Refresh table
        } catch (Exception ex) {
            view.showError("Error registering the invoice: " + ex.getMessage());
        }
    }

    private void sendInvoice() {
        try {
            model.updateInvoiceSentDate(view.getInvoiceNumber().trim(), new Date());
            view.showMessage("Invoice sent successfully on " + dateFormat.format(new Date()));
        } catch (Exception ex) {
            view.showError("Error sending the invoice: " + ex.getMessage());
        }
    }

    private void scheduleAutomaticInvoices() {
        try {
            model.generateAutomaticInvoices();
        } catch (Exception ex) {
            view.showError("Error scheduling automatic invoices: " + ex.getMessage());
        }
    }
}