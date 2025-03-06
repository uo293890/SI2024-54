package giis.demo.tkrun;

import java.util.List;

public class FinancialReportDTO {
    private List<ActivityDTO> activities;
    private double totalEstimatedIncome;
    private double totalActualIncome;
    private double totalEstimatedExpenses;
    private double totalActualExpenses;

    // Constructor
    public FinancialReportDTO(List<ActivityDTO> activities, double totalEstimatedIncome, 
                              double totalActualIncome, double totalEstimatedExpenses, 
                              double totalActualExpenses) {
        this.activities = activities;
        this.totalEstimatedIncome = totalEstimatedIncome;
        this.totalActualIncome = totalActualIncome;
        this.totalEstimatedExpenses = totalEstimatedExpenses;
        this.totalActualExpenses = totalActualExpenses;
    }

    // Getters
    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public double getTotalEstimatedIncome() {
        return totalEstimatedIncome;
    }

    public double getTotalActualIncome() {
        return totalActualIncome;
    }

    public double getTotalEstimatedExpenses() {
        return totalEstimatedExpenses;
    }

    public double getTotalActualExpenses() {
        return totalActualExpenses;
    }
}