package giis.demo.tkrun;

import java.util.List;

public class SponsorshipAgreementRegistrationController {
    private SponsorshipAgreementRegistrationModel model;
    private SponsorshipAgreementRegistrationView view;

    public SponsorshipAgreementRegistrationController(SponsorshipAgreementRegistrationModel model, SponsorshipAgreementRegistrationView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Registers a new sponsorship agreement.
     */
    public void registerAgreement(SponsorshipAgreementRegistrationDTO dto) {
        model.registerSponsorship(dto);
    }

    /**
     * Fetches all events for the view.
     */
    public List<EventDTO> getAllEvents() {
        return model.getAllEvents();
    }

    /**
     * Fetches all sponsors for the view.
     */
    public List<SponsorDTO> getAllSponsors() {
        return model.getAllSponsors();
    }

    /**
     * Fetches all governing board members for the view.
     */
    public List<GBMemberDTO> getAllGBMembers() {
        return model.getAllGBMembers();
    }
}