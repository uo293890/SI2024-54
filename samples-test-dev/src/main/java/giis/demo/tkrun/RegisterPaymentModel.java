package giis.demo.tkrun;

import java.util.List;
import giis.demo.util.Database;

public class RegisterPaymentModel extends Database {

    public List<Object[]> getPendingAgreements() {
        return this.executeQueryArray(
            "SELECT a.agreement_id, e.event_name, s.sponsor_name, a.agreement_date, a.agreement_amount, a.agreement_status " +
            "FROM Agreement a " +
            "JOIN SpContact sc ON a.spcontact_id = sc.spcontact_id " +
            "JOIN Sponsor s ON sc.sponsor_id = s.sponsor_id " +
            "JOIN LevelOfSponsorship los ON a.level_id = los.level_id " +
            "JOIN Event e ON los.event_id = e.event_id " +
            "WHERE a.agreement_status = 'Agreed'"
        );
    }

    public List<Object[]> getPreviousPayments(int agreementId) {
        return this.executeQueryArray(
            "SELECT m.movement_date, m.movement_amount, m.movement_concept " +
            "FROM Movement m " +
            "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
            "WHERE i.agreement_id = ?",
            agreementId
        );
    }

    public double getAgreementExpectedAmount(int agreementId) {
        List<Object[]> res = this.executeQueryArray(
            "SELECT agreement_amount FROM Agreement WHERE agreement_id = ?",
            agreementId
        );
        return res.isEmpty() ? 0.0 : ((Number)res.get(0)[0]).doubleValue();
    }

    public double getAgreementTotalPaid(int agreementId) {
        List<Object[]> res = this.executeQueryArray(
            "SELECT SUM(m.movement_amount) FROM Movement m " +
            "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
            "WHERE i.agreement_id = ?",
            agreementId
        );
        return res.get(0)[0] == null ? 0.0 : ((Number)res.get(0)[0]).doubleValue();
    }

    public int getInvoiceIdByAgreement(int agreementId) {
        List<Object[]> res = this.executeQueryArray(
            "SELECT invoice_id FROM Invoice WHERE agreement_id = ?",
            agreementId
        );
        return res.isEmpty() ? -1 : ((Number)res.get(0)[0]).intValue();
    }

    public void insertPayment(RegisterPaymentDTO dto) {
        this.executeUpdate(
            "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) VALUES (?, ?, ?, ?)",
            dto.getInvoiceId(), dto.getPaymentDate(), dto.getConcept(), dto.getAmount()
        );
    }

    public void updateAgreementStatusToPaid(int agreementId) {
        this.executeUpdate(
            "UPDATE Agreement SET agreement_status = 'Paid' WHERE agreement_id = ?",
            agreementId
        );
    }
} 
