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
        if (selectedTypeIndex == 0) {
        	dto.setTypeId(0);
        } else { 
        	dto.setTypeId(types.get(selectedTypeIndex - 1).getTypeId());
        }
        
        // Set event name
        String eventName = view.getNameField().getText().trim();
        if (eventName.isEmpty()) {
            throw new ApplicationException("Please enter an event name");
        }
        dto.setEventName(eventName);
        
        // Set and validate dates
        try {
            LocalDate startDate = null;
            LocalDate endDate = null;
            
            // Parse start date if not blank
            if (!view.getStartDateField().getText().trim().isEmpty()) {
                startDate = LocalDate.parse(view.getStartDateField().getText());
                
                // Check against working date
                if (startDate.isBefore(workingDate)) {
                	int confirm = javax.swing.JOptionPane.showConfirmDialog(
                	        null,
                	        "The date entered is in the past. Are you sure you want to proceed?",
                	        "Past Date Confirmation",
                	        javax.swing.JOptionPane.YES_NO_OPTION
                	    );
                	    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                	        throw new ApplicationException("Operation cancelled by the user due to past date.");
                	    }
                }
            }
            
            // Parse end date if not blank
            if (!view.getEndDateField().getText().trim().isEmpty()) {
                endDate = LocalDate.parse(view.getEndDateField().getText());
            }
            
            // Validate end date against start date if both are provided
            if (startDate != null && endDate != null) {
                if (endDate.isBefore(startDate)) {
                    throw new ApplicationException("End date cannot be before start date");
                }
            }
            
            // Set dates in DTO (null values are allowed)
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);
            
        } catch (DateTimeParseException e) {
            throw new ApplicationException("Invalid date format. Please use YYYY-MM-DD");
        }
        
        // Set location
        String location = view.getLocationField().getText().trim();
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
            
            levels.add(level);
        }
        
        if (levels.isEmpty()) {
            throw new ApplicationException("Please add at least one sponsorship level");
        }
        
        dto.setSponsorshipLevels(levels);
        
        return dto;
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