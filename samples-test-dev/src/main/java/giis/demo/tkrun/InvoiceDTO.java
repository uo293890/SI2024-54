package giis.demo.tkrun;

public class InvoiceDTO {
    private String invoiceNumber;
    private int agreementId;
    private String activityName;
    private String issueDate;
    private String invoiceDate;
    private double invoiceVat;
    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;

    public InvoiceDTO(String invoiceNumber, int agreementId, String activityName, String issueDate, String invoiceDate, double invoiceVat, String recipientName, String recipientTaxId, String recipientAddress) {
        this.invoiceNumber = invoiceNumber;
        this.agreementId = agreementId;
        this.activityName = activityName;
        this.issueDate = issueDate;
        this.invoiceDate = invoiceDate;
        this.invoiceVat = invoiceVat;
        this.recipientName = recipientName;
        this.recipientTaxId = recipientTaxId;
        this.recipientAddress = recipientAddress;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public int getAgreementId() { return agreementId; }
    public String getActivityName() { return activityName; }
    public String getIssueDate() { return issueDate; }
    public String getInvoiceDate() { return invoiceDate; }
    public double getInvoiceVat() { return invoiceVat; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientTaxId() { return recipientTaxId; }
    public String getRecipientAddress() { return recipientAddress; }

    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public void setAgreementId(int agreementId) { this.agreementId = agreementId; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setInvoiceVat(double invoiceVat) { this.invoiceVat = invoiceVat; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setRecipientTaxId(String recipientTaxId) { this.recipientTaxId = recipientTaxId; }
    public void setRecipientAddress(String recipientAddress) { this.recipientAddress = recipientAddress; }
}