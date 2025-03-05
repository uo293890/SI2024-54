package giis.demo.tkrun;

import java.util.List;

public class ActivityFinancialStatusDTO {
    private String activityName;
    private String activityEdition;
    private String activityDate;
    private String activityStatus;
    private List<SponsorshipDTO> sponsorships;
    private List<IncomeDTO> incomes;
    private List<ExpenseDTO> expenses;

    // Getters and Setters
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityEdition() {
        return activityEdition;
    }

    public void setActivityEdition(String activityEdition) {
        this.activityEdition = activityEdition;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public List<SponsorshipDTO> getSponsorships() {
        return sponsorships;
    }

    public void setSponsorships(List<SponsorshipDTO> sponsorships) {
        this.sponsorships = sponsorships;
    }

    public List<IncomeDTO> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<IncomeDTO> incomes) {
        this.incomes = incomes;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }
}

class SponsorshipDTO {
    private String sponsorName;
    private String agreementDate;
    private double amount; // Added amount field
    private String status;

    // Getters and Setters
    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(String agreementDate) {
        this.agreementDate = agreementDate;
    }

    public double getAmount() { // Added getAmount method
        return amount;
    }

    public void setAmount(double amount) { // Added setAmount method
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class IncomeDTO {
    private String category;
    private double estimatedAmount;
    private double paidAmount;

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }
}

class ExpenseDTO {
    private String category;
    private double estimatedAmount;
    private double paidAmount;

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }
}