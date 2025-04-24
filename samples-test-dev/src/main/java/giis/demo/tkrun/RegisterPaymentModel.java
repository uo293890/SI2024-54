package giis.demo.tkrun;

import java.util.List;
import giis.demo.util.Database;

public class RegisterPaymentModel extends Database {

    public List<Object[]> getAgreementsByStatus(String status) {
        // Keep selecting agreement_id first as it's needed internally
        String sql = "SELECT a.agreement_id, s.sponsor_name, c.spcontact_name, a.agreement_amount, a.agreement_status " +
                     "FROM Agreement a " +
                     "JOIN SpContact c ON a.spcontact_id = c.spcontact_id " +
                     "JOIN Sponsor s ON c.sponsor_id = s.sponsor_id " +
                     "WHERE a.agreement_status = ?";
        return this.executeQueryArray(sql, new Object[]{ status });
    }

    public List<Object[]> getPreviousPayments(int agreementId) {
        // Assuming movement_concept is nullable or allows empty string in DB schema
        String sql = "SELECT m.movement_date, m.movement_amount, m.movement_concept " +
                     "FROM Movement m " +
                     "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "WHERE i.agreement_id = ?";
        return this.executeQueryArray(sql, agreementId);
    }

    public double getAgreementExpectedAmount(int agreementId) {
        String sql = "SELECT agreement_amount FROM Agreement WHERE agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        // Ensure result is not null before casting
        return res.isEmpty() || res.get(0)[0] == null ? 0.0 : ((Number) res.get(0)[0]).doubleValue();
    }

    public double getAgreementTotalPaid(int agreementId) {
        String sql = "SELECT COALESCE(SUM(m.movement_amount), 0.0) FROM Movement m " + // Use COALESCE to get 0.0 if no payments
                     "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "WHERE i.agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        // SUM can return null if there are no rows or all values are null
        return res.isEmpty() || res.get(0)[0] == null ? 0.0 : ((Number) res.get(0)[0]).doubleValue();
    }

    public int getInvoiceIdByAgreement(int agreementId) {
        String sql = "SELECT invoice_id FROM Invoice WHERE agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        return res.isEmpty() ? -1 : ((Number) res.get(0)[0]).intValue();
    }

    // This method already handles String concept, which can be null or empty
    public void insertPayment(RegisterPaymentDTO dto) {
        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) VALUES (?, ?, ?, ?)";
        // Pass the concept string directly; Database class needs to handle inserting null/empty
        this.executeUpdate(sql, dto.getInvoiceId(), dto.getPaymentDate(), dto.getConcept(), dto.getAmount());
    }

    // New method to update agreement status generically
     public void updateAgreementStatus(int agreementId, String status) {
         String sql = "UPDATE Agreement SET agreement_status = ? WHERE agreement_id = ?";
         this.executeUpdate(sql, status, agreementId);
     }

    // This method is no longer used directly in the controller, kept for completeness
     public void updateAgreementStatusToPaid(int agreementId) {
         updateAgreementStatus(agreementId, "Paid");
     }
}