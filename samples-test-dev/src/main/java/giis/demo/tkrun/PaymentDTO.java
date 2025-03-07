package giis.demo.tkrun;

import java.util.Date;

public class PaymentDTO {
    private int invoiceId; // ID of the invoice
    private Date paymentDate; // Date the payment was received
    private double amount; // Amount received (must match the invoice amount)

    // Getters and Setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}