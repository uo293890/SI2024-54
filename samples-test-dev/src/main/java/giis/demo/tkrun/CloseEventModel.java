package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.ArrayList;
import java.util.List;

public class CloseEventModel extends Database {

    public List<Object[]> getEventsToClose() {
        return this.executeQueryArray("""
            SELECT event_id, event_name, event_inidate, event_enddate, event_status 
            FROM Event
            WHERE event_status IN ('Planned', 'Closed')
        """);
    }

    public boolean checkAgreementsPaidOrClosed(int eventId) {
        List<Object[]> result = this.executeQueryArray("""
            SELECT COUNT(*) FROM Agreement
            JOIN LevelOfSponsorship USING (level_id)
            WHERE event_id = ? AND agreement_status NOT IN ('Paid', 'Closed')
        """, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public boolean checkAllInvoicesExist(int eventId) {
        List<Object[]> result = this.executeQueryArray("""
            SELECT COUNT(*) FROM Agreement a
            JOIN LevelOfSponsorship l ON a.level_id = l.level_id
            WHERE l.event_id = ? AND NOT EXISTS (
                SELECT 1 FROM Invoice i WHERE i.agreement_id = a.agreement_id
            )
        """, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public boolean checkIncomesExpensesPaid(int eventId) {
        List<Object[]> result = this.executeQueryArray("""
            SELECT COUNT(*) FROM IncomesExpenses
            WHERE event_id = ? AND incexp_status != 'Paid'
        """, eventId);
        return ((Number) result.get(0)[0]).intValue() == 0;
    }

    public boolean checkNoPendingMovements(int eventId) {
        List<Object[]> result = this.executeQueryArray("""
            SELECT COUNT(*) FROM Movement m
            LEFT JOIN Invoice i ON m.invoice_id = i.invoice_id
            LEFT JOIN IncomesExpenses ie ON m.incexp_id = ie.incexp_id
            WHERE (i.agreement_id IN (
                SELECT agreement_id FROM Agreement a
                JOIN LevelOfSponsorship l ON a.level_id = l.level_id
                WHERE l.event_id = ?
            ) OR ie.event_id = ?) AND m.movement_amount IS NULL
        """, eventId, eventId);
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