package giis.demo.tkrun;

import java.time.LocalDate;

public class SponsorshipAgreementRegistrationDTO {
    private Integer eventId;
    private String eventTitle;
    private Integer sponsorId;
    private String sponsorName;
    private String negotiator;
    private LocalDate agreementDate;
    private Double agreedAmount;
    private String sponsorshipStatus;
    private String sponsorContactName;
    private String sponsorContactEmail;
    private String sponsorContactPhone;
    
    // Getters and Setters
    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    
    public Integer getSponsorId() { return sponsorId; }
    public void setSponsorId(Integer sponsorId) { this.sponsorId = sponsorId; }
    
    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }
    
    public String getNegotiator() { return negotiator; }
    public void setNegotiator(String negotiator) { this.negotiator = negotiator; }
    
    public LocalDate getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDate agreementDate) { this.agreementDate = agreementDate; }
    
    public Double getAgreedAmount() { return agreedAmount; }
    public void setAgreedAmount(Double agreedAmount) { this.agreedAmount = agreedAmount; }
    
    public String getSponsorshipStatus() { return sponsorshipStatus; }
    public void setSponsorshipStatus(String sponsorshipStatus) { this.sponsorshipStatus = sponsorshipStatus; }
    
    public String getSponsorContactName() { return sponsorContactName; }
    public void setSponsorContactName(String sponsorContactName) { this.sponsorContactName = sponsorContactName; }
    
    public String getSponsorContactEmail() { return sponsorContactEmail; }
    public void setSponsorContactEmail(String sponsorContactEmail) { this.sponsorContactEmail = sponsorContactEmail; }
    
    public String getSponsorContactPhone() { return sponsorContactPhone; }
    public void setSponsorContactPhone(String sponsorContactPhone) { this.sponsorContactPhone = sponsorContactPhone; }
}
