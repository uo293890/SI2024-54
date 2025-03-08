package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InvoiceModel {
    private Database db = new Database();

    public void saveInvoice(InvoiceDTO invoice) throws Exception {
        String sql = "INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat, recipient_name, recipient_tax_id, recipient_address, contact_email, sent_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            db.executeUpdate(sql,
                    invoice.getAgreementId(),
                    new Date(invoice.getInvoiceDate().getTime()),
                    invoice.getInvoiceNumber(),
                    invoice.getInvoiceVat(),
                    invoice.getRecipientName(),
                    invoice.getRecipientTaxId(),
                    invoice.getRecipientAddress(),
                    invoice.getContactEmail(),
                    invoice.getInvoiceDate() != null ? new Date(invoice.getInvoiceDate().getTime()) : null
            );
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public String generateInvoiceNumber() {
        long number = System.currentTimeMillis() % 1000000000;
        return String.format("%09d", number);
    }
    
    
    
    
    // Método para obtener todos los invoices de la base de datos
    public List<InvoiceDTO> getAllInvoices() throws Exception {
        List<InvoiceDTO> invoices = new ArrayList<>();
        // Asegúrate de incluir TODAS las columnas necesarias
        String sql = "SELECT invoice_number, recipient_name, recipient_tax_id, recipient_address, "
                   + "contact_email, invoice_date, agreement_id, invoice_vat FROM Invoice"; // ✅ Incluir todas las columnas
        
        List<List<Object>> results = db.executeQuery(sql);
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha de la BD
        
        for (List<Object> row : results) {
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setInvoiceNumber(row.get(0).toString());
            invoice.setRecipientName(row.get(1).toString());
            invoice.setRecipientTaxId(row.get(2).toString());
            invoice.setRecipientAddress(row.get(3).toString());
            invoice.setContactEmail(row.get(4).toString());
            
            // Parsear la fecha desde String (si es necesario)
            Object fecha = row.get(5);
            if (fecha instanceof String) { // Si la BD devuelve la fecha como String
                invoice.setInvoiceDate(dbDateFormat.parse((String) fecha));
            } else if (fecha instanceof java.sql.Date) { // Si ya es Date
                invoice.setInvoiceDate(new java.util.Date(((java.sql.Date) fecha).getTime()));
            }
            
            invoice.setAgreementId(((Number) row.get(6)).intValue());
            invoice.setInvoiceVat(((Number) row.get(7)).doubleValue());
            invoices.add(invoice);
        }
        return invoices;
    }
}
