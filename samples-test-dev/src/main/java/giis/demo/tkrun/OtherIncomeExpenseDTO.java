package giis.demo.tkrun;

import java.time.LocalDate;

/**
 * DTO que representa un movimiento de ingreso/gasto.
 */
public class OtherIncomeExpenseDTO {

    public enum MovementType {
        INCOME, EXPENSE
    }

    public enum MovementStatus {
        ESTIMATED, PAID
    }

    private int movementId;
    private int eventId;
    private MovementType movementType;
    private MovementStatus movementStatus;
    private double estimatedAmount;
    private double paidAmount;
    private LocalDate paidDate;
    private String concept;

    public OtherIncomeExpenseDTO(int movementId, int eventId,
                                 MovementType movementType, MovementStatus movementStatus,
                                 double estimatedAmount, double paidAmount,
                                 LocalDate paidDate, String concept) {
        this.movementId = movementId;
        this.eventId = eventId;
        this.movementType = movementType;
        this.movementStatus = movementStatus;
        this.estimatedAmount = estimatedAmount;
        this.paidAmount = paidAmount;
        this.paidDate = paidDate;
        this.concept = concept;
    }

    // Getters y setters

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
