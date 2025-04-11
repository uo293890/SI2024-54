package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import giis.demo.util.ApplicationException;

public class RegisterSponsorshipAgreementController {
    private RegisterSponsorshipAgreementModel model;
    private RegisterSponsorshipAgreementView view;
    private LocalDate workingDate;

    public RegisterSponsorshipAgreementController(RegisterSponsorshipAgreementModel model, RegisterSponsorshipAgreementView view, LocalDate workingDate) {
        this.model = model;
        this.view = view;
        this.workingDate = workingDate;
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
        RegisterSponsorshipAgreementDTO selectedLevel = levels.get(selectedLevelIndex - 1);
        dto.setLevelId(selectedLevel.getLevelId());

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

        // Set date
        LocalDate agreementDate = LocalDate.parse(view.getAgreementDateField().getText());
        if (agreementDate.isAfter(workingDate)) {
            view.showMessage("You have set the date on the future");
        }
        dto.setAgreementDate(agreementDate);
        
        // Get and validate the agreed amount
        double agreedAmount;
        try {
            agreedAmount = Double.parseDouble(view.getAgreedAmountField().getText());
        } catch (NumberFormatException e) {
            throw new ApplicationException("Invalid amount format. Please enter a valid number.");
        }
        
        // Check if amount meets the minimum requirement for the level
        if (agreedAmount < selectedLevel.getLevelMinAmount()) {
            throw new ApplicationException(String.format(
                "The agreed amount (%.2f) is below the minimum required (%.2f) for this sponsorship level",
                agreedAmount, selectedLevel.getLevelMinAmount()));
        }
        dto.setAgreementAmount(agreedAmount);
        
        dto.setAgreementStatus("Agreed");

        return dto;
    }

    private void validateForm() throws ApplicationException {
        if (view.getEventComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select an event");
        }
        if (view.getSponsorComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a sponsor");
        }
        if (view.getSponsorContactComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a sponsor contact");
        }
        if (view.getGbMemberComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a Governing Board Member");
        }
        if (view.getSponsorshipLevelComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select a sponsorship level");
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
            
            view.getSponsorContactComboBox().removeAllItems();
            view.getSponsorContactComboBox().addItem("-- Select Sponsor Contact --");
            
            view.getSponsorshipLevelComboBox().removeAllItems();
            view.getSponsorshipLevelComboBox().addItem("-- Select Level --");
            
            view.getAgreementDateField().setText(workingDate.toString());
            view.getAgreedAmountField().setText("");
            
        } catch (ApplicationException ex) {
            view.showError("Error initializing form: " + ex.getMessage());
        }
    }
}