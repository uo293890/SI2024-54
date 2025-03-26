package giis.demo.tkrun;

import java.sql.Date;

public class CloseEventDTO {
    private int eventId;
    private String typeName;
    private String eventName;
    private Date eventStartDate;
    private Date eventEndDate;
    private String eventLocation;
    private String eventStatus;
    private boolean agreementsClosedOrPaid;
    private boolean invoicesGenerated;
    private boolean incomesExpensesPaid;
    private boolean hasPendingMovements;

    public CloseEventDTO(int eventId, String typeName, String eventName, Date eventStartDate, Date eventEndDate,
                          String eventLocation, String eventStatus,
                          boolean agreementsClosedOrPaid, boolean invoicesGenerated,
                          boolean incomesExpensesPaid, boolean hasPendingMovements) {
        this.eventId = eventId;
        this.typeName = typeName;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventLocation = eventLocation;
        this.eventStatus = eventStatus;
        this.agreementsClosedOrPaid = agreementsClosedOrPaid;
        this.invoicesGenerated = invoicesGenerated;
        this.incomesExpensesPaid = incomesExpensesPaid;
        this.hasPendingMovements = hasPendingMovements;
    }

    public int getEventId() {
        return eventId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public boolean isAgreementsClosedOrPaid() {
        return agreementsClosedOrPaid;
    }

    public boolean isInvoicesGenerated() {
        return invoicesGenerated;
    }

    public boolean isIncomesExpensesPaid() {
        return incomesExpensesPaid;
    }

    public boolean isHasPendingMovements() {
        return hasPendingMovements;
    }

    public boolean isReadyToClose() {
        return agreementsClosedOrPaid && invoicesGenerated && incomesExpensesPaid && !hasPendingMovements;
    }
}
