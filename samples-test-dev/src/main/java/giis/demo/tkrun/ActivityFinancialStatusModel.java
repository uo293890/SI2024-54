package giis.demo.tkrun;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityFinancialStatusModel {
    private Database database;

    public ActivityFinancialStatusModel() {
        this.database = new Database();
    }

    /**
     * Fetches the financial status of an activity.
     */
    public ActivityFinancialStatusDTO getFinancialStatus(int eventId) {
        ActivityFinancialStatusDTO dto = new ActivityFinancialStatusDTO();

        try (Connection conn = database.getConnection()) {
            // Fetch event details
            String eventSql = "SELECT title, event_date, status FROM Event WHERE event_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(eventSql)) {
                pstmt.setInt(1, eventId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    dto.setEventTitle(rs.getString("title"));
                    dto.setEventDate(rs.getDate("event_date").toLocalDate());
                    dto.setEventStatus(rs.getString("status"));
                }
            }

            // Fetch sponsorships
            String sponsorshipSql = "SELECT s.sponsor_name, sp.agreement_date, sp.agreed_amount, sp.sponsorship_status " +
                                    "FROM Sponsorship sp " +
                                    "JOIN Sponsor s ON sp.sponsor_id = s.sponsor_id " +
                                    "WHERE sp.event_id = ?";
            List<SponsorshipDTO> sponsorships = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(sponsorshipSql)) {
                pstmt.setInt(1, eventId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    SponsorshipDTO sponsorship = new SponsorshipDTO();
                    sponsorship.setSponsorName(rs.getString("sponsor_name"));
                    sponsorship.setAgreementDate(rs.getDate("agreement_date").toLocalDate());
                    sponsorship.setAmount(rs.getDouble("agreed_amount"));
                    sponsorship.setStatus(rs.getString("sponsorship_status"));
                    sponsorships.add(sponsorship);
                }
            }
            dto.setSponsorships(sponsorships);

            // Fetch financial data
            String financeSql = "SELECT transaction_type, status, SUM(amount) as total " +
                               "FROM Transactions " +
                               "WHERE event_id = ? " +
                               "GROUP BY transaction_type, status";
            
            List<FinancialCategoryDTO> incomes = new ArrayList<>();
            List<FinancialCategoryDTO> expenses = new ArrayList<>();
            
            try (PreparedStatement pstmt = conn.prepareStatement(financeSql)) {
                pstmt.setInt(1, eventId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("transaction_type");
                    String status = rs.getString("status");
                    Double amount = rs.getDouble("total");
                    
                    FinancialCategoryDTO category = new FinancialCategoryDTO();
                    category.setCategory(type.equals("Income") ? "Income" : "Expense");
                    
                    if (status.equals("Estimated")) {
                        category.setEstimated(amount);
                    } else if (status.equals("Paid")) {
                        category.setPaid(amount);
                    }
                    
                    if (type.equals("Income")) {
                        incomes.add(category);
                    } else {
                        expenses.add(category);
                    }
                }
            }
            
            dto.setIncomes(incomes);
            dto.setExpenses(expenses);

        } catch (SQLException e) {
            throw new ApplicationException("Error retrieving financial status");
        }
        
        return dto;
    }
}