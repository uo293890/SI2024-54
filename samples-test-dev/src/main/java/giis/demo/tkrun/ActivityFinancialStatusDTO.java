package giis.demo.tkrun;

import java.time.LocalDate;
import java.util.List;

public class ActivityFinancialStatusDTO {
    private String eventTitle;
    private LocalDate eventDate;
    private String eventStatus;
    private List<SponsorshipDTO> sponsorships;
    private List<FinancialCategoryDTO> incomes;
    private List<FinancialCategoryDTO> expenses;

    // Getters and Setters
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }

    public List<SponsorshipDTO> getSponsorships() { return sponsorships; }
    public void setSponsorships(List<SponsorshipDTO> sponsorships) { this.sponsorships = sponsorships; }

    public List<FinancialCategoryDTO> getIncomes() { return incomes; }
    public void setIncomes(List<FinancialCategoryDTO> incomes) { this.incomes = incomes; }

    public List<FinancialCategoryDTO> getExpenses() { return expenses; }
    public void setExpenses(List<FinancialCategoryDTO> expenses) { this.expenses = expenses; }
}

class SponsorshipDTO {
    private String sponsorName;
    private LocalDate agreementDate;
    private Double amount;
    private String status;

    // Getters and Setters
    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }

    public LocalDate getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDate agreementDate) { this.agreementDate = agreementDate; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class FinancialCategoryDTO {
    private String category;
    private Double estimated;
    private Double paid;

    // Getters and Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getEstimated() { return estimated; }
    public void setEstimated(Double estimated) { this.estimated = estimated; }

    public Double getPaid() { return paid; }
    public void setPaid(Double paid) { this.paid = paid; }
}