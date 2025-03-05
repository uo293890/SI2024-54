package giis.demo.tkrun;


public class EventDTO {
    private int eventId;
    private String title;
    
    // Constructor vacío necesario
    public EventDTO() {}
    
    // Getters y setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}