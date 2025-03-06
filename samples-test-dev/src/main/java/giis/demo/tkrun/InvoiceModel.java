package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;

public class InvoiceModel {
    private Database db = new Database();

    public void saveInvoice(InvoiceDTO invoice) throws Exception {
        String sql = "INSERT INTO Invoice (invoice_number, invoice_date, recipient_name, tax_id, address) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {
            db.executeUpdate(sql,
                invoice.getInvoiceNumber(),
                new Date(invoice.getInvoiceDate().getTime()),
                invoice.getRecipientName(),
                invoice.getTaxId(),
                invoice.getAddress()
            );
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}