package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class RegisterSponsorshipAgreementModel {
    private Database db = new Database();
    
    // SQL Queries
    private static final String SQL_GET_EDITIONS = "SELECT edition_id AS editionId, edition_title AS editionTitle FROM Edition;";
    private static final String SQL_GET_SPONSORS = "SELECT sponsor_id AS sponsorId, sponsor_name AS sponsorName FROM Sponsor;";
    private static final String SQL_GET_GBMEMBERS = "SELECT gbmember_id AS gbMemberId, gbmember_name AS gbMemberName FROM GBMember;";
    
    /**
     * Fetches all editions from the database.
     */
    public List<RegisterSponsorshipAgreementDTO> getAllEditions() {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_EDITIONS);
    }

    /**
     * Fetches all sponsors from the database.
     */
    public List<RegisterSponsorshipAgreementDTO> getAllSponsors() {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_SPONSORS);
    }
    
    /**
     * Fetches all gbmembers from the database.
     */
    public List<RegisterSponsorshipAgreementDTO> getAllGBMembers() {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_GBMEMBERS);
    }

    /**
     * Registers a new sponsorship agreement in the database.
     */
    public void registerSponsorshipAgreement(RegisterSponsorshipAgreementDTO dto) {
        String sql = "INSERT INTO Agreement (edition_id, sponsor_id, gbmember_id, contact_name, contact_number, contact_email, agreement_date, agreement_amount, agreement_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.executeUpdate(sql, 
            dto.getEditionId(), 
            dto.getSponsorId(), 
            dto.getGbMemberId(), 
            dto.getContactName(), 
            dto.getContactNumber(), 
            dto.getContactEmail(), 
            dto.getAgreementDate(), 
            dto.getAgreementAmount(), 
            dto.getAgreementStatus());
    }
}