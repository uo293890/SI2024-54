package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat;

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        initView();
        initController();
    }

    private void initView() {
        view.setVisible(true);
    }

    private void initController() {
        view.getGenerateButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateInvoice();
            }
        });

        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerInvoice();
            }
        });
    }

    private void generateInvoice() {
        try {
            InvoiceDTO newInvoice = new InvoiceDTO();
            newInvoice.setInvoiceNumber(model.generateInvoiceNumber());
            newInvoice.setInvoiceDate(new Date());

            view.setInvoiceNumber(newInvoice.getInvoiceNumber());
            view.setInvoiceDate(dateFormat.format(newInvoice.getInvoiceDate()));
        } catch (Exception ex) {
            view.showError("Error al generar la factura: " + ex.getMessage());
        }
    }

    private void registerInvoice() {
        try {
            // Basic validations for input fields
            if (view.getInvoiceNumber().trim().isEmpty()) {
                view.showError("Invoice number cannot be empty.");
                return;
            }
            if (view.getInvoiceDate().trim().isEmpty()) {
                view.showError("Invoice date cannot be empty.");
                return;
            }
            if (view.getAgreementId().trim().isEmpty()) {
                view.showError("Agreement ID cannot be empty.");
                return;
            }
            if (view.getInvoiceVat().trim().isEmpty()) {
                view.showError("Invoice VAT cannot be empty.");
                return;
            }
            
            // Validate the date format
            Date invoiceDate;
            try {
                invoiceDate = dateFormat.parse(view.getInvoiceDate());
            } catch (Exception e) {
                view.showError("The date format is incorrect. It should be dd/MM/yyyy.");
                return;
            }
            
            // Validate that the Agreement ID is an integer
            int agreementId;
            try {
                agreementId = Integer.parseInt(view.getAgreementId());
            } catch (NumberFormatException e) {
                view.showError("Agreement ID must be an integer.");
                return;
            }
            
            // Validate that the VAT is a valid number
            double invoiceVat;
            try {
                invoiceVat = Double.parseDouble(view.getInvoiceVat());
            } catch (NumberFormatException e) {
                view.showError("Invoice VAT must be a valid number.");
                return;
            }
            
            // Optional: Additional check that VAT is non-negative
            if (invoiceVat < 0) {
                view.showError("Invoice VAT cannot be negative.");
                return;
            }
            
            // If all validations pass, create the DTO and save the invoice
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setInvoiceNumber(view.getInvoiceNumber());
            invoice.setInvoiceDate(invoiceDate);
            invoice.setAgreementId(agreementId);
            invoice.setInvoiceVat(invoiceVat);
            
            model.saveInvoice(invoice);
            view.showMessage("Invoice registered successfully");
            view.clearForm();
        } catch (Exception ex) {
            view.showError("Error registering invoice: " + ex.getMessage());
        }
    }

    }

