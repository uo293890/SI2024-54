package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class SponsorshipAgreementRegistrationModel {
    private Database db = new Database();

    /**
     * Fetches all events from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllEvents() {
        String sql = "SELECT event_id, event_title FROM Event";
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, sql);
    }

    /**
     * Fetches all sponsors from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllSponsors() {
        String sql = "SELECT sponsor_id, sponsor_name FROM Sponsor";
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, sql);
    }

    /**
     * Fetches all governing board members from the database.
     */
    public List<SponsorshipAgreementRegistrationDTO> getAllGBMembers() {
        String sql = "SELECT gb_member_id, gb_member_name FROM GBMember";
        return db.executeQueryPojo(SponsorshipAgreementRegistrationDTO.class, sql);
    }

    /**
     * Registers a new sponsorship agreement in the database.
     */
    public void registerSponsorshipAgreement(SponsorshipAgreementRegistrationDTO dto) {
        String sql = "INSERT INTO SponsorshipAgreement (event_id, sponsor_id, gb_member_id, agreement_date, agreed_amount, sponsorship_status, sponsor_contact_name, sponsor_contact_email, sponsor_contact_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, dto.getEventId(), dto.getSponsorId(), dto.getGbMemberId(), dto.getAgreementDate(), dto.getAgreedAmount(), dto.getSponsorshipStatus(), dto.getSponsorContactName(), dto.getSponsorContactEmail(), dto.getSponsorContactPhone());
    }
}
