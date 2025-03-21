package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class FinancialReportModel {
    private Database db = new Database();

    public List<FinancialReportDTO> getFinancialReport(String startDate, String endDate, String status) {
        List<FinancialReportDTO> reports = new ArrayList<>();

        if (startDate == null || startDate.trim().isEmpty() || endDate == null || endDate.trim().isEmpty()) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            startDate = currentYear + "-01-01";
            endDate = currentYear + "-12-31";
        }

        boolean isAll = "All".equalsIgnoreCase(status);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.event_name AS name, e.event_status AS status, ")
           .append("e.event_inidate AS start_date, e.event_enddate AS end_date, ")
           .append("COALESCE((SELECT SUM(a.agreement_amount) ")
           .append("           FROM Agreement a ")
           .append("           JOIN LevelOfSponsorship l ON a.level_id = l.level_id ")
           .append("          WHERE l.event_id = e.event_id), 0) AS estimated_income, ")
           .append("COALESCE((SELECT SUM(ie.incexp_amount) ")
           .append("           FROM IncomesExpenses ie ")
           .append("          WHERE ie.event_id = e.event_id), 0) AS estimated_expenses, ")
           .append("COALESCE((SELECT SUM(i.invoice_vat) ")
           .append("           FROM Invoice i ")
           .append("           JOIN Agreement a ON i.agreement_id = a.agreement_id ")
           .append("           JOIN LevelOfSponsorship l ON a.level_id = l.level_id ")
           .append("          WHERE l.event_id = e.event_id), 0) AS actual_income, ")
           .append("COALESCE((SELECT SUM(m.movement_amount) ")
           .append("           FROM Movement m ")
           .append("           JOIN IncomesExpenses ie ON m.incexp_id = ie.incexp_id ")
           .append("          WHERE ie.event_id = e.event_id), 0) AS actual_expenses ")
           .append("FROM Event e ")
           .append("WHERE e.event_inidate BETWEEN ? AND ? ");

        List<Object> params = new ArrayList<>();
        params.add(startDate);
        params.add(endDate);

        if (!isAll) {
            sql.append("AND e.event_status = ? ");
            params.add(status);
        }

        sql.append("ORDER BY e.event_inidate");

        List<Map<String, Object>> rows = db.executeQueryMap(sql.toString(), params.toArray());

        for (Map<String, Object> row : rows) {
            FinancialReportDTO report = new FinancialReportDTO(
                String.valueOf(row.get("name")),
                String.valueOf(row.get("status")),
                String.valueOf(row.get("start_date")),
                String.valueOf(row.get("end_date")),
                toDouble(row.get("estimated_income")),
                toDouble(row.get("estimated_expenses")),
                toDouble(row.get("actual_income")),
                toDouble(row.get("actual_expenses"))
            );
            reports.add(report);
        }

        return reports;
    }

    private double toDouble(Object obj) {
        return obj == null ? 0.0 : Double.parseDouble(obj.toString());
    }
}
