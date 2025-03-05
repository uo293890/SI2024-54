package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class FinancialReportModel {
    private Database db = new Database();

    public FinancialReportDTO generateReport(String startDate, String endDate, String status) {
        String sql = "SELECT activity, start_date, end_date, status, total_income, total_expenses, balance "
                   + "FROM financial_reports "
                   + "WHERE start_date >= ? AND end_date <= ? AND status = ?";
        
        List<ActivityDTO> activities = db.executeQueryPojo(ActivityDTO.class, sql, startDate, endDate, status);
        
        double totalIncome = activities.stream().mapToDouble(ActivityDTO::getIncome).sum();
        double totalExpenses = activities.stream().mapToDouble(ActivityDTO::getExpenses).sum();
        
        return new FinancialReportDTO(activities, totalIncome, totalExpenses);
    }
}