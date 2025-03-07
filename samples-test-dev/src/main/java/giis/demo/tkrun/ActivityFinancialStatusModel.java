package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class ActivityFinancialStatusModel {
    private Database db = new Database();

    // SQL Queries
    private static final String SQL_GET_ACTIVITIES = "SELECT edition_id AS editionId, edition_title AS editionTitle, edition_inidate AS editionStartDate, edition_status AS editionStatus FROM Edition;";
    private static final String SQL_GET_EDITIONS = "SELECT edition_id AS editionId, edition_title AS editionTitle, edition_inidate AS editionStartDate, edition_enddate AS editionEndDate, edition_status AS editionStatus FROM Edition WHERE event_id = ?;";
    private static final String SQL_GET_AGREEMENTS = "SELECT a.agreement_id, s.sponsor_name, a.agreement_date, a.agreement_amount, a.agreement_status " +
                                                     "FROM Agreement a JOIN Sponsor s ON a.sponsor_id = s.sponsor_id WHERE a.edition_id = ?;";
    private static final String SQL_GET_OTHERIES = "SELECT otherie_id, otherie_amount, otherie_description, otherie_status FROM Otherie WHERE edition_id = ?;";
    /**
     * Fetches all events from the database.
     */
    public List<ActivityFinancialStatusDTO> getAllActivities() {
        return db.executeQueryPojo(ActivityFinancialStatusDTO.class, SQL_GET_ACTIVITIES);
    }

    /**
     * Fetches editions for a specific event.
     */
    public List<ActivityFinancialStatusDTO> getEditionsForEvent(int eventId) {
        return db.executeQueryPojo(ActivityFinancialStatusDTO.class, SQL_GET_EDITIONS, eventId);
    }

    /**
     * Fetches agreements for a specific edition.
     */
    public List<ActivityFinancialStatusDTO> getAgreementsForEdition(int editionId) {
        return db.executeQueryPojo(ActivityFinancialStatusDTO.class, SQL_GET_AGREEMENTS, editionId);
    }

    /**
     * Fetches otheries (expenses) for a specific edition.
     */
    public List<ActivityFinancialStatusDTO> getOtheriesForEdition(int editionId) {
        return db.executeQueryPojo(ActivityFinancialStatusDTO.class, SQL_GET_OTHERIES, editionId);
    }

    /**
     * Calculates totals for income and expenses.
     */
    public double[] calculateTotals(List<ActivityFinancialStatusDTO> incomeList, List<ActivityFinancialStatusDTO> expenseList) {
        double totalIncome = incomeList.stream().mapToDouble(ActivityFinancialStatusDTO::getInvoiceAmount).sum();
        double totalExpenses = expenseList.stream().mapToDouble(ActivityFinancialStatusDTO::getOtherieAmount).sum();
        return new double[]{totalIncome, totalExpenses};
    }
}