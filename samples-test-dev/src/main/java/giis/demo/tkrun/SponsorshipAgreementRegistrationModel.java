package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class SponsorshipAgreementRegistrationModel {
    private Database db = new Database();
    
    // SQL Queries
    private static final String SQL_GET_EDITIONS = "SELECT edition_id AS editionId, edition_title AS editionTitle FROM Edition;";
    private static final String SQL_GET_SPONSORS = "SELECT sponsor_id AS sponsorId, sponsor_name AS sponsorName FROM Sponsor;";
    private static final String SQL_GET_NEGOTIATORS = "SELECT DISTINCT negotiator FROM Agreement;";

    /**
     * Fetches all editions from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllEditions() {
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, SQL_GET_EDITIONS);
    }

    /**
     * Fetches all sponsors from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllSponsors() {
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, SQL_GET_SPONSORS);
    }

    /**
     * Fetches all negotiators from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllNegotiators() {
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, SQL_GET_NEGOTIATORS);
    }

    /**
     * Registers a new sponsorship agreement in the database.
     */
    public void registerSponsorshipAgreement(SponsorshipAgreementRegistrationDTO dto) {
        String sql = "INSERT INTO Agreement (edition_id, sponsor_id, negotiator, contact_worker, contact_number, contact_email, agreement_date, agreement_amount, agreement_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.executeUpdate(sql, 
            dto.getEditionId(), 
            dto.getSponsorId(), 
            dto.getNegotiator(), 
            dto.getContactWorker(), 
            dto.getContactNumber(), 
            dto.getContactEmail(), 
            dto.getAgreementDate(), 
            dto.getAgreementAmount(), 
            dto.getAgreementStatus());
    }
}