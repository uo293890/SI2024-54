package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RegisterSponsorshipAgreementController {
    private RegisterSponsorshipAgreementModel model;
    private RegisterSponsorshipAgreementView view;

    public RegisterSponsorshipAgreementController(RegisterSponsorshipAgreementModel model, RegisterSponsorshipAgreementView view) {
        this.model = model;
        this.view = view;
        this.initView();
        this.initController();
    }

    private void initController() {
        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new DTO object
                RegisterSponsorshipAgreementDTO dto = new RegisterSponsorshipAgreementDTO();

                // Set fields from the view
                dto.setEditionId(view.getEventComboBox().getSelectedIndex() + 1); // Assuming edition_id is based on index
                dto.setSponsorId(view.getSponsorComboBox().getSelectedIndex() + 1); // Assuming sponsor_id is based on index
                dto.setGbMemberId(view.getGBMemberComboBox().getSelectedIndex() + 1);
                dto.setContactWorker(view.getContactWorkerField().getText());
                dto.setContactNumber(view.getContactNumberField().getText());
                dto.setContactEmail(view.getContactEmailField().getText());
                dto.setAgreementDate(java.time.LocalDate.parse(view.getAgreementDateField().getText()));
                dto.setAgreementAmount(Double.parseDouble(view.getAgreedAmountField().getText()));
                dto.setAgreementStatus("Estimated"); // Default status
                
                if (dto.getAgreementDate().equals(null))
                	view.showError("date is blank");

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
        List<RegisterSponsorshipAgreementDTO> editions = model.getAllEditions();
        for (RegisterSponsorshipAgreementDTO edition : editions) {
            view.getEventComboBox().addItem(edition.getEditionTitle());
        }

        // Populate the sponsor combo box with sponsor names
        List<RegisterSponsorshipAgreementDTO> sponsors = model.getAllSponsors();
        for (RegisterSponsorshipAgreementDTO sponsor : sponsors) {
            view.getSponsorComboBox().addItem(sponsor.getSponsorName());
        }
        
     // Populate the gbmember combo box with gbmember names
        List<RegisterSponsorshipAgreementDTO> gbmembers = model.getAllGBMembers();
        for (RegisterSponsorshipAgreementDTO gbmember : gbmembers) {
            view.getGBMemberComboBox().addItem(gbmember.getGbMemberName());
        }
    }
}