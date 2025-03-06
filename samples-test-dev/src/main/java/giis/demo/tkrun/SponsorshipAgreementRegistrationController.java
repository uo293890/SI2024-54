package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SponsorshipAgreementRegistrationController {
    private SponsorshipAgreementRegistrationModel model;
    private SponsorshipAgreementRegistrationView view;

    public SponsorshipAgreementRegistrationController(SponsorshipAgreementRegistrationModel model, SponsorshipAgreementRegistrationView view) {
        this.model = model;
        this.view = view;
        this.initView();
        this.initController();
    }

    private void initController() {
        view.getRegisterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SponsorshipAgreementRegistrationDTO dto = new SponsorshipAgreementRegistrationDTO();
                    dto.setEventId(view.getEventComboBox().getSelectedIndex() + 1);
                    dto.setSponsorId(view.getSponsorComboBox().getSelectedIndex() + 1);
                    dto.setGbMemberId(view.getGbMemberComboBox().getSelectedIndex() + 1);
                    dto.setAgreementDate(java.time.LocalDate.parse(view.getAgreementDateField().getText()));
                    dto.setAgreedAmount(Double.parseDouble(view.getAgreedAmountField().getText()));
                    dto.setSponsorshipStatus("Active");
                    dto.setSponsorContactName(view.getSponsorContactNameField().getText());
                    dto.setSponsorContactEmail(view.getSponsorContactEmailField().getText());
                    dto.setSponsorContactPhone(view.getSponsorContactPhoneField().getText());

                    model.registerSponsorshipAgreement(dto);
                    view.showMessage("Sponsorship agreement registered successfully!");
                } catch (Exception ex) {
                    view.showError("Error registering sponsorship agreement: " + ex.getMessage());
                }
            }
        });
    }

    private void initView() {
        List<SponsorshipAgreementRegistrationDTO> events = model.getAllEvents();
        for (SponsorshipAgreementRegistrationDTO event : events) {
            view.getEventComboBox().addItem(event.getEventTitle());
        }

        List<SponsorshipAgreementRegistrationDTO> sponsors = model.getAllSponsors();
        for (SponsorshipAgreementRegistrationDTO sponsor : sponsors) {
            view.getSponsorComboBox().addItem(sponsor.getSponsorName());
        }

        List<SponsorshipAgreementRegistrationDTO> members = model.getAllGBMembers();
        for (SponsorshipAgreementRegistrationDTO member : members) {
            view.getGbMemberComboBox().addItem(member.getGbMemberName());
        }
    }
}
