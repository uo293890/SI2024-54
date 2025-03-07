package giis.demo.tkrun;

import java.util.Date;

public class OtherMovementDTO {
    private int editionId;
    private String concept;
    private double amount;
    private String type; // Ensure this is a String
    private String status;
    private Date paymentDate;

    // Getters and Setters
    public int getEditionId() { return editionId; }
    public void setEditionId(int editionId) { this.editionId = editionId; }

    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; } // Ensure this accepts a String

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
}