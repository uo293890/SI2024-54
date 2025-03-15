package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class InvoiceModel {
    private Database db = new Database();

    public void saveInvoice(String invoiceNumber, int agreementId, String issueDate, String invoiceDate, double invoiceVat, String recipientName, String recipientTaxId, String recipientAddress) throws Exception {
        String sql = "INSERT INTO Invoice (invoice_number, agreement_id, invoice_date, invoice_vat, recipient_name, recipient_tax_id, recipient_address, sent_date) " +
                     "VALUES ('" + invoiceNumber + "', " + agreementId + ", '" + invoiceDate + "', " + invoiceVat + ", '" + recipientName + "', '" + recipientTaxId + "', '" + recipientAddress + "', '" + issueDate + "')";
        try {
            db.executeUpdate(sql);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public void updateInvoiceSentDate(String invoiceNumber, String sentDate) throws Exception {
        String sql = "UPDATE Invoice SET sent_date = '" + sentDate + "' WHERE invoice_number = '" + invoiceNumber + "'";
        try {
            db.executeUpdate(sql);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public List<List<Object>> getAllEditions() throws Exception {
        String sql = "SELECT DISTINCT edition_title FROM Edition";
        try {
            return db.executeQuery(sql);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public List<List<Object>> getAgreementsForActivity(String activityName) throws Exception {
        String sql = "SELECT a.agreement_id, s.sponsor_name, a.contact_name, a.contact_email, a.agreement_amount, a.agreement_status, " +
                     "(CASE WHEN EXISTS (SELECT 1 FROM Invoice i WHERE i.agreement_id = a.agreement_id) THEN 'YES' ELSE 'NO' END) AS has_invoice " +
                     "FROM Agreement a " +
                     "JOIN Sponsor s ON a.sponsor_id = s.sponsor_id " +
                     "JOIN Edition e ON a.edition_id = e.edition_id " +
                     "WHERE e.edition_title = '" + activityName + "'";
        try {
            return db.executeQuery(sql);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }
}
