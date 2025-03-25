package giis.demo.tkrun;

import giis.demo.util.Database;

import java.util.Date;
import java.util.List;


/**
 * Model class responsible for database access and business logic related to invoices.
 * Interacts with the Invoice, Movement, and Event tables.
 */
public class InvoiceModel {
    private Database db = new Database();

    /**
     * Stores a new invoice in the database without a send date.
     *
     * @param invoiceNumber Unique identifier for the invoice.
     * @param agreementId   ID of the agreement being invoiced.
     * @param invoiceVat    VAT applied to the invoice.
     * @throws Exception if the database update fails.
     */
    public void saveInvoice(String invoiceNumber, int agreementId, double invoiceVat) throws Exception {
        String sql = "INSERT INTO Invoice (agreement_id, invoice_number, invoice_vat) " +
                     "VALUES ('" + agreementId + "', '" + invoiceNumber + "', '" + invoiceVat + "')";
        db.executeUpdate(sql);
    }


    /**
     * Sets the date when an invoice was actually sent to the sponsor.
     *
     * @param invoiceNumber Unique identifier for the invoice.
     * @param invoiceDate   Date the invoice was sent, in YYYY-MM-DD format.
     * @throws Exception if the database update fails.
     */
    public void setInvoiceDate(String invoiceNumber, String invoiceDate) throws Exception {
        String sql = "UPDATE Invoice SET invoice_date = '" + invoiceDate + "' WHERE invoice_number = '" + invoiceNumber + "'";
        db.executeUpdate(sql);
    }

    /**
     * Records a new "Invoice Sent" financial movement associated with the invoice.
     *
     * @param invoiceNumber The invoice's identifier.
     * @param sentDate      The date the invoice was sent.
     * @throws Exception if the database insert fails.
     */
    public void recordMovementForInvoice(String invoiceNumber, String sentDate) throws Exception {
        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) " +
                     "SELECT invoice_id, '" + sentDate + "', 'Invoice Sent', 0 FROM Invoice WHERE invoice_number = '" + invoiceNumber + "'";
        db.executeUpdate(sql);
    }

    /**
     * Retrieves a list of all event names from the Event table.
     *
     * @return A list of Object arrays, each containing one event name.
     * @throws Exception if the query fails.
     */
    public List<Object[]> getAllEvents() throws Exception {
        String sql = "SELECT DISTINCT event_name FROM Event";
        return db.executeQueryArray(sql);
    }

    /**
     * Retrieves all agreements for a given activity, including whether an invoice has been generated.
     *
     * @param activityName The name of the activity.
     * @return A list of agreements and related sponsor/contact data.
     * @throws Exception if the query fails.
     */
    public List<Object[]> getAgreementsForActivity(String activityName) throws Exception {
        String sql = "SELECT a.agreement_id, s.sponsor_name, c.spcontact_name, c.spcontact_email, " +
                     "a.agreement_amount, a.agreement_status, " +
                     "(CASE WHEN EXISTS (SELECT 1 FROM Invoice i WHERE i.agreement_id = a.agreement_id) THEN 'YES' ELSE 'NO' END) AS has_invoice " +
                     "FROM Agreement a " +
                     "JOIN SpContact c ON a.spcontact_id = c.spcontact_id " +
                     "JOIN Sponsor s ON c.sponsor_id = s.sponsor_id " +
                     "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
                     "JOIN Event e ON l.event_id = e.event_id " +
                     "WHERE e.event_name = ?";
        
        return db.executeQueryArray(sql, new Object[]{ activityName });
    }



    /**
     * Retrieves the start date of the given event.
     *
     * @param activityName The name of the event.
     * @return A Date object representing the event's start date.
     * @throws RuntimeException if the event is not found.
     */
    public Date getEventStartDate(String activityName) {
        String sql = "SELECT event_inidate FROM Event WHERE event_name = '" + activityName + "'";
        List<Object[]> result = db.executeQueryArray(sql);
        if (!result.isEmpty()) {
            return java.sql.Date.valueOf(result.get(0)[0].toString()); // esto debe funcionar bien
        } else {
            throw new RuntimeException("Event not found: " + activityName);
        }
        
    }
    
    

}
