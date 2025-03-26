package giis.demo.tkrun;

import java.time.LocalDate;

/**
 * Represents a single "other income or expense" movement associated with an event.
 * 
 * <p>As the secretary, you can register these movements for an event after the event
 * is planned and before it is closed. We differentiate between estimated vs. paid
 * movements. For paid movements, we store the date when it was paid.</p>
 * 
 * <p>It is common that estimated amounts differ from the final paid amounts.</p>
 * 
 * <p>Restriction SP1: we only handle "perfect" movements now (no corrections/updates).
 * Future sprints will address error handling and compensation movements.</p>
 */
public class OtherIncomeExpenseDTO {

    // Simple enum to distinguish between an INCOME or an EXPENSE.
    public enum MovementType {
        INCOME,
        EXPENSE
    }

    // Enum to track the status of the movement: ESTIMATED or PAID.
    public enum MovementStatus {
        ESTIMATED,
        PAID
    }

    // Fields for the movement
    private int movementId;             // Internal ID (if needed in DB)
    private int eventId;                // Which event this movement belongs to
    private MovementType movementType;  // INCOME or EXPENSE
    private MovementStatus movementStatus; // ESTIMATED or PAID
    private double estimatedAmount;     // The initial (estimated) amount
    private double paidAmount;          // The final paid amount (if paid)
    private LocalDate paidDate;         // The date when the movement was paid
    private String concept;             // Short description (e.g. "Room fee")

    /**
     * Constructor for creating a new OtherIncomeExpenseDTO.
     *
     * @param movementId      The internal movement ID (if used in the database).
     * @param eventId         The ID of the event this movement is associated with.
     * @param movementType    Whether this is an INCOME or an EXPENSE.
     * @param movementStatus  Whether this is ESTIMATED or PAID.
     * @param estimatedAmount The initially estimated amount for this movement.
     * @param paidAmount      The actual paid amount (may differ from estimated).
     * @param paidDate        The date on which the payment was made (null if not yet paid).
     * @param concept         A short description of the movement (e.g. "Room rental").
     */
    public OtherIncomeExpenseDTO(int movementId, int eventId, MovementType movementType,
                                 MovementStatus movementStatus, double estimatedAmount,
                                 double paidAmount, LocalDate paidDate, String concept) {
        this.movementId = movementId;
        this.eventId = eventId;
        this.movementType = movementType;
        this.movementStatus = movementStatus;
        this.estimatedAmount = estimatedAmount;
        this.paidAmount = paidAmount;
        this.paidDate = paidDate;
        this.concept = concept;
    }

    // Getters and setters

    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public MovementStatus getMovementStatus() {
        return movementStatus;
    }

    public void setMovementStatus(MovementStatus movementStatus) {
        this.movementStatus = movementStatus;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }
}
