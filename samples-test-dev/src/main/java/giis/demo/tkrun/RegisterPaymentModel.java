package giis.demo.tkrun;

import java.util.List;
import giis.demo.util.Database;

public class RegisterPaymentModel extends Database {

    public List<Object[]> getAgreementsByStatus(String status) {
        String sql = "SELECT a.agreement_id, s.sponsor_name, c.spcontact_name, a.agreement_amount, a.agreement_status " +
                     "FROM Agreement a " +
                     "JOIN SpContact c ON a.spcontact_id = c.spcontact_id " +
                     "JOIN Sponsor s ON c.sponsor_id = s.sponsor_id " +
                     "WHERE a.agreement_status = ?";
        return this.executeQueryArray(sql, new Object[]{ status });
    }

    public List<Object[]> getPreviousPayments(int agreementId) {
        String sql = "SELECT m.movement_date, m.movement_amount, m.movement_concept " +
                     "FROM Movement m " +
                     "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "WHERE i.agreement_id = ?";
        return this.executeQueryArray(sql, agreementId);
    }

    public double getAgreementExpectedAmount(int agreementId) {
        String sql = "SELECT agreement_amount FROM Agreement WHERE agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        return res.isEmpty() ? 0.0 : ((Number) res.get(0)[0]).doubleValue();
    }

    public double getAgreementTotalPaid(int agreementId) {
        String sql = "SELECT SUM(m.movement_amount) FROM Movement m " +
                     "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "WHERE i.agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        return res.get(0)[0] == null ? 0.0 : ((Number) res.get(0)[0]).doubleValue();
    }

    public int getInvoiceIdByAgreement(int agreementId) {
        String sql = "SELECT invoice_id FROM Invoice WHERE agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        return res.isEmpty() ? -1 : ((Number) res.get(0)[0]).intValue();
    }

    public void insertPayment(RegisterPaymentDTO dto) {
        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) VALUES (?, ?, ?, ?)";
        this.executeUpdate(sql, dto.getInvoiceId(), dto.getPaymentDate(), dto.getConcept(), dto.getAmount());
    }

    public void updateAgreementStatusToPaid(int agreementId) {
        String sql = "UPDATE Agreement SET agreement_status = 'Paid' WHERE agreement_id = ?";
        this.executeUpdate(sql, agreementId);
    }
}
