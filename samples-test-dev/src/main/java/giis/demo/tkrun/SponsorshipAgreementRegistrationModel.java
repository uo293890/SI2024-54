package giis.demo.tkrun;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SponsorshipAgreementRegistrationModel {
    private Database database;

    public SponsorshipAgreementRegistrationModel() {
        this.database = new Database();
    }

    /**
     * Registers a new sponsorship agreement in the database.
     */
    public void registerSponsorship(SponsorshipAgreementRegistrationDTO dto) {
        String sql = "INSERT INTO Sponsorship (event_id, sponsor_id, gb_member_id, agreement_date, " +
                     "sponsorship_level, agreed_amount, sponsorship_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, dto.getEventId());
            pstmt.setInt(2, dto.getSponsorId());
            pstmt.setInt(3, dto.getGbMemberId());
            pstmt.setDate(4, java.sql.Date.valueOf(dto.getAgreementDate()));
            pstmt.setString(5, dto.getSponsorshipLevel());
            pstmt.setDouble(6, dto.getAgreedAmount());
            pstmt.setString(7, dto.getSponsorshipStatus());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationException("Error saving sponsorship agreement");
        }
    }

    /**
     * Fetches all events from the database.
     */
    public List<EventDTO> getAllEvents() {
        List<EventDTO> events = new ArrayList<>();
        String sql = "SELECT event_id, title FROM Event";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("Executing query: " + sql); // Debugging
            
            while (rs.next()) {
                EventDTO event = new EventDTO();
                event.setEventId(rs.getInt("event_id"));
                event.setTitle(rs.getString("title"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage()); // Debugging
            throw new ApplicationException("Error fetching events");
        }

        return events;
    }

    /**
     * Fetches all sponsors from the database.
     */
    public List<SponsorDTO> getAllSponsors() {
        List<SponsorDTO> sponsors = new ArrayList<>();
        String sql = "SELECT sponsor_id, sponsor_name FROM Sponsor";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                SponsorDTO sponsor = new SponsorDTO();
                sponsor.setSponsorId(rs.getInt("sponsor_id"));
                sponsor.setSponsorName(rs.getString("sponsor_name"));
                sponsors.add(sponsor);
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error fetching sponsors");
        }

        return sponsors;
    }

    /**
     * Fetches all governing board members from the database.
     */
    public List<GBMemberDTO> getAllGBMembers() {
        List<GBMemberDTO> members = new ArrayList<>();
        String sql = "SELECT gb_member_id, name FROM GBMember";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                GBMemberDTO member = new GBMemberDTO();
                member.setGbMemberId(rs.getInt("gb_member_id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error fetching governing board members");
        }

        return members;
    }
}

// Supporting DTO classes
class EventDTO {
    private Integer eventId;
    private String title;

    // Getters and Setters
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

class SponsorDTO {
    private Integer sponsorId;
    private String sponsorName;

    // Getters and Setters
    public Integer getSponsorId() { return sponsorId; }
    public void setSponsorId(Integer sponsorId) { this.sponsorId = sponsorId; }

    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }
}

class GBMemberDTO {
    private Integer gbMemberId;
    private String name;

    // Getters and Setters
    public Integer getGbMemberId() { return gbMemberId; }
    public void setGbMemberId(Integer gbMemberId) { this.gbMemberId = gbMemberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}