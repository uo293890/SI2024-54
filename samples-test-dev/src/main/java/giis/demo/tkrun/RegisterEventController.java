package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.RegisterEventView.LevelPanel;
import giis.demo.util.ApplicationException;

public class RegisterEventController {
    private RegisterEventModel model;
    private RegisterEventView view;
    private LocalDate workingDate;

    public RegisterEventController(RegisterEventModel model, RegisterEventView view, LocalDate workingDate) {
        this.model = model;
        this.view = view;
        this.workingDate = workingDate;
        this.initView();
        this.initController();
    }

    private void initController() {    
        view.getAddLevelButton().addActionListener(e -> {
            view.addLevelPanel();
        });
        
        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    validateForm();
                    RegisterEventDTO dto = createEventDTO();
                    model.registerEvent(dto);
                    view.showMessage("Event registered successfully!");
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

    private RegisterEventDTO createEventDTO() throws ApplicationException {
        RegisterEventDTO dto = new RegisterEventDTO();
        
        // Set event type
        int selectedTypeIndex = view.getTypeComboBox().getSelectedIndex();
        List<RegisterEventDTO> types = model.getAllTypes();
        if (selectedTypeIndex < 1 || selectedTypeIndex > types.size()) {
            throw new ApplicationException("Please select a valid event type");
        }
        dto.setTypeId(types.get(selectedTypeIndex - 1).getTypeId());
        
        // Set event name
        String eventName = view.getNameField().getText().trim();
        if (eventName.isEmpty()) {
            throw new ApplicationException("Please enter an event name");
        }
        dto.setEventName(eventName);
        
        // Set and validate dates
        try {
            LocalDate startDate = LocalDate.parse(view.getStartDateField().getText());
            LocalDate endDate = LocalDate.parse(view.getEndDateField().getText());
            
            // Check against working date
            if (startDate.isBefore(workingDate)) {
                throw new ApplicationException("Start date cannot be before the working date (" + workingDate + ")");
            }
            
            if (endDate.isBefore(startDate)) {
                throw new ApplicationException("End date cannot be before start date");
            }
            
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);
        } catch (DateTimeParseException e) {
            throw new ApplicationException("Invalid date format. Please use YYYY-MM-DD");
        }
        
        // Set location
        String location = view.getLocationField().getText().trim();
        if (location.isEmpty()) {
            throw new ApplicationException("Please enter a location");
        }
        dto.setLocation(location);
        
        // Set default status
        dto.setStatus("Planned");
        
        // Set sponsorship levels
        List<RegisterEventDTO.SponsorshipLevelDTO> levels = new ArrayList<>();
        for (LevelPanel panel : view.getLevelPanels()) {
            RegisterEventDTO.SponsorshipLevelDTO level = new RegisterEventDTO.SponsorshipLevelDTO();
            
            String levelName = panel.getNameField().getText().trim();
            if (levelName.isEmpty()) {
                throw new ApplicationException("All sponsorship levels must have a name");
            }
            level.setLevelName(levelName);
            
            try {
                double minAmount = Double.parseDouble(panel.getMinAmountField().getText());
                if (minAmount <= 0) {
                    throw new ApplicationException("Minimum amount must be greater than 0");
                }
                level.setMinAmount(minAmount);
            } catch (NumberFormatException e) {
                throw new ApplicationException("Invalid amount format for sponsorship level");
            }
            
            String advantages = panel.getAdvantagesArea().getText().trim();
            if (advantages.isEmpty()) {
                throw new ApplicationException("All sponsorship levels must have advantages specified");
            }
            level.setAdvantages(advantages);
            
            levels.add(level);
        }
        
        if (levels.isEmpty()) {
            throw new ApplicationException("Please add at least one sponsorship level");
        }
        
        dto.setSponsorshipLevels(levels);
        
        return dto;
    }

    private void validateForm() throws ApplicationException {
        if (view.getTypeComboBox().getSelectedIndex() <= 0) {
            throw new ApplicationException("Please select an event type");
        }
        
        if (view.getNameField().getText().trim().isEmpty()) {
            throw new ApplicationException("Please enter an event name");
        }
        
        try {
            LocalDate startDate = LocalDate.parse(view.getStartDateField().getText());
            LocalDate endDate = LocalDate.parse(view.getEndDateField().getText());
            
            // Validate against working date
            if (startDate.isBefore(workingDate)) {
                throw new ApplicationException("Start date cannot be before working date (" + workingDate + ")");
            }
            
            if (endDate.isBefore(startDate)) {
                throw new ApplicationException("End date cannot be before start date");
            }
        } catch (DateTimeParseException e) {
            throw new ApplicationException("Invalid date format. Please use YYYY-MM-DD");
        }
        
        if (view.getLocationField().getText().trim().isEmpty()) {
            throw new ApplicationException("Please enter a location");
        }
        
        RegisterEventDTO eventData = createEventDTO();
        List<RegisterEventDTO.SponsorshipLevelDTO> levels = eventData.getSponsorshipLevels();
        
        if (levels.isEmpty()) {
            throw new ApplicationException("Please add at least one sponsorship level");
        }
        
        for (RegisterEventDTO.SponsorshipLevelDTO level : levels) {
            if (level.getLevelName().trim().isEmpty()) {
                throw new ApplicationException("All sponsorship levels must have a name");
            }
            if (level.getMinAmount() <= 0) {
                throw new ApplicationException("Minimum amount must be greater than 0 for all levels");
            }
            if (level.getAdvantages().trim().isEmpty()) {
                throw new ApplicationException("All sponsorship levels must have advantages specified");
            }
        }
    }

    private void initView() {
        try {
            view.getTypeComboBox().removeAllItems();
            view.getTypeComboBox().addItem("-- Select Type--");
            
            List<RegisterEventDTO> types = model.getAllTypes();
            for (RegisterEventDTO type : types) {
                view.getTypeComboBox().addItem(type.getTypeName());
            }
            
            // Initialize date fields with working date
            view.getStartDateField().setText(workingDate.toString());
            view.getEndDateField().setText(workingDate.toString());
            
        } catch (ApplicationException ex) {
            view.showError("Error initializing form: " + ex.getMessage());
        }
    }
}