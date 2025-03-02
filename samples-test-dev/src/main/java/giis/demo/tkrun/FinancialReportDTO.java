package giis.demo.tkrun;

import java.util.List;

public class FinancialReportDTO {
    private List<ActivityDTO> activities;
    private double totalIncome;
    private double totalExpenses;

    public FinancialReportDTO(List<ActivityDTO> activities, double totalIncome, double totalExpenses) {
        this.activities = activities;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }
}