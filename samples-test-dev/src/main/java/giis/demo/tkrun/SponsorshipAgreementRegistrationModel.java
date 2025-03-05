package giis.demo.tkrun;

public class SponsorshipAgreementRegistrationModel {
    public boolean registerSponsorshipAgreement(SponsorshipAgreementRegistrationDTO agreementDTO) {
        // Validate the agreed amount is at least the fee for the activity
        if (agreementDTO.getAgreedAmount() < getMinimumFeeForActivity(agreementDTO.getActivitySupported())) {
            return false; // Amount is insufficient
        }

        // Save the agreement to the database or perform other business logic
        saveAgreementToDatabase(agreementDTO);

        return true; // Registration successful
    }

    private double getMinimumFeeForActivity(String activitySupported) {
        // Fetch the minimum fee for the activity from a database or configuration
        // This is a placeholder implementation
        return 1000.0; // Example minimum fee
    }

    private void saveAgreementToDatabase(SponsorshipAgreementRegistrationDTO agreementDTO) {
        // Logic to save the agreement to a database
        System.out.println("Agreement saved: " + agreementDTO.getSponsorOrganizationName());
    }
}
