package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static final String ISSUER_NAME = "COIIPA";
    private static final String ISSUER_FISCAL_NUMBER = "A12345678";
    private static final String ISSUER_ADDRESS = "123 Issuer Street, City";
    private static final String ISSUER_EMAIL = "issuer@coiipa.es";

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        initView();
        initController();
        loadAvailableIds();
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

    
    private void loadAvailableIds() {
        try {
            List<InvoiceDTO> invoices = model.getAllInvoices();
            Object[][] data = new Object[invoices.size()][2];
            for (int i = 0; i < invoices.size(); i++) {
                InvoiceDTO inv = invoices.get(i);
                data[i][0] = inv.getInvoiceNumber();
                data[i][1] = inv.getRecipientName();
            }
            view.updateAvailableIdsTable(data);
        } catch (Exception ex) {
            view.showError("Error cargando IDs disponibles: " + ex.getMessage());
        }
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
            // Validación de campos obligatorios
            if (view.getInvoiceNumber().trim().isEmpty()) {
                view.showError("El ID de la factura es obligatorio.");
                return;
            }

            // Validar que el ID de la factura tenga 9 dígitos
            if (!isValidInvoiceId(view.getInvoiceNumber().trim())) {
                view.showError("El ID de la factura debe tener exactamente 9 dígitos.");
                return;
            }

            if (view.getInvoiceDate().trim().isEmpty()) {
                view.showError("La fecha de la factura es obligatoria.");
                return;
            }
            if (view.getRecipientName().trim().isEmpty()) {
                view.showError("El nombre del destinatario es obligatorio.");
                return;
            }
            if (view.getRecipientTaxId().trim().isEmpty()) {
                view.showError("El NIF/CIF es obligatorio.");
                return;
            }
            if (view.getRecipientAddress().trim().isEmpty()) {
                view.showError("La dirección fiscal es obligatoria.");
                return;
            }
            if (view.getContactEmail().trim().isEmpty()) {
                view.showError("El correo electrónico es obligatorio.");
                return;
            }

            // Validar formato del NIF/CIF
            if (!isValidNIF(view.getRecipientTaxId().trim())) {
            	view.showError("El formato del NIF/CIF/NIE es incorrecto. Ejemplos: NIF: 12345678Z, CIF: B12345678, NIE: X1234567L.");
                return;
            }

            // Validar formato del correo electrónico
            if (!isValidEmail(view.getContactEmail().trim())) {
                view.showError("El formato del correo electrónico es incorrecto. Ejemplo: usuario@dominio.com.");
                return;
            }

            // Validar formato de la fecha de la factura
            Date invoiceDate;
            try {
                invoiceDate = dateFormat.parse(view.getInvoiceDate().trim());
            } catch (ParseException pe) {
                view.showError("El formato de la fecha de la factura es incorrecto. Use dd/MM/yyyy. Ejemplo: 01/01/2025.");
                return;
            }

            // Validar la fecha del evento
            String eventDateStr = view.getEventDate().trim();
            if (!eventDateStr.isEmpty()) {
                Date eventDate;
                try {
                    eventDate = dateFormat.parse(eventDateStr);
                } catch (ParseException pe) {
                    view.showError("El formato de la fecha del evento es incorrecto. Use dd/MM/yyyy. Ejemplo: 01/01/2025.");
                    return;
                }

                long diff = eventDate.getTime() - invoiceDate.getTime();
                long daysDiff = diff / (1000 * 60 * 60 * 24);
                if (daysDiff < 28) {
                    view.showError("La fecha de la factura debe ser al menos 4 semanas antes del evento.");
                    return;
                }
                if (eventDate.before(invoiceDate)) {
                    view.showError("La fecha del evento no puede ser anterior a la fecha de la factura.");
                    return;
                }
            }

            // Validación del ID del acuerdo
            if (view.getAgreementId().trim().isEmpty()) {
                view.showError("El ID del acuerdo es obligatorio.");
                return;
            }

            // Validación del IVA
            if (view.getInvoiceVat().trim().isEmpty()) {
                view.showError("El IVA es obligatorio.");
                return;
            }

            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setInvoiceNumber(view.getInvoiceNumber().trim());
            invoice.setInvoiceDate(invoiceDate);
            invoice.setRecipientName(view.getRecipientName().trim());
            invoice.setRecipientTaxId(view.getRecipientTaxId().trim());
            invoice.setRecipientAddress(view.getRecipientAddress().trim());
            invoice.setContactEmail(view.getContactEmail().trim());
            invoice.setAgreementId(Integer.parseInt(view.getAgreementId().trim()));
            invoice.setInvoiceVat(Double.parseDouble(view.getInvoiceVat().trim()));

            model.saveInvoice(invoice);
            view.showMessage("Factura registrada correctamente.");
            view.addInvoiceToTable(invoice.getInvoiceNumber(),
                    invoice.getRecipientName(),
                    invoice.getRecipientTaxId(),
                    invoice.getRecipientAddress(),
                    invoice.getContactEmail(),
                    view.getInvoiceDate(),
                    "");
            
            addInvoiceToTables(invoice);
            loadAvailableIds();
            
        } catch (Exception ex) {
            view.showError("Error registrando la factura: " + ex.getMessage());
        }
    }
    
    private void addInvoiceToTables(InvoiceDTO invoice) {
        // Tabla de facturas generadas
        view.addInvoiceToTable(
            invoice.getInvoiceNumber(),
            invoice.getRecipientName(),
            invoice.getRecipientTaxId(),
            invoice.getRecipientAddress(),
            invoice.getContactEmail(),
            dateFormat.format(invoice.getInvoiceDate()),
            ""
        );
        
        // Tabla de IDs disponibles (se actualiza automáticamente con loadAvailableIds)
    }

    private void sendInvoice() {
        try {
            if (view.getInvoiceNumber().trim().isEmpty()) {
                view.showError("El ID de la factura es obligatorio para enviar la factura.");
                return;
            }
            if (view.getContactEmail().trim().isEmpty()) {
                view.showError("El correo electrónico es obligatorio para enviar la factura.");
                return;
            }

            // Validar formato del correo electrónico
            if (!isValidEmail(view.getContactEmail().trim())) {
                view.showError("El formato del correo electrónico es incorrecto. Ejemplo: usuario@dominio.com.");
                return;
            }

            Date sentDate = new Date();

            String emailContent = "Estimado " + view.getRecipientName() + ",\n\n" +
                    "Adjunto encontrará los detalles de su factura:\n" +
                    "ID de Factura: " + view.getInvoiceNumber() + "\n" +
                    "Fecha de Factura: " + view.getInvoiceDate() + "\n\n" +
                    "Emisor: " + ISSUER_NAME + "\n" +
                    "NIF del Emisor: " + ISSUER_FISCAL_NUMBER + "\n" +
                    "Dirección del Emisor: " + ISSUER_ADDRESS + "\n\n" +
                    "Gracias.";

            view.showMessage("Factura enviada a " + view.getContactEmail() + " correctamente.\n\n" +
                    emailContent + "\n\nFecha de envío: " + dateFormat.format(sentDate));

            // Actualizar fecha de envío en la tabla
            for (int i = 0; i < view.getInvoicesTable().getRowCount(); i++) {
                if (view.getInvoicesTable().getValueAt(i, 0).equals(view.getInvoiceNumber().trim())) {
                    view.getInvoicesTable().setValueAt(dateFormat.format(sentDate), i, 6);
                    break;
                }
            }
        } catch (Exception ex) {
            view.showError("Error enviando la factura: " + ex.getMessage());
        }
    }

    private boolean isValidNIF(String nif) {
        // Validación básica del NIF (puedes ajustar según las reglas específicas)
        return Pattern.matches("([0-9]{8}[A-Z])|([A-HJ-NP-SUVW][0-9]{7}[A-Z0-9])|([XYZ][0-9]{7}[A-Z])", nif);
    }

    private boolean isValidEmail(String email) {
        // Validación básica del correo electrónico
        return Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email);
    }

    private boolean isValidInvoiceId(String invoiceId) {
        // Validación de que el ID de la factura tenga exactamente 9 dígitos
        return invoiceId.length() == 9 && Pattern.matches("\\d{9}", invoiceId);
    }
}