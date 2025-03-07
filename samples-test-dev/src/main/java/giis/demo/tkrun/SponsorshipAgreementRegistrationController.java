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
            public void actionPerformed(ActionEvent e) {
                // Create a new DTO object
                SponsorshipAgreementRegistrationDTO dto = new SponsorshipAgreementRegistrationDTO();

                // Set fields from the view
                dto.setEditionId(view.getEventComboBox().getSelectedIndex() + 1); // Assuming edition_id is based on index
                dto.setSponsorId(view.getSponsorComboBox().getSelectedIndex() + 1); // Assuming sponsor_id is based on index
                dto.setNegotiator(view.getNegotiatorField().getText());
                dto.setContactWorker(view.getContactWorkerField().getText());
                dto.setContactNumber(view.getContactNumberField().getText());
                dto.setContactEmail(view.getContactEmailField().getText());
                dto.setAgreementDate(java.time.LocalDate.parse(view.getAgreementDateField().getText()));
                dto.setAgreementAmount(Double.parseDouble(view.getAgreedAmountField().getText()));
                dto.setAgreementStatus("Estimated"); // Default status

                // Register the agreement
                model.registerSponsorshipAgreement(dto);

                // Optionally, show a success message or clear the form
                view.showMessage("Agreement registered successfully!");
                view.clearForm();
            }
        });
    }

    private void initView() {
        // Populate the event combo box with edition titles
        List<SponsorshipAgreementRegistrationDTO> editions = model.getAllEditions();
        for (SponsorshipAgreementRegistrationDTO edition : editions) {
            view.getEventComboBox().addItem(edition.getEditionTitle());
        }

        // Populate the sponsor combo box with sponsor names
        List<SponsorshipAgreementRegistrationDTO> sponsors = model.getAllSponsors();
        for (SponsorshipAgreementRegistrationDTO sponsor : sponsors) {
            view.getSponsorComboBox().addItem(sponsor.getSponsorName());
        }
    }
}