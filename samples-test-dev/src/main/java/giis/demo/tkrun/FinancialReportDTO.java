package giis.demo.tkrun;

import java.util.Objects; // Import Objects for potential future use if needed

/**
 * DTO (Data Transfer Object) que representa la información financiera de un evento.
 * Incluye datos como nombre, estado, fechas, ingresos y gastos tanto estimados como reales.
 * Los campos de ingreso y gasto representan los totales combinados (Sponsorship + Other Income, Total Expenses).
 */
public class FinancialReportDTO {
    private String activityName;
    private String status;
    private String startDate;
    private String endDate;
    private double estimatedIncome; // Combined Estimated Sponsorship + Estimated Other Income
    private double estimatedExpenses; // Total Estimated Expenses
    private double actualIncome; // Combined Actual Sponsorship + Actual Other Income
    private double actualExpenses; // Total Actual Expenses

    public FinancialReportDTO(String activityName, String status, String startDate, String endDate,
                              double estimatedIncome, double estimatedExpenses,
                              double actualIncome, double actualExpenses) {
        this.activityName = activityName;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedIncome = estimatedIncome;
        this.estimatedExpenses = estimatedExpenses;
        this.actualIncome = actualIncome;
        this.actualExpenses = actualExpenses;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getStatus() {
        return status;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    // Getters return the calculated total values
    public double getEstimatedIncome() {
        return estimatedIncome;
    }

    public double getEstimatedExpenses() {
        return estimatedExpenses;
    }

    public double getActualIncome() {
        return actualIncome;
    }

    public double getActualExpenses() {
        return actualExpenses;
    }

    // Balances calculated as Income - Expenses (using the total values)
    public double getEstimatedBalance() {
        return estimatedIncome - estimatedExpenses;
    }

    public double getActualBalance() {
        return actualIncome - actualExpenses;
    }
}