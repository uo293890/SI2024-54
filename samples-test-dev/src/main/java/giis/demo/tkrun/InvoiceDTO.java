package giis.demo.tkrun;

public class InvoiceDTO {
    private String invoiceNumber;
    private int agreementId;
    private String invoiceDate; // puede ser null
    private double invoiceVat;

    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;

    public InvoiceDTO(String invoiceNumber, int agreementId, double invoiceVat) {
        this.invoiceNumber = invoiceNumber;
        this.agreementId = agreementId;
        this.invoiceVat = invoiceVat;
        this.invoiceDate = null; // aún no enviada
    }

    // Getters
    public String getInvoiceNumber() { return invoiceNumber; }
    public int getAgreementId() { return agreementId; }
    public String getInvoiceDate() { return invoiceDate; }
    public double getInvoiceVat() { return invoiceVat; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientTaxId() { return recipientTaxId; }
    public String getRecipientAddress() { return recipientAddress; }

    // Setters
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public void setAgreementId(int agreementId) { this.agreementId = agreementId; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setInvoiceVat(double invoiceVat) { this.invoiceVat = invoiceVat; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setRecipientTaxId(String recipientTaxId) { this.recipientTaxId = recipientTaxId; }
    public void setRecipientAddress(String recipientAddress) { this.recipientAddress = recipientAddress; }
}
