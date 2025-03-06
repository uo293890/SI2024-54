package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.util.List;

public class ReportModel {
    private Database db = new Database();

    public List<ReportDTO> getFinancialReport(Date startDate, Date endDate, String status) {
        String sql = "SELECT e.edition_title AS editionTitle, "
                   + "e.edition_inidate AS editionStartDate, "
                   + "e.edition_enddate AS editionEndDate, "
                   + "e.edition_status AS editionStatus, "
                   + "COALESCE(SUM(a.agreement_amount), 0) AS totalIncome, "
                   + "COALESCE(SUM(o.otherie_amount), 0) AS totalExpenses, "
                   + "COALESCE(SUM(CASE WHEN a.agreement_status = 'Paid' THEN a.agreement_amount ELSE 0 END), 0) AS totalPaidIncome, "
                   + "COALESCE(SUM(CASE WHEN o.otherie_status = 'Paid' THEN o.otherie_amount ELSE 0 END), 0) AS totalPaidExpenses "
                   + "FROM Edition e "
                   + "LEFT JOIN Agreement a ON e.edition_id = a.edition_id "
                   + "LEFT JOIN Otherie o ON e.edition_id = o.edition_id "
                   + "WHERE e.edition_inidate BETWEEN ? AND ? "
                   + "AND (? = 'All' OR e.edition_status = ?) "
                   + "GROUP BY e.edition_id";

        return db.executeQueryPojo(ReportDTO.class, sql, startDate, endDate, status, status);
    }
}