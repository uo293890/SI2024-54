package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportModel {
    private Database db = new Database();

    public List<ReportDTO> getFinancialReport(String startDate, String endDate, String status) {
        List<ReportDTO> reports = new ArrayList<>();
        
        if (startDate == null || startDate.trim().isEmpty() || endDate == null || endDate.trim().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            startDate = year + "-01-01";
            endDate = year + "-12-31";
        }
        
        String sql = "SELECT " +
                     "  e.edition_id AS id, " +
                     "  e.edition_title AS name, " +
                     "  e.edition_status AS status, " +
                     "  e.edition_inidate AS start_date, " +
                     "  e.edition_enddate AS end_date, " +
                     "  COALESCE(SUM(a.agreement_amount), 0) AS estimated_income, " +
                     "  COALESCE(SUM(o.otherie_amount), 0) AS estimated_expenses, " +
                     "  COALESCE(SUM(i.invoice_vat), 0) AS actual_income, " +
                     "  COALESCE(SUM(m.movement_amount), 0) AS actual_expenses " +
                     "FROM Edition e " +
                     "LEFT JOIN Agreement a ON e.edition_id = a.edition_id " +
                     "LEFT JOIN Invoice i ON a.agreement_id = i.agreement_id " +
                     "LEFT JOIN Otherie o ON e.edition_id = o.edition_id " +
                     "LEFT JOIN Movement m ON o.otherie_id = m.otherie_id " +
                     "WHERE e.edition_inidate BETWEEN ? AND ? " +
                     "AND (? = 'All' OR e.edition_status = ?) " +
                     "GROUP BY e.edition_id";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            stmt.setString(3, status);
            stmt.setString(4, status);
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReportDTO report = new ReportDTO();
                report.setId(rs.getInt("id"));
                report.setActivityName(rs.getString("name"));
                report.setStatus(rs.getString("status"));
                report.setStartDate(rs.getString("start_date") != null ? Date.valueOf(rs.getString("start_date")) : null);
                report.setEndDate(rs.getString("end_date") != null ? Date.valueOf(rs.getString("end_date")) : null);
                report.setEstimatedIncome(rs.getDouble("estimated_income"));
                report.setEstimatedExpenses(rs.getDouble("estimated_expenses"));
                report.setActualIncome(rs.getDouble("actual_income"));
                report.setActualExpenses(rs.getDouble("actual_expenses"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
}
