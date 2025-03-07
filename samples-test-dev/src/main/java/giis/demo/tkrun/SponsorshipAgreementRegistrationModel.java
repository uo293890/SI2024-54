package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class SponsorshipAgreementRegistrationModel {
    private Database db = new Database();
    
    private static final String SQL_GET_EVENTS = "SELECT edition_id FROM Agreement;";
    private static final String SQL_GET_SPONSORS = "SELECT sponsor_id FROM Agreement;";
    private static final String SQL_GET_GBMEMBERS = "SELECT negotiator FROM Agreement;";

    /**
     * Fetches all events from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllEvents() {
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, SQL_GET_EVENTS);
    }

    /**
     * Fetches all sponsors from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllSponsors() {
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, SQL_GET_SPONSORS);
    }

    /**
     * Fetches all governing board members from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllNegotiators() {
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, SQL_GET_GBMEMBERS);
    }

    /**
     * Registers a new sponsorship agreement in the database.
     */
    public void registerSponsorshipAgreement(SponsorshipAgreementRegistrationDTO dto) {
        String sql = "INSERT INTO SponsorshipAgreement (event_id, sponsor_id, gb_member_id, agreement_date, agreed_amount, sponsorship_status, sponsor_contact_name, sponsor_contact_email, sponsor_contact_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.executeUpdate(sql, dto.getEventId(), dto.getSponsorId(), dto.getNegotiator(), dto.getAgreementDate(), dto.getAgreedAmount(), dto.getSponsorshipStatus(), dto.getSponsorContactName(), dto.getSponsorContactEmail(), dto.getSponsorContactPhone());
    }
}
