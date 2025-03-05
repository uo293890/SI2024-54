package giis.demo.tkrun;

import java.util.ArrayList;
import java.util.List;

public class ActivityFinancialStatusModel {
    public ActivityFinancialStatusDTO getFinancialStatus(String activityName) {
        // Simulate data retrieval
        ActivityFinancialStatusDTO dto = new ActivityFinancialStatusDTO();
        dto.setActivityName(activityName);
        dto.setActivityEdition("2024");
        dto.setActivityDate("2024-05-01");
        dto.setActivityStatus("Active");

        // Sponsorships
        List<SponsorshipDTO> sponsorships = new ArrayList<>();
        SponsorshipDTO sponsorship1 = new SponsorshipDTO();
        sponsorship1.setSponsorName("Company A");
        sponsorship1.setAgreementDate("01/02/2024");
        sponsorship1.setAmount(5000.0); // Added amount
        sponsorship1.setStatus("Paid");
        sponsorships.add(sponsorship1);

        SponsorshipDTO sponsorship2 = new SponsorshipDTO();
        sponsorship2.setSponsorName("Company B");
        sponsorship2.setAgreementDate("05/03/2024");
        sponsorship2.setAmount(7000.0); // Added amount
        sponsorship2.setStatus("Estimated");
        sponsorships.add(sponsorship2);

        dto.setSponsorships(sponsorships);

        // Incomes
        List<IncomeDTO> incomes = new ArrayList<>();
        IncomeDTO income1 = new IncomeDTO();
        income1.setCategory("Sponsorships");
        income1.setEstimatedAmount(12000.0);
        income1.setPaidAmount(8000.0);
        incomes.add(income1);

        dto.setIncomes(incomes);

        // Expenses
        List<ExpenseDTO> expenses = new ArrayList<>();
        ExpenseDTO expense1 = new ExpenseDTO();
        expense1.setCategory("Venue");
        expense1.setEstimatedAmount(5000.0);
        expense1.setPaidAmount(3000.0);
        expenses.add(expense1);

        dto.setExpenses(expenses);

        return dto;
    }
}