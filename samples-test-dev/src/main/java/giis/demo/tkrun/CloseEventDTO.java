package giis.demo.tkrun;

public class CloseEventDTO {
    private int eventId;
    private boolean agreementsClosedOrPaid;
    private boolean invoicesGenerated;
    private boolean incomesExpensesPaid;
    private boolean hasPendingMovements;

    public CloseEventDTO(int eventId, boolean agreementsClosedOrPaid, boolean invoicesGenerated,
                         boolean incomesExpensesPaid, boolean hasPendingMovements) {
        this.eventId = eventId;
        this.agreementsClosedOrPaid = agreementsClosedOrPaid;
        this.invoicesGenerated = invoicesGenerated;
        this.incomesExpensesPaid = incomesExpensesPaid;
        this.hasPendingMovements = hasPendingMovements;
    }

    public int getEventId() {
        return eventId;
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
