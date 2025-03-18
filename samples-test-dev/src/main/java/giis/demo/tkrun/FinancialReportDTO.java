package giis.demo.tkrun;

import java.sql.Date;

public class FinancialReportDTO {
    private String activityName;
    private String status;
    private String startDate;
    private String endDate;
    private double estimatedIncome;
    private double estimatedExpenses;
    private double actualIncome;
    private double actualExpenses;

    public FinancialReportDTO(String activityName, String status, String startDate, String endDate, double estimatedIncome, double estimatedExpenses, double actualIncome, double actualExpenses) {
        this.activityName = activityName;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedIncome = estimatedIncome;
        this.estimatedExpenses = estimatedExpenses;
        this.actualIncome = actualIncome;
        this.actualExpenses = actualExpenses;
    }

    public String getActivityName() { return activityName; }
    public String getStatus() { return status; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public double getEstimatedIncome() { return estimatedIncome; }
    public double getEstimatedExpenses() { return estimatedExpenses; }
    public double getActualIncome() { return actualIncome; }
    public double getActualExpenses() { return actualExpenses; }
    public double getEstimatedBalance() { return estimatedIncome - estimatedExpenses; }
    public double getActualBalance() { return actualIncome - actualExpenses; }
}
