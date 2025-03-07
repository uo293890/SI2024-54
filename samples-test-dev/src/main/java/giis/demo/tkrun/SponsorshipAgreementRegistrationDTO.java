package giis.demo.tkrun;

import java.time.LocalDate;

public class SponsorshipAgreementRegistrationDTO {
    private Integer agreementId; // Matches agreement_id in Agreement table
    private Integer editionId; // Matches edition_id in Agreement table
    private Integer sponsorId; // Matches sponsor_id in Agreement table
    private String negotiator; // Matches negotiator in Agreement table
    private String contactWorker; // Matches contact_worker in Agreement table
    private String contactNumber; // Matches contact_number in Agreement table
    private String contactEmail; // Matches contact_email in Agreement table
    private LocalDate agreementDate; // Matches agreement_date in Agreement table
    private Double agreementAmount; // Matches agreement_amount in Agreement table
    private String agreementStatus; // Matches agreement_status in Agreement table
    
    private String editionTitle;
    private String sponsorName;
	
	public String getEditionTitle() { return editionTitle; }
	public void setEditionTitle(String editionTitle) { this.editionTitle = editionTitle; }
	
    public String getSponsorName() { return sponsorName; }
	public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }

    public Integer getAgreementId() { return agreementId; }
    public void setAgreementId(Integer agreementId) { this.agreementId = agreementId; }

    public Integer getEditionId() { return editionId; }
    public void setEditionId(Integer editionId) { this.editionId = editionId; }

    public Integer getSponsorId() { return sponsorId; }
    public void setSponsorId(Integer sponsorId) { this.sponsorId = sponsorId; }

    public String getNegotiator() { return negotiator; }
    public void setNegotiator(String negotiator) { this.negotiator = negotiator; }

    public String getContactWorker() { return contactWorker; }
    public void setContactWorker(String contactWorker) { this.contactWorker = contactWorker; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public LocalDate getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDate agreementDate) { this.agreementDate = agreementDate; }

    public Double getAgreementAmount() { return agreementAmount; }
    public void setAgreementAmount(Double agreementAmount) { this.agreementAmount = agreementAmount; }

    public String getAgreementStatus() { return agreementStatus; }
    public void setAgreementStatus(String agreementStatus) { this.agreementStatus = agreementStatus; }
}