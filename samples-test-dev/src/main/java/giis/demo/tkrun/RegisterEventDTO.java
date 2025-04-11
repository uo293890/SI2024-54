package giis.demo.tkrun;

import java.time.LocalDate;
import java.util.List;

public class RegisterEventDTO {
    private int typeId;
    private String typeName;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String status;
    private List<SponsorshipLevelDTO> sponsorshipLevels;

    // Getters and Setters
    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<SponsorshipLevelDTO> getSponsorshipLevels() { return sponsorshipLevels; }
    public void setSponsorshipLevels(List<SponsorshipLevelDTO> sponsorshipLevels) { 
        this.sponsorshipLevels = sponsorshipLevels; 
    }

    // Nested DTO for sponsorship levels
    public static class SponsorshipLevelDTO {
    	private int eventId;
        private String levelName;
        private double minAmount;

        // Getters and Setters
        public int getEventId() { return eventId; }
        public void setEventId(int eventId) { this.eventId = eventId; }
        
        public String getLevelName() { return levelName; }
        public void setLevelName(String levelName) { this.levelName = levelName; }
        
        public double getMinAmount() { return minAmount; }
        public void setMinAmount(double minAmount) { this.minAmount = minAmount; }
    }
}