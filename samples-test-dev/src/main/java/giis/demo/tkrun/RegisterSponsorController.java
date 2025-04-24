package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.RegisterSponsorView.ContactPanel;
import giis.demo.util.ApplicationException;

public class RegisterSponsorController {
    private RegisterSponsorModel model;
    private RegisterSponsorView view;

    public RegisterSponsorController(RegisterSponsorModel model, RegisterSponsorView view) {
        this.model = model;
        this.view = view;
        this.initController();
    }

    private void initController() {    
        view.getAddContactButton().addActionListener(e -> {
            view.addContactPanel();
        });
        
        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RegisterSponsorDTO dto = createSponsorDTO();
                    model.registerSponsor(dto);
                    view.showMessage("Sponsor registered successfully!");
                    view.clearForm();
                } catch (ApplicationException ex) {
                    view.showError(ex.getMessage());
                } catch (Exception ex) {
                    view.showError("Unexpected error: " + ex.getMessage());
                }
            }
        });
    }

    private RegisterSponsorDTO createSponsorDTO() throws ApplicationException {
        RegisterSponsorDTO dto = new RegisterSponsorDTO();
        
        // Set sponsor name
        String sponsorName = view.getSponsorNameField().getText().trim();
        if (sponsorName.isEmpty()) {
            throw new ApplicationException("Please enter a sponsor name");
        }
        dto.setSponsorName(sponsorName);
        
        // Set contacts
        List<RegisterSponsorDTO.SponsorContactDTO> contacts = new ArrayList<>();
        for (ContactPanel panel : view.getContactPanels()) {
            RegisterSponsorDTO.SponsorContactDTO contact = new RegisterSponsorDTO.SponsorContactDTO();
            
            String contactName = panel.getNameField().getText().trim();
            if (contactName.isEmpty()) {
                throw new ApplicationException("All contacts must have a name");
            }
            contact.setContactName(contactName);
            
            String email = panel.getEmailField().getText().trim();
            if (email.isEmpty()) {
                throw new ApplicationException("Email is required for all contacts");
            }
            if (!email.contains("@")) {
                throw new ApplicationException("Invalid email format");
            }
            contact.setEmail(email);
            
            contacts.add(contact);
        }
        
        if (contacts.isEmpty()) {
            throw new ApplicationException("Please add at least one contact");
        }
        
        dto.setContacts(contacts);
        
        return dto;
    }
}