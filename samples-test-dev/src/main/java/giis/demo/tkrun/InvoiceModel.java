package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;

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
}