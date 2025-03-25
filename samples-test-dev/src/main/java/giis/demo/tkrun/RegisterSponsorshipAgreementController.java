package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
        // Event selection changed - update sponsorship levels
        view.getEventComboBox().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = view.getEventComboBox().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        RegisterSponsorshipAgreementDTO selectedEvent = model.getAllEvents().get(selectedIndex);
                        List<RegisterSponsorshipAgreementDTO> levels = model.getSponsorshipLevels(selectedEvent.getEventId());
                        view.updateSponsorshipLevels(levels);
                    }
                }
            }
        });

        // Sponsor selection changed - update contacts
        view.getSponsorComboBox().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = view.getSponsorComboBox().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        RegisterSponsorshipAgreementDTO selectedSponsor = model.getAllSponsors().get(selectedIndex);
                        List<RegisterSponsorshipAgreementDTO> contacts = model.getSponsorContacts(selectedSponsor.getSponsorId());
                        view.updateSponsorContacts(contacts);
                    }
                }
            }
        });

        // Register button action
        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Create a new DTO object
                    RegisterSponsorshipAgreementDTO dto = new RegisterSponsorshipAgreementDTO();

                    // Set fields from the view
                    int selectedLevelIndex = view.getSponsorshipLevelComboBox().getSelectedIndex();
                    List<RegisterSponsorshipAgreementDTO> levels = view.getCurrentSponsorshipLevels();
                    if (selectedLevelIndex >= 0 && selectedLevelIndex < levels.size()) {
                        dto.setLevelId(levels.get(selectedLevelIndex).getLevelId());
                    }

                    int selectedGbMemberIndex = view.getGbMemberComboBox().getSelectedIndex();
                    List<RegisterSponsorshipAgreementDTO> gbMembers = model.getAllGBMembers();
                    if (selectedGbMemberIndex >= 0 && selectedGbMemberIndex < gbMembers.size()) {
                        dto.setGbMemberId(gbMembers.get(selectedGbMemberIndex).getGbMemberId());
                    }

                    int selectedContactIndex = view.getSponsorContactComboBox().getSelectedIndex();
                    List<RegisterSponsorshipAgreementDTO> contacts = view.getCurrentSponsorContacts();
                    if (selectedContactIndex >= 0 && selectedContactIndex < contacts.size()) {
                        dto.setSpContactId(contacts.get(selectedContactIndex).getSpContactId());
                    }

                    dto.setAgreementDate(java.time.LocalDate.parse(view.getAgreementDateField().getText()));
                    dto.setAgreementAmount(Double.parseDouble(view.getAgreedAmountField().getText()));
                    dto.setAgreementStatus("Agreed"); // Default status

                    // Register the agreement
                    model.registerSponsorshipAgreement(dto);

                    // Show success message and clear form
                    view.showMessage("Sponsorship agreement registered successfully!");
                    view.clearForm();
                } catch (Exception ex) {
                    view.showError("Error registering agreement: " + ex.getMessage());
                }
            }
        });
    }

    private void initView() {
        // Populate the event combo box
        List<RegisterSponsorshipAgreementDTO> events = model.getAllEvents();
        for (RegisterSponsorshipAgreementDTO event : events) {
            view.getEventComboBox().addItem(event.getEventName());
        }

        // Populate the sponsor combo box
        List<RegisterSponsorshipAgreementDTO> sponsors = model.getAllSponsors();
        for (RegisterSponsorshipAgreementDTO sponsor : sponsors) {
            view.getSponsorComboBox().addItem(sponsor.getSponsorName());
        }

        // Populate the GB member combo box
        List<RegisterSponsorshipAgreementDTO> gbMembers = model.getAllGBMembers();
        for (RegisterSponsorshipAgreementDTO member : gbMembers) {
            view.getGbMemberComboBox().addItem(member.getGbMemberName() + " (" + member.getGbMemberPosition() + ")");
        }
    }
}