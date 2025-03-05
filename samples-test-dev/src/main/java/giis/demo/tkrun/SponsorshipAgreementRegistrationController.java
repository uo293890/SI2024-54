package giis.demo.tkrun;

public class SponsorshipAgreementRegistrationController {
    private SponsorshipAgreementRegistrationModel model;

    public SponsorshipAgreementRegistrationController() {
        this.model = new SponsorshipAgreementRegistrationModel();
    }

    public boolean registerAgreement(SponsorshipAgreementRegistrationDTO agreementDTO) {
        return model.registerSponsorshipAgreement(agreementDTO);
    }
}