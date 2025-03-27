package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.ArrayList;
import java.util.List;

public class CloseEventModel extends Database {

    // Join Event with Type to get the full event details
    public List<Object[]> getEventsToClose() {
        String sql = "SELECT e.event_id, t.type_name, e.event_name, e.event_inidate, e.event_enddate, e.event_location, e.event_status " +
                     "FROM Event e " +
                     "LEFT JOIN Type t ON e.type_id = t.type_id " +
                     "WHERE e.event_status IN ('Planned', 'Closed')";
        return this.executeQueryArray(sql);
    }

    public boolean checkAgreementsPaidOrClosed(int eventId) {
        String sql = "SELECT COUNT(*) FROM Agreement " +
                     "JOIN LevelOfSponsorship USING (level_id) " +
                     "WHERE event_id = ? AND agreement_status NOT IN ('Paid', 'Closed')";
        List<Object[]> result = this.executeQueryArray(sql, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public boolean checkAllInvoicesExist(int eventId) {
        String sql = "SELECT COUNT(*) FROM Agreement a " +
                     "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
                     "WHERE l.event_id = ? AND NOT EXISTS (" +
                     "    SELECT 1 FROM Invoice i WHERE i.agreement_id = a.agreement_id" +
                     ")";
        List<Object[]> result = this.executeQueryArray(sql, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public boolean checkIncomesExpensesPaid(int eventId) {
        String sql = "SELECT COUNT(*) FROM IncomesExpenses " +
                     "WHERE event_id = ? AND incexp_status != 'Paid'";
        List<Object[]> result = this.executeQueryArray(sql, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public boolean checkNoPendingMovements(int eventId) {
        String sql = "SELECT COUNT(*) FROM Movement m " +
                     "LEFT JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "LEFT JOIN IncomesExpenses ie ON m.incexp_id = ie.incexp_id " +
                     "WHERE (i.agreement_id IN (" +
                     "    SELECT agreement_id FROM Agreement a " +
                     "    JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
                     "    WHERE l.event_id = ?" +
                     ") OR ie.event_id = ?) AND m.movement_amount IS NULL";
        List<Object[]> result = this.executeQueryArray(sql, eventId, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public void closeEvent(int eventId) {
        this.executeUpdate("UPDATE Event SET event_status = 'Closed' WHERE event_id = ?", eventId);
    }

    public void reopenEvent(int eventId) {
        this.executeUpdate("UPDATE Event SET event_status = 'Planned' WHERE event_id = ?", eventId);
    }

    public List<String> getClosureIssues(int eventId) {
        List<String> issues = new ArrayList<>();

        if (!checkAgreementsPaidOrClosed(eventId)) {
            issues.add("Some agreements are not marked as 'Paid' or 'Closed'.");
        }
        if (!checkAllInvoicesExist(eventId)) {
            issues.add("Some agreements are missing invoices.");
        }
        if (!checkIncomesExpensesPaid(eventId)) {
            issues.add("Some incomes or expenses are not marked as 'Paid'.");
        }
        if (!checkNoPendingMovements(eventId)) {
            issues.add("There are pending or undefined financial movements.");
        }

        return issues;
    }

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
