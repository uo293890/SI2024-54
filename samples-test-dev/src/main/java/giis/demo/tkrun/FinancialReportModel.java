package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        
        String sql = "SELECT e.edition_title AS name, e.edition_status AS status, " +
                     "e.edition_inidate AS start_date, e.edition_enddate AS end_date, " +
                     "COALESCE((SELECT SUM(a.agreement_amount) FROM Agreement a WHERE a.edition_id = e.edition_id), 0) AS estimated_income, " +
                     "COALESCE((SELECT SUM(o.otherie_amount) FROM Otherie o WHERE o.edition_id = e.edition_id), 0) AS estimated_expenses, " +
                     "COALESCE((SELECT SUM(i.invoice_vat) FROM Invoice i JOIN Agreement a ON i.agreement_id = a.agreement_id WHERE a.edition_id = e.edition_id), 0) AS actual_income, " +
                     "COALESCE((SELECT SUM(m.movement_amount) FROM Movement m JOIN Otherie o ON m.otherie_id = o.otherie_id WHERE o.edition_id = e.edition_id), 0) AS actual_expenses " +
                     "FROM Edition e WHERE e.edition_inidate BETWEEN ? AND ? ";
        
        if (!isAll) {
            sql += "AND e.edition_status = ? ";
        }
        sql += "ORDER BY e.edition_inidate";
        
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            if (!isAll) {
                stmt.setString(3, status);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FinancialReportDTO report = new FinancialReportDTO(
                    rs.getString("name"),
                    rs.getString("status"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getDouble("estimated_income"),
                    rs.getDouble("estimated_expenses"),
                    rs.getDouble("actual_income"),
                    rs.getDouble("actual_expenses")
                );
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
}
