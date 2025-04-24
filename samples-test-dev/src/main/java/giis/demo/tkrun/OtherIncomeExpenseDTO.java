package giis.demo.tkrun;

import java.time.LocalDate;

/**
 * Data object (DTO) for a summary of an Income/Expense entry,
 * including the calculated total paid amount.
 */
public class OtherIncomeExpenseDTO {

    // Fields for an entry summary + calculated total paid
    private int incexpId; // Entry ID
    private int eventId;
    private String eventName; // Event name
    private String concept; // Entry concept
    private double estimatedAmount; // Original estimated amount (can be negative for expenses)
    private String status; // Entry status
    private double totalPaid; // Sum of related movement amounts (can be negative)

    // Constructor to create DTO from database data
    public OtherIncomeExpenseDTO(int incexpId, int eventId, String eventName, String concept,
                                 double estimatedAmount, String status, double totalPaid) {
        this.incexpId = incexpId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.concept = concept;
        this.estimatedAmount = estimatedAmount;
        this.status = status;
        this.totalPaid = totalPaid;
    }

    // Getters for fields (used by controller and view)
    public int getIncexpId() { return incexpId; }
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getConcept() { return concept; }
    public double getEstimatedAmount() { return estimatedAmount; } // Raw estimated amount
    public String getStatus() { return status; }
    public double getTotalPaid() { return totalPaid; } // Raw total paid

    // Helper to determine if it's an income or expense based on estimated amount sign
    public String getType() { return estimatedAmount >= 0 ? "Income" : "Expense"; }

    // Helper to get estimated amount as positive for display in table
    public double getEstimatedAmountForDisplay() { return Math.abs(estimatedAmount); }

    // Helper to get remaining amount (Estimated - Total Paid)
    // This value is used by the table renderer to determine display text and color
    public double getRemainingAmountRaw() { return estimatedAmount - totalPaid; }
}