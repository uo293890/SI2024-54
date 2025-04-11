package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class RegisterSponsorshipAgreementModel {
    private Database db = new Database();
    
    // SQL Queries
    private static final String SQL_GET_EVENTS = 
        "SELECT event_id AS eventId, event_name AS eventName FROM Event WHERE event_status = 'Planned'";
    
    private static final String SQL_GET_SPONSORS = 
    	    "SELECT sponsor_id AS sponsorId, sponsor_name AS sponsorName FROM Sponsor";
    
    private static final String SQL_GET_GBMEMBERS = 
        "SELECT gbmember_id AS gbMemberId, gbmember_name AS gbMemberName, " +
        "gbmember_position AS gbMemberPosition FROM GbMember;";
    
    private static final String SQL_GET_SPONSOR_CONTACTS = 
        "SELECT spcontact_id AS spContactId, spcontact_name AS spContactName, " +
        "spcontact_number AS spContactNumber, spcontact_email AS spContactEmail " +
        "FROM SpContact WHERE sponsor_id = ?;";
    
    private static final String SQL_GET_SPONSORSHIP_LEVELS = 
        "SELECT level_id AS levelId, level_name AS levelName, " +
        "level_minamount AS levelMinAmount " +
        "FROM LevelOfSponsorship WHERE event_id = ?;";

    /**
     * Fetches all planned events from the database.
     */
    public List<RegisterSponsorshipAgreementDTO> getAllEvents() {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_EVENTS);
    }

    /**
     * Fetches all sponsors from the database.
     */
    public List<RegisterSponsorshipAgreementDTO> getAllSponsors() {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_SPONSORS);
    }
    
    /**
     * Fetches all GB members from the database.
     */
    public List<RegisterSponsorshipAgreementDTO> getAllGBMembers() {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_GBMEMBERS);
    }

    /**
     * Fetches all contacts for a specific sponsor.
     */
    public List<RegisterSponsorshipAgreementDTO> getSponsorContacts(int sponsorId) {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_SPONSOR_CONTACTS, sponsorId);
    }

    /**
     * Fetches all sponsorship levels for a specific event.
     */
    public List<RegisterSponsorshipAgreementDTO> getSponsorshipLevels(int eventId) {
        return db.executeQueryPojo(RegisterSponsorshipAgreementDTO.class, SQL_GET_SPONSORSHIP_LEVELS, eventId);
    }

    /**
     * Registers a new sponsorship agreement in the database.
     */
    public void registerSponsorshipAgreement(RegisterSponsorshipAgreementDTO dto) {
        String sql = "INSERT INTO Agreement (" +
                     "level_id, gbmember_id, spcontact_id, " +
                     "agreement_date, agreement_amount, agreement_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?);";
        
        db.executeUpdate(sql, 
            dto.getLevelId(), 
            dto.getGbMemberId(), 
            dto.getSpContactId(),
            dto.getAgreementDate(), 
            dto.getAgreementAmount(), 
            dto.getAgreementStatus());
    }
}