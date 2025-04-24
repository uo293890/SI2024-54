package giis.demo.tkrun;

import java.util.List;
import giis.demo.util.Database;

public class RegisterSponsorModel {
    private Database db = new Database();
    
    public void registerSponsor(RegisterSponsorDTO dto) {
        String sqlSponsor = "INSERT INTO Sponsor (sponsor_name) VALUES (?)";
        String sqlContact = "INSERT INTO SpContact (sponsor_id, spcontact_name, " +
                "spcontact_number, spcontact_email) VALUES (?, ?, ?, ?)";
        
        // Insert sponsor
        db.executeUpdate(sqlSponsor, dto.getSponsorName());
        
        // Get the most recent sponsor_id
        String getLastIdSql = "SELECT MAX(sponsor_id) AS sponsorId FROM Sponsor";
        int sponsorId = db.executeQueryPojo(RegisterSponsorDTO.class, getLastIdSql).get(0).getSponsorId();

        // Insert contacts using the retrieved sponsorId
        for (RegisterSponsorDTO.SponsorContactDTO contact : dto.getContacts()) {
            db.executeUpdate(sqlContact,
                    sponsorId,
                    contact.getContactName(),
                    contact.getPhoneNumber(),
                    contact.getEmail());
        }
    }
}