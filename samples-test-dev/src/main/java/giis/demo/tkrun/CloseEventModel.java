package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CloseEventModel extends Database {

    /**
     * Retrieves events that are in 'Planned' or 'Closed' status.
     * Joins with the Type table to get the type name.
     * Includes event_id at index 0 for internal use.
     * @return List of Object arrays representing event data.
     */
    public List<Object[]> getEventsToClose() {
        String sql = "SELECT e.event_id, t.type_name, e.event_name, e.event_inidate, e.event_enddate, e.event_location, e.event_status " +
                     "FROM Event e " +
                     "LEFT JOIN Type t ON e.type_id = t.type_id " +
                     "WHERE e.event_status IN ('Planned', 'Closed')"; // Only show events that can be managed here
        return this.executeQueryArray(sql);
    }

    /**
     * Checks if all agreements for a given event are marked as 'Paid' or 'Closed'.
     * @param eventId The ID of the event.
     * @return true if all agreements are Paid or Closed, false otherwise.
     */
    public boolean checkAgreementsPaidOrClosed(int eventId) {
        String sql = "SELECT COUNT(*) FROM Agreement " +
                     "JOIN LevelOfSponsorship USING (level_id) " + // Link Agreement to Event via LevelOfSponsorship
                     "WHERE event_id = ? AND agreement_status NOT IN ('Paid', 'Closed')";
        List<Object[]> result = this.executeQueryArray(sql, eventId);
        // If count is 0, all agreements are Paid or Closed
        return ((Number) Objects.requireNonNull(result.get(0)[0], "Query result is null")).intValue() == 0;
    }

    /**
     * Checks if all agreements associated with an event have a corresponding invoice.
     * @param eventId The ID of the event.
     * @return true if all agreements have invoices, false otherwise.
     */
    public boolean checkAllInvoicesExist(int eventId) {
        String sql = "SELECT COUNT(*) FROM Agreement a " +
                     "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " + // Link Agreement to Event via LevelOfSponsorship
                     "WHERE l.event_id = ? AND NOT EXISTS (" + // Filter agreements for the event
                     "    SELECT 1 FROM Invoice i WHERE i.agreement_id = a.agreement_id" + // Check if an invoice exists for this agreement
                     ")";
        List<Object[]> result = this.executeQueryArray(sql, eventId);
        // If count is 0, all agreements have invoices
        return ((Number) Objects.requireNonNull(result.get(0)[0], "Query result is null")).intValue() == 0;
    }

    /**
     * Checks if all IncomesExpenses entries for an event are marked as 'Paid'.
     * Assumes 'Paid' is the final status for IncomesExpenses.
     * @param eventId The ID of the event.
     * @return true if all IncomesExpenses are Paid, false otherwise.
     */
    public boolean checkIncomesExpensesPaid(int eventId) {
        String sql = "SELECT COUNT(*) FROM IncomesExpenses " +
                     "WHERE event_id = ? AND incexp_status != 'Paid'";
        List<Object[]> result = this.executeQueryArray(sql, eventId);
        // If count is 0, all IncomesExpenses are Paid
        return ((Number) Objects.requireNonNull(result.get(0)[0], "Query result is null")).intValue() == 0;
    }

    /**
     * Checks if there are any pending financial movements linked to the event.
     * Based on the provided SQL, "pending movements" are those with a NULL movement_amount
     * linked to agreements or IncomesExpenses of this event.
     * @param eventId The ID of the event.
     * @return true if there are pending movements, false otherwise.
     */
    public boolean checkNoPendingMovements(int eventId) {
        String sql = "SELECT COUNT(*) FROM Movement m " +
                     "LEFT JOIN Invoice i ON m.invoice_id = i.invoice_id " + // Link movements to Agreements via Invoices
                     "LEFT JOIN IncomesExpenses ie ON m.incexp_id = ie.incexp_id " + // Link movements to IncomesExpenses
                     "WHERE (i.agreement_id IN (" + // Movements linked to Agreements of this event
                     "    SELECT agreement_id FROM Agreement a " +
                     "    JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
                     "    WHERE l.event_id = ?" +
                     ") OR ie.event_id = ?) AND m.movement_amount IS NULL"; // Movements linked to IncomesExpenses of this event, where amount is NULL
        List<Object[]> result = this.executeQueryArray(sql, eventId, eventId);
        // If count is 0, there are no movements with a NULL amount linked to this event
        return ((Number) Objects.requireNonNull(result.get(0)[0], "Query result is null")).intValue() == 0;
    }

    /**
     * Updates the status of an event to 'Closed'.
     * @param eventId The ID of the event to close.
     */
    public void closeEvent(int eventId) {
        this.executeUpdate("UPDATE Event SET event_status = 'Closed' WHERE event_id = ?", eventId);
    }

    /**
     * Updates the status of an event to 'Planned'.
     * @param eventId The ID of the event to reopen.
     */
    public void reopenEvent(int eventId) {
        this.executeUpdate("UPDATE Event SET event_status = 'Planned' WHERE event_id = ?", eventId);
    }

    /**
     * Gets the current status of an event by its ID directly from the database.
     * @param eventId The ID of the event.
     * @return The event status as a String.
     * @throws SQLException if a database error occurs or event not found.
     */
    public String getEventStatus(int eventId) throws SQLException {
        String sql = "SELECT event_status FROM Event WHERE event_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, eventId);
        if (res == null || res.isEmpty() || res.get(0) == null || res.get(0).length == 0 || res.get(0)[0] == null) {
             throw new SQLException("Event with ID " + eventId + " not found.");
        }
        Object statusObj = res.get(0)[0];
        if (statusObj instanceof String) {
             return (String) statusObj;
        } else {
            throw new SQLException("Unexpected data type for event status for ID " + eventId + ": " + (statusObj != null ? statusObj.getClass().getName() : "null"));
        }
    }

    // Helper methods used by the controller to get boolean results directly
    // These methods wrap the check methods to handle potential SQLExceptions
    public boolean allAgreementsClosedOrPaid(int eventId) {
        return checkAgreementsPaidOrClosed(eventId);
    }

    public boolean allInvoicesGenerated(int eventId) {
        return checkAllInvoicesExist(eventId);
    }

    public boolean allIncomesExpensesPaid(int eventId) {
        return checkIncomesExpensesPaid(eventId);
    }

    public boolean hasPendingMovements(int eventId) {
        return !checkNoPendingMovements(eventId);
    }
}