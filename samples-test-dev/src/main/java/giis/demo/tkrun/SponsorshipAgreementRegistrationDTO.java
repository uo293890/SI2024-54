package giis.demo.tkrun;

import java.time.LocalDate;

public class SponsorshipAgreementRegistrationDTO {
    private String sponsorOrganizationName;
    private String sponsorContactName;
    private String sponsorContactEmail;
    private double agreedAmount;
    private String activitySupported;
    private String governingBoardMemberName;
    private String governingBoardMemberRole;
    private LocalDate agreementDate;

    // Getters and Setters
    public String getSponsorOrganizationName() {
        return sponsorOrganizationName;
    }

    public void setSponsorOrganizationName(String sponsorOrganizationName) {
        this.sponsorOrganizationName = sponsorOrganizationName;
    }

    public String getSponsorContactName() {
        return sponsorContactName;
    }

    public void setSponsorContactName(String sponsorContactName) {
        this.sponsorContactName = sponsorContactName;
    }

    public String getSponsorContactEmail() {
        return sponsorContactEmail;
    }

    public void setSponsorContactEmail(String sponsorContactEmail) {
        this.sponsorContactEmail = sponsorContactEmail;
    }

    public double getAgreedAmount() {
        return agreedAmount;
    }

    public void setAgreedAmount(double agreedAmount) {
        this.agreedAmount = agreedAmount;
    }

    public String getActivitySupported() {
        return activitySupported;
    }

    public void setActivitySupported(String activitySupported) {
        this.activitySupported = activitySupported;
    }

    public String getGoverningBoardMemberName() {
        return governingBoardMemberName;
    }

    public void setGoverningBoardMemberName(String governingBoardMemberName) {
        this.governingBoardMemberName = governingBoardMemberName;
    }

    public String getGoverningBoardMemberRole() {
        return governingBoardMemberRole;
    }

    public void setGoverningBoardMemberRole(String governingBoardMemberRole) {
        this.governingBoardMemberRole = governingBoardMemberRole;
    }

    public LocalDate getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(LocalDate agreementDate) {
        this.agreementDate = agreementDate;
    }
}
