package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat;
    
    // Datos fiscales del emisor (COIIPA)
    private static final String ISSUER_NAME = "COIIPA";
    private static final String ISSUER_FISCAL_NUMBER = "A12345678"; // Ejemplo
    private static final String ISSUER_ADDRESS = "123 Issuer Street, City";
    private static final String ISSUER_EMAIL = "issuer@coiipa.es";
    
    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
        
        view.getSendButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendInvoice();
            }
        });
    }
    
    // Genera el número de factura y asigna la fecha actual
    private void generateInvoice() {
        try {
            InvoiceDTO newInvoice = new InvoiceDTO();
            newInvoice.setInvoiceNumber(model.generateInvoiceNumber());
            newInvoice.setInvoiceDate(new Date());
            view.setInvoiceNumber(newInvoice.getInvoiceNumber());
            view.setInvoiceDate(dateFormat.format(newInvoice.getInvoiceDate()));
        } catch (Exception ex) {
            view.showError("Error generating invoice: " + ex.getMessage());
        }
    }
    
    // Registra la factura en la base de datos (solo se almacenan los campos definidos en el DDL)
    private void registerInvoice() {
        try {
            // Validaciones de campos obligatorios para la factura
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
            // Validar el formato de la fecha de la factura
            Date invoiceDate;
            try {
                invoiceDate = dateFormat.parse(view.getInvoiceDate());
            } catch (Exception e) {
                view.showError("The invoice date format is incorrect. It should be dd/MM/yyyy.");
                return;
            }
            
            // Validar que el Agreement ID sea un entero
            int agreementId;
            try {
                agreementId = Integer.parseInt(view.getAgreementId());
            } catch (NumberFormatException e) {
                view.showError("Agreement ID must be an integer.");
                return;
            }
            
            // Validar que el VAT sea un número válido y no negativo
            double invoiceVat;
            try {
                invoiceVat = Double.parseDouble(view.getInvoiceVat());
            } catch (NumberFormatException e) {
                view.showError("Invoice VAT must be a valid number.");
                return;
            }
            if (invoiceVat < 0) {
                view.showError("Invoice VAT cannot be negative.");
                return;
            }
            
            // Validación opcional: formato del email del receptor (para envío)
            if (!view.getRecipientEmail().trim().isEmpty() && !view.getRecipientEmail().contains("@")) {
                view.showError("Recipient email is not valid.");
                return;
            }
            
            // Si se ha proporcionado la fecha del evento, comprobar que la factura se genera al menos 4 semanas (28 días) antes
            if (!view.getEventDate().trim().isEmpty()) {
                try {
                    Date eventDate = dateFormat.parse(view.getEventDate());
                    long diff = eventDate.getTime() - invoiceDate.getTime();
                    long daysDiff = diff / (1000 * 60 * 60 * 24);
                    if (daysDiff < 28) {
                        int response = JOptionPane.showConfirmDialog(view,
                            "The invoice is being generated less than 4 weeks before the event. Do you want to proceed?",
                            "Warning", JOptionPane.YES_NO_OPTION);
                        if (response != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                } catch (Exception e) {
                    view.showError("The event date format is incorrect. It should be dd/MM/yyyy.");
                    return;
                }
            }
            
            // Crear y poblar el InvoiceDTO (solo los datos que se guardan en la tabla Invoice)
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setInvoiceNumber(view.getInvoiceNumber());
            invoice.setInvoiceDate(invoiceDate);
            invoice.setAgreementId(agreementId);
            invoice.setInvoiceVat(invoiceVat);
            
            model.saveInvoice(invoice);
            view.showMessage("Invoice registered successfully.");
            // Opcional: limpiar el formulario
            // view.clearForm();
        } catch (Exception ex) {
            view.showError("Error registering invoice: " + ex.getMessage());
        }
    }
    
    // Simula el envío de la factura por email utilizando los datos adicionales (no almacenados en la tabla Invoice)
    private void sendInvoice() {
        try {
            if (view.getInvoiceNumber().trim().isEmpty()) {
                view.showError("Invoice number is required to send the invoice.");
                return;
            }
            if (view.getRecipientEmail().trim().isEmpty()) {
                view.showError("Recipient email is required to send the invoice.");
                return;
            }
            
            // Construir el contenido del email
            String emailContent = "Dear " + view.getRecipientName() + ",\n\n" +
                "Please find attached your invoice details below:\n" +
                "Invoice Number: " + view.getInvoiceNumber() + "\n" +
                "Invoice Date: " + view.getInvoiceDate() + "\n" +
                "Invoice VAT: " + view.getInvoiceVat() + "\n\n" +
                "Issuer: " + ISSUER_NAME + "\n" +
                "Issuer Fiscal Number: " + ISSUER_FISCAL_NUMBER + "\n" +
                "Issuer Address: " + ISSUER_ADDRESS + "\n\n" +
                "Thank you.";
            
            // Simulación del envío del email (en un escenario real se integraría un API de correo)
            view.showMessage("Invoice sent to " + view.getRecipientEmail() + " successfully.\n\n" + emailContent);
        } catch (Exception ex) {
            view.showError("Error sending invoice: " + ex.getMessage());
        }
    }
}
