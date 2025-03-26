package giis.demo.tkrun;

import java.time.LocalDate;

public class RegisterSponsorshipAgreementDTO {
    private Integer agreementId; // Matches agreement_id in Agreement table
    private Integer levelId; // Matches level_id in Agreement table
    private Integer gbMemberId; // Matches gbmember_id in Agreement table
    private Integer spContactId; // Matches spcontact_id in Agreement table
    private LocalDate agreementDate; // Matches agreement_date in Agreement table
    private Double agreementAmount; // Matches agreement_amount in Agreement table
    private String agreementStatus; // Matches agreement_status in Agreement table
    
    // Additional fields from related tables
    private Integer eventId;
    private Integer sponsorId;
    private String eventName;
    private String sponsorName;
    private String gbMemberName;
    private String gbMemberPosition;
    private String spContactName;
    private String spContactNumber;
    private String spContactEmail;
    private String levelName;
    private Double levelMinAmount;
    private String advantages;

    // Getters and Setters
    public Integer getAgreementId() { return agreementId; }
    public void setAgreementId(Integer agreementId) { this.agreementId = agreementId; }

    public Integer getLevelId() { return levelId; }
    public void setLevelId(Integer levelId) { this.levelId = levelId; }

    public Integer getGbMemberId() { return gbMemberId; }
    public void setGbMemberId(Integer gbMemberId) { this.gbMemberId = gbMemberId; }

    public Integer getSpContactId() { return spContactId; }
    public void setSpContactId(Integer spContactId) { this.spContactId = spContactId; }

    public LocalDate getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDate agreementDate) { this.agreementDate = agreementDate; }

    public Double getAgreementAmount() { return agreementAmount; }
    public void setAgreementAmount(Double agreementAmount) { this.agreementAmount = agreementAmount; }

    public String getAgreementStatus() { return agreementStatus; }
    public void setAgreementStatus(String agreementStatus) { this.agreementStatus = agreementStatus; }
    
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    
    public Integer getSponsorId() { return sponsorId; }
    public void setSponsorId(Integer sponsorId) { this.sponsorId = sponsorId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }

    public String getGbMemberName() { return gbMemberName; }
    public void setGbMemberName(String gbMemberName) { this.gbMemberName = gbMemberName; }

    public String getGbMemberPosition() { return gbMemberPosition; }
    public void setGbMemberPosition(String gbMemberPosition) { this.gbMemberPosition = gbMemberPosition; }

    public String getSpContactName() { return spContactName; }
    public void setSpContactName(String spContactName) { this.spContactName = spContactName; }

    public String getSpContactNumber() { return spContactNumber; }
    public void setSpContactNumber(String spContactNumber) { this.spContactNumber = spContactNumber; }

    public String getSpContactEmail() { return spContactEmail; }
    public void setSpContactEmail(String spContactEmail) { this.spContactEmail = spContactEmail; }

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }

    public Double getLevelMinAmount() { return levelMinAmount; }
    public void setLevelMinAmount(Double levelMinAmount) { this.levelMinAmount = levelMinAmount; }

    public String getAdvantages() { return advantages; }
    public void setAdvantages(String advantages) { this.advantages = advantages; }
}