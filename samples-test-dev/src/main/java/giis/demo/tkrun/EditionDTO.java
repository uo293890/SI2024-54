package giis.demo.tkrun;

import java.sql.Date;

public class EditionDTO {
    private int editionId;
    private int eventId;
    private String title;
    private Date date;
    private String place;
    private boolean plannedCompleted;

    // Constructor vacío necesario para DbUtils
    public EditionDTO() {}

    // Getters y Setters
    public int getEditionId() { return editionId; }
    public void setEditionId(int editionId) { this.editionId = editionId; }
    
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    
    public boolean isPlannedCompleted() { return plannedCompleted; }
    public void setPlannedCompleted(boolean plannedCompleted) { this.plannedCompleted = plannedCompleted; }
}