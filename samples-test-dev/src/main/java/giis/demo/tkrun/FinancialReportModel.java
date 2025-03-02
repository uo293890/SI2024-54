package giis.demo.tkrun;

import java.util.List;
import giis.demo.util.Database;

public class FinancialReportModel {
    private Database db = new Database();

    public FinancialReportDTO generateReport(String startDate, String endDate, String status) {
        String sql = "SELECT date, activity, status, income, expenses FROM financial_reports WHERE date BETWEEN ? AND ? AND status = ?";
        List<ActivityDTO> activities = db.executeQueryPojo(ActivityDTO.class, sql, startDate, endDate, status);
        double totalIncome = activities.stream().mapToDouble(ActivityDTO::getIncome).sum();
        double totalExpenses = activities.stream().mapToDouble(ActivityDTO::getExpenses).sum();
        return new FinancialReportDTO(activities, totalIncome, totalExpenses);
    }
}