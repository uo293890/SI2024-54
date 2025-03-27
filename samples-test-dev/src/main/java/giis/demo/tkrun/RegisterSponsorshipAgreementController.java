package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.format.DateTimeParseException;
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
        // Event selection changed - update sponsorship levels
        view.getEventComboBox().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                try {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        int selectedIndex = view.getEventComboBox().getSelectedIndex();
                        
                        if (selectedIndex <= 0) {
                            view.updateSponsorshipLevels(null);
                            return;
                        }
                        
                        List<RegisterSponsorshipAgreementDTO> events = model.getAllEvents();
                        if (selectedIndex > 0 && selectedIndex <= events.size()) {
                            RegisterSponsorshipAgreementDTO selectedEvent = events.get(selectedIndex - 1);
                            List<RegisterSponsorshipAgreementDTO> levels = model.getSponsorshipLevels(selectedEvent.getEventId());
                            
                            view.getSponsorshipLevelComboBox().setEnabled(true);
                            view.updateSponsorshipLevels(levels);
                        }
                    }
                } catch (ApplicationException ex) {
                    view.showError("Error loading sponsorship levels: " + ex.getMessage());
                    view.updateSponsorshipLevels(null);
                }
            }
        });

        // Sponsor selection changed - update contacts
        view.getSponsorComboBox().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                try {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        int selectedIndex = view.getSponsorComboBox().getSelectedIndex();
                        
                        if (selectedIndex <= 0) {
                            view.updateSponsorContacts(null);
                            return;
                        }
                        
                        List<RegisterSponsorshipAgreementDTO> sponsors = model.getAllSponsors();
                        if (selectedIndex > 0 && selectedIndex <= sponsors.size()) {
                            RegisterSponsorshipAgreementDTO selectedSponsor = sponsors.get(selectedIndex - 1);
                            List<RegisterSponsorshipAgreementDTO> contacts = model.getSponsorContacts(selectedSponsor.getSponsorId());
                            
                            view.getSponsorContactComboBox().setEnabled(true);
                            view.updateSponsorContacts(contacts);
                        }
                    }
                } catch (ApplicationException ex) {
                    view.showError("Error loading sponsor contacts: " + ex.getMessage());
                    view.updateSponsorContacts(null);
                }
            }
        });

        // Register button action
        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    validateForm();
                    
                    RegisterSponsorshipAgreementDTO dto = createAgreementDTO();
                    model.registerSponsorshipAgreement(dto);
                    
                    view.showMessage("Sponsorship agreement registered successfully!");
                    view.clearForm();
                } catch (ApplicationException ex) {
                    view.showError(ex.getMessage());
                } catch (DateTimeParseException ex) {
                    view.showError("Invalid date format. Please use YYYY-MM-DD format.");
                } catch (NumberFormatException ex) {
                    view.showError("Invalid amount format. Please enter a valid number.");
                } catch (Exception ex) {
                    view.showError("Unexpected error: " + ex.getMessage());
                }
            }
        });
    }

    private RegisterSponsorshipAgreementDTO createAgreementDTO() throws ApplicationException {
        RegisterSponsorshipAgreementDTO dto = new RegisterSponsorshipAgreementDTO();
        
        // Set sponsorship level
        int selectedLevelIndex = view.getSponsorshipLevelComboBox().getSelectedIndex();
        List<RegisterSponsorshipAgreementDTO> levels = view.getCurrentSponsorshipLevels();
        if (selectedLevelIndex < 1 || selectedLevelIndex > levels.size()) {
            throw new ApplicationException("Please select a valid sponsorship level");
        }
        dto.setLevelId(levels.get(selectedLevelIndex - 1).getLevelId());

        // Set GB member
        int selectedGbMemberIndex = view.getGbMemberComboBox().getSelectedIndex();
        List<RegisterSponsorshipAgreementDTO> gbMembers = model.getAllGBMembers();
        if (selectedGbMemberIndex < 1 || selectedGbMemberIndex > gbMembers.size()) {
            throw new ApplicationException("Please select a valid GB member");
        }
        dto.setGbMemberId(gbMembers.get(selectedGbMemberIndex - 1).getGbMemberId());

        // Set sponsor contact
        int selectedContactIndex = view.getSponsorContactComboBox().getSelectedIndex();
        List<RegisterSponsorshipAgreementDTO> contacts = view.getCurrentSponsorContacts();
        if (selectedContactIndex < 1 || selectedContactIndex > contacts.size()) {
            throw new ApplicationException("Please select a valid sponsor contact");
        }
        dto.setSpContactId(contacts.get(selectedContactIndex - 1).getSpContactId());

        // Set date and amount
        dto.setAgreementDate(java.time.LocalDate.parse(view.getAgreementDateField().getText()));
        dto.setAgreementAmount(Double.parseDouble(view.getAgreedAmountField().getText()));
        dto.setAgreementStatus("Agreed");

        return dto;
    }

    private void validateForm() throws ApplicationException {
        if (view.getEventComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select an event");
        }
        if (view.getSponsorshipLevelComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a sponsorship level");
        }
        if (view.getSponsorComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a sponsor");
        }
        if (view.getSponsorContactComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a sponsor contact");
        }
        if (view.getGbMemberComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a GB member");
        }
        if (view.getAgreementDateField().getText().isEmpty()) {
            throw new ApplicationException("Please enter an agreement date");
        }
        if (view.getAgreedAmountField().getText().isEmpty()) {
            throw new ApplicationException("Please enter an agreed amount");
        }
    }

    private void initView() {
        try {
            // Clear existing items first
            view.getEventComboBox().removeAllItems();
            view.getEventComboBox().addItem("-- Select Event --");
            
            List<RegisterSponsorshipAgreementDTO> events = model.getAllEvents();
            for (RegisterSponsorshipAgreementDTO event : events) {
                view.getEventComboBox().addItem(event.getEventName());
            }
            
            view.getSponsorComboBox().removeAllItems();
            view.getSponsorComboBox().addItem("-- Select Sponsor --");

            List<RegisterSponsorshipAgreementDTO> sponsors = model.getAllSponsors();
            for (RegisterSponsorshipAgreementDTO sponsor : sponsors) {
                view.getSponsorComboBox().addItem(sponsor.getSponsorName());
            }
            
            view.getGbMemberComboBox().removeAllItems();
            view.getGbMemberComboBox().addItem("-- Select GB Member --");

            List<RegisterSponsorshipAgreementDTO> gbMembers = model.getAllGBMembers();
            for (RegisterSponsorshipAgreementDTO member : gbMembers) {
                view.getGbMemberComboBox().addItem(member.getGbMemberName() + " (" + member.getGbMemberPosition() + ")");
            }
            
        } catch (ApplicationException ex) {
            view.showError("Error initializing form: " + ex.getMessage());
        }
    }
}