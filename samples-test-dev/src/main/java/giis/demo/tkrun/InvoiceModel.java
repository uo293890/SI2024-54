package giis.demo.tkrun;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import giis.demo.util.Database;

public class InvoiceModel {
    private Database db = new Database();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void saveInvoice(String invoiceNumber, int activityId, int agreementId, java.util.Date invoiceDate, double invoiceVat, String recipientName, String recipientTaxId, String recipientAddress, String contactEmail) throws Exception {
        String sql = "INSERT INTO Invoice (invoice_number, activity_id, agreement_id, invoice_date, invoice_vat, recipient_name, recipient_tax_id, recipient_address, contact_email, sent_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NULL)";
        try {
            db.executeUpdate(sql, invoiceNumber, activityId, agreementId, new Date(invoiceDate.getTime()), invoiceVat, recipientName, recipientTaxId, recipientAddress, contactEmail);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public void updateInvoiceSentDate(String invoiceNumber, java.util.Date sentDate) throws Exception {
        String sql = "UPDATE Invoice SET sent_date = ? WHERE invoice_number = ?";
        try {
            db.executeUpdate(sql, new Date(sentDate.getTime()), invoiceNumber);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public List<String[]> getAllInvoices() throws Exception {
        List<String[]> invoices = new ArrayList<>();
        String sql = "SELECT invoice_number, recipient_name, recipient_tax_id, recipient_address, contact_email, invoice_date, agreement_id, activity_id, invoice_vat FROM Invoice";
        List<List<Object>> results = db.executeQuery(sql);
        
        for (List<Object> row : results) {
            String[] invoice = new String[9];
            for (int i = 0; i < row.size(); i++) {
                invoice[i] = row.get(i) != null ? row.get(i).toString() : "";
            }
            invoices.add(invoice);
        }
        return invoices;
    }

    public String generateInvoiceNumber() {
        long number = System.currentTimeMillis() % 1000000000;
        return String.format("%09d", number);
    }
}
