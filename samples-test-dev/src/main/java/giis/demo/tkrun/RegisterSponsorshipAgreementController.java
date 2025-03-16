package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import giis.demo.util.ApplicationException;

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
            	try {
	                // Create a new DTO object
	                RegisterSponsorshipAgreementDTO dto = new RegisterSponsorshipAgreementDTO();
	
	                // Set fields from the view
	                dto.setEditionId(view.getEventComboBox().getSelectedIndex() + 1); // Assuming edition_id is based on index
	                dto.setSponsorId(view.getSponsorComboBox().getSelectedIndex() + 1); // Assuming sponsor_id is based on index
	                
	                // Validate and set contact name
	                if (view.getContactWorkerField().getText().isBlank()) {
	                    throw new ApplicationException("Contact name is required.");
	                }
	                dto.setContactName(view.getContactWorkerField().getText());
	                
	                // Validate contact information (at least one of phone or email is required)
	                if (view.getContactNumberField().getText().isBlank() && view.getContactEmailField().getText().isBlank()) {
	                	throw new ApplicationException("At least one contact method (phone or email) is required.");
	                } else if (view.getContactNumberField().getText().isBlank() && !view.getContactEmailField().getText().isBlank()) {
	                	dto.setContactEmail(view.getContactEmailField().getText()); // Only set email if phone is blank
	                } else if (view.getContactEmailField().getText().isBlank() && !view.getContactNumberField().getText().isBlank()) {
	                	dto.setContactNumber(view.getContactNumberField().getText()); // Only set phone if email is blank
	                } else {
	                	dto.setContactNumber(view.getContactNumberField().getText()); // Set both if both are provided
	                	dto.setContactEmail(view.getContactEmailField().getText());
	                }
	                
	                dto.setGbMemberId(view.getGBMemberComboBox().getSelectedIndex() + 1);
	                
	                // Validate and set agreement date
	                if (view.getAgreementDateField().getText().isBlank()) {
	                    throw new ApplicationException("Agreement date is required.");
	                }
	                dto.setAgreementDate(java.time.LocalDate.parse(view.getAgreementDateField().getText()));
	                
	                // Validate and set agreed amount
	                if (view.getAgreedAmountField().getText().isBlank()) {
	                    throw new ApplicationException("Agreed amount is required.");
	                }
	                dto.setAgreementAmount(Double.parseDouble(view.getAgreedAmountField().getText()));
	                
	                // Set default agreement status
	                dto.setAgreementStatus("Agreed"); // Default status
	
	                // Register the agreement
	                model.registerSponsorshipAgreement(dto);
	
	                // Optionally, show a success message or clear the form
	                view.showMessage("Agreement registered successfully!");
	                view.clearForm(); 
	                
            	} catch (ApplicationException ex) {
                    view.showError(ex.getMessage());
                } catch (Exception ex) {
                    view.showError("An unexpected error occurred: " + ex.getMessage());
                }
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