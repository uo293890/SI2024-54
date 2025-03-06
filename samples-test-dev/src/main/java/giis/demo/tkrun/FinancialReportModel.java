package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class FinancialReportModel {
    private Database db = new Database();

    public FinancialReportDTO generateReport(String startDate, String endDate, String status) {
        String sql = "SELECT e.edition_title AS name, "
                   + "e.edition_inidate AS startDate, "
                   + "e.edition_enddate AS endDate, "
                   + "e.edition_status AS status, "
                   + "SUM(CASE WHEN o.otherie_status = 'Estimated' AND o.otherie_amount > 0 THEN o.otherie_amount ELSE 0 END) AS estimatedIncome, "
                   + "SUM(CASE WHEN m.movement_amount > 0 THEN m.movement_amount ELSE 0 END) AS actualIncome, "
                   + "SUM(CASE WHEN o.otherie_status = 'Estimated' AND o.otherie_amount < 0 THEN ABS(o.otherie_amount) ELSE 0 END) AS estimatedExpenses, "
                   + "SUM(CASE WHEN m.movement_amount < 0 THEN ABS(m.movement_amount) ELSE 0 END) AS actualExpenses "
                   + "FROM Edition e "
                   + "LEFT JOIN Otherie o ON e.edition_id = o.edition_id "
                   + "LEFT JOIN Movement m ON o.otherie_id = m.otherie_id "
                   + "WHERE e.edition_inidate >= ? AND e.edition_enddate <= ? AND e.edition_status = ? "
                   + "GROUP BY e.edition_id";

        List<ActivityDTO> activities = db.executeQueryPojo(ActivityDTO.class, sql, startDate, endDate, status);

        // Cálculo de totales
        double totalEstIncome = activities.stream().mapToDouble(ActivityDTO::getEstimatedIncome).sum();
        double totalActIncome = activities.stream().mapToDouble(ActivityDTO::getActualIncome).sum();
        double totalEstExpenses = activities.stream().mapToDouble(ActivityDTO::getEstimatedExpenses).sum();
        double totalActExpenses = activities.stream().mapToDouble(ActivityDTO::getActualExpenses).sum();

        return new FinancialReportDTO(activities, totalEstIncome, totalActIncome, totalEstExpenses, totalActExpenses);
    }
}