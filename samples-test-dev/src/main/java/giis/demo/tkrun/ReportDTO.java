package giis.demo.tkrun;

import java.util.Date;

public class ReportDTO {
    private String editionTitle; // Activity name
    private Date editionStartDate; // Start date of the edition
    private Date editionEndDate; // End date of the edition
    private String editionStatus; // Status of the edition
    private double totalIncome; // Total income from agreements
    private double totalExpenses; // Total expenses from otherie
    private double totalPaidIncome; // Total paid income from invoices
    private double totalPaidExpenses; // Total paid expenses from movements

    // Getters and Setters
    public String getEditionTitle() { return editionTitle; }
    public void setEditionTitle(String editionTitle) { this.editionTitle = editionTitle; }

    public Date getEditionStartDate() { return editionStartDate; }
    public void setEditionStartDate(Date editionStartDate) { this.editionStartDate = editionStartDate; }

    public Date getEditionEndDate() { return editionEndDate; }
    public void setEditionEndDate(Date editionEndDate) { this.editionEndDate = editionEndDate; }

    public String getEditionStatus() { return editionStatus; }
    public void setEditionStatus(String editionStatus) { this.editionStatus = editionStatus; }

    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }

    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }

    public double getTotalPaidIncome() { return totalPaidIncome; }
    public void setTotalPaidIncome(double totalPaidIncome) { this.totalPaidIncome = totalPaidIncome; }

    public double getTotalPaidExpenses() { return totalPaidExpenses; }
    public void setTotalPaidExpenses(double totalPaidExpenses) { this.totalPaidExpenses = totalPaidExpenses; }

    // Calculated fields
    public double getBalance() { return totalIncome - totalExpenses; }
    public double getEstimatedIncome() { return totalIncome - totalPaidIncome; }
    public double getEstimatedExpenses() { return totalExpenses - totalPaidExpenses; }
}