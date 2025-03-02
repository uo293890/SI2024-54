package giis.demo.tkrun;

import giis.demo.util.Database;

public class InvoiceModel {
    private Database db = new Database();

    public void saveInvoice(String activity, String invoiceDate, String invoiceId, String name, String taxId, String address) {
        String sql = "INSERT INTO invoices (activity, invoice_date, invoice_id, name, tax_id, address) VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, activity, invoiceDate, invoiceId, name, taxId, address);
    }

    public void sendInvoice(String invoiceId) {
        String sql = "UPDATE invoices SET sent_date = CURRENT_DATE WHERE invoice_id = ?";
        db.executeUpdate(sql, invoiceId);
    }
}