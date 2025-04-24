package giis.demo.tkrun;

import giis.demo.util.Database; // Assuming this utility class is available

import java.sql.SQLException;
import java.text.SimpleDateFormat; // Still used for old Date type
import java.time.LocalDate; // Using modern date/time API
import java.time.ZoneId; // For converting Date to LocalDate
import java.util.Date;
import java.util.List;

/**
 * Model class responsible for database access and business logic related to invoices.
 * Interacts with the Invoice, Movement, and Event tables.
 */
public class InvoiceModel {
    // Database connection handled by the utility class instance
    private Database db = new Database();

    // Constructor might take Database instance if it's managed externally
    // public InvoiceModel(Database db) { this.db = db; }

    /**
     * Saves the initial invoice data to the database.
     * Note: This method saves the invoice_number, agreement_id, and invoice_vat.
     * The invoice_date is set later when the invoice is sent using setInvoiceDate.
     * @param invoiceNumber Unique identifier for the invoice.
     * @param agreementId   ID of the associated agreement.
     * @param invoiceVat    VAT percentage.
     * @throws SQLException if a database access error occurs.
     */
    public void saveInvoice(String invoiceNumber, int agreementId, double invoiceVat) throws SQLException {
        // Using prepared statement syntax for safety
        String sql = "INSERT INTO Invoice (agreement_id, invoice_number, invoice_vat) " +
                     "VALUES (?, ?, ?)";
        // db.executeUpdate should handle closing resources internally
        db.executeUpdate(sql, agreementId, invoiceNumber, invoiceVat);
    }

    /**
     * Sets or updates the invoice_date for an existing invoice.
     * @param invoiceNumber The number of the invoice to update.
     * @param invoiceDate   The date to set in 'yyyy-MM-dd' format.
     * @throws SQLException if a database access error occurs.
     */
    public void setInvoiceDate(String invoiceNumber, String invoiceDate) throws SQLException {
        // Sets the invoice_date for a given invoice number
        String sql = "UPDATE Invoice SET invoice_date = ? WHERE invoice_number = ?"; // Using prepared statement syntax
        db.executeUpdate(sql, invoiceDate, invoiceNumber); // Pass values as parameters
    }

    /**
     * Records a movement entry for a given invoice, typically when it's sent.
     * @param invoiceNumber The number of the invoice the movement is for.
     * @param sentDate      The date the invoice was sent, used as the movement date ('yyyy-MM-dd').
     * @throws SQLException if a database access error occurs or invoice not found.
     */
    public void recordMovementForInvoice(String invoiceNumber, String sentDate) throws SQLException {
        // First, get the invoice_id from the Invoice table based on the invoice_number
        String getInvoiceIdSql = "SELECT invoice_id FROM Invoice WHERE invoice_number = ?";
        List<Object[]> result = db.executeQueryArray(getInvoiceIdSql, invoiceNumber);

        if (result.isEmpty() || result.get(0)[0] == null) {
            // If invoice_id is not found or null, throw a specific error
            throw new SQLException("Invoice with number " + invoiceNumber + " not found for movement recording.");
        }

        // Assuming invoice_id is an integer (or can be cast to one)
        int invoiceId = (Integer) result.get(0)[0];

        // Then insert the movement record
        String insertMovementSql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) " +
                                  "VALUES (?, ?, 'Invoice Sent', 0)"; // movement_amount is 0 for sending invoice
        db.executeUpdate(insertMovementSql, invoiceId, sentDate); // Pass values as parameters
    }

    /**
     * Retrieves a list of all distinct event names.
     * @return List of Object[] where each array contains one String (event name).
     * @throws SQLException if a database access error occurs.
     */
    public List<Object[]> getAllEvents() throws SQLException {
        String sql = "SELECT DISTINCT event_name FROM Event";
        return db.executeQueryArray(sql);
    }

    /**
     * Retrieves detailed agreement data for a specific activity (event).
     * Includes the agreement ID which is needed by the view/controller.
     * The columns returned match the order expected by InvoiceView's table model.
     * @param activityName The name of the activity (event).
     * @return List of Object[] arrays containing agreement details.
     * @throws SQLException if a database access error occurs.
     */
    public List<Object[]> getAgreementsForActivity(String activityName) throws SQLException {
        String sql = "SELECT "+
                     "a.agreement_id, "+ // Added Agreement ID
                     "s.sponsor_name, c.spcontact_name, c.spcontact_email, "+
                     "a.agreement_amount, e.event_inidate, e.event_location, "+
                     "l.level_name, a.agreement_status "+
                     "FROM Agreement a "+
                     "JOIN SpContact c ON a.spcontact_id = c.spcontact_id "+
                     "JOIN Sponsor s ON c.sponsor_id = s.sponsor_id "+
                     "JOIN LevelOfSponsorship l ON a.level_id = l.level_id "+
                     "JOIN Event e ON l.event_id = e.event_id "+
                     "WHERE e.event_name = ?";
        return db.executeQueryArray(sql, activityName); // Pass activityName as parameter
    }

    /**
     * Gets the start date of a specific event.
     * @param activityName The name of the event.
     * @return The event start date as a java.util.Date.
     * @throws SQLException if a database access error occurs or event not found/date null.
     */
    public Date getEventStartDate(String activityName) throws SQLException {
        String sql = "SELECT event_inidate FROM Event WHERE event_name = ?";
        List<Object[]> result = db.executeQueryArray(sql, activityName);
        if (!result.isEmpty() && result.get(0) != null && result.get(0)[0] != null) {
            // Assuming event_inidate is stored as TEXT or DATE in 'yyyy-MM-dd' format
            // java.sql.Date is a subclass of java.util.Date
            return java.sql.Date.valueOf(result.get(0)[0].toString());
        } else {
            // Throw an exception if event is not found or date is null
            throw new SQLException("Event not found or start date is null for activity: " + activityName);
        }
    }
}