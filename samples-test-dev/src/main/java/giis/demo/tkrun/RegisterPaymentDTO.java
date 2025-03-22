package giis.demo.tkrun;



public class RegisterPaymentDTO {
    private String paymentDate;
    private String concept;
    private double amount;
    private int invoiceId;

    public RegisterPaymentDTO(String paymentDate, String concept, double amount, int invoiceId) {
        this.paymentDate = paymentDate;
        this.concept = concept;
        this.amount = amount;
        this.invoiceId = invoiceId;
    }
    public String getPaymentDate() { return paymentDate; }
    public String getConcept() { return concept; }
    public double getAmount() { return amount; }
    public int getInvoiceId() { return invoiceId; }
}
