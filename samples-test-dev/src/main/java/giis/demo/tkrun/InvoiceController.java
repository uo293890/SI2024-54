package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import giis.demo.util.Database;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private Database db = new Database();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        initView();
        initController();
    }

    private void initView() {
        view.setVisible(true);
    }

    private void initController() {
        view.getGenerateButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (generateInvoice()) {
                    registerInvoice();
                }
            }
        });

        view.getSendButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendInvoice();
            }
        });
    }

    private boolean generateInvoice() {
        try {
            String manualId = view.getInvoiceNumber().trim();
            if (manualId.isEmpty()) {
                manualId = model.generateInvoiceNumber();
                view.setInvoiceNumber(manualId);
            }
            String currentDate = dateFormat.format(new Date());
            view.setInvoiceDate(currentDate);
            return true;
        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
            return false;
        }
    }

    private void registerInvoice() {
        try {
            if (view.getInvoiceNumber().trim().isEmpty()) {
                view.showError("Invoice ID is required.");
                return;
            }
            if (view.getSelectedActivity() == null) {
                view.showError("You must select an activity.");
                return;
            }
            if (view.getSelectedAgreement() == null) {
                view.showError("You must select an agreement.");
                return;
            }

            String[] activityData = view.getSelectedActivity().split(" - ");
            String[] agreementData = view.getSelectedAgreement().split(" - ");
            
            model.saveInvoice(
                view.getInvoiceNumber().trim(),
                Integer.parseInt(activityData[0]),
                Integer.parseInt(agreementData[0]),
                new Date(dateFormat.parse(view.getInvoiceDate().trim()).getTime()),
                Double.parseDouble(view.getInvoiceVat().trim()),
                view.getRecipientName().trim(),
                view.getRecipientTaxId().trim(),
                view.getRecipientAddress().trim(),
                view.getContactEmail().trim()
            );
            
            view.showMessage("Invoice registered successfully.");
        } catch (Exception ex) {
            view.showError("Error registering the invoice: " + ex.getMessage());
        }
    }

    private void sendInvoice() {
        try {
            if (view.getInvoiceNumber().trim().isEmpty()) {
                view.showError("Invoice ID is required to send the invoice.");
                return;
            }
            if (view.getContactEmail().trim().isEmpty()) {
                view.showError("Email address is required to send the invoice.");
                return;
            }
            
            Date sentDate = new Date();
            model.updateInvoiceSentDate(view.getInvoiceNumber().trim(), sentDate);
            
            view.showMessage("Invoice sent successfully on " + dateFormat.format(sentDate));
        } catch (Exception ex) {
            view.showError("Error sending the invoice: " + ex.getMessage());
        }
    }
}