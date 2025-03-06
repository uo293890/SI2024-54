package giis.demo.tkrun;

import java.time.LocalDate;

public class SponsorshipAgreementRegistrationDTO {
    private Integer eventId;
    private Integer sponsorId;
    private Integer gbMemberId;
    private LocalDate agreementDate;
    private String sponsorshipLevel;
    private Double agreedAmount;
    private String sponsorshipStatus;

    // Getters and Setters
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public Integer getSponsorId() { return sponsorId; }
    public void setSponsorId(Integer sponsorId) { this.sponsorId = sponsorId; }

    public Integer getGbMemberId() { return gbMemberId; }
    public void setGbMemberId(Integer gbMemberId) { this.gbMemberId = gbMemberId; }

    public LocalDate getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDate agreementDate) { this.agreementDate = agreementDate; }

    public String getSponsorshipLevel() { return sponsorshipLevel; }
    public void setSponsorshipLevel(String sponsorshipLevel) { this.sponsorshipLevel = sponsorshipLevel; }

    public Double getAgreedAmount() { return agreedAmount; }
    public void setAgreedAmount(Double agreedAmount) { this.agreedAmount = agreedAmount; }

    public String getSponsorshipStatus() { return sponsorshipStatus; }
    public void setSponsorshipStatus(String sponsorshipStatus) { this.sponsorshipStatus = sponsorshipStatus; }
}