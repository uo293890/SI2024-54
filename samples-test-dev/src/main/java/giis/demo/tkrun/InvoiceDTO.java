package giis.demo.tkrun;

import java.sql.Date;

public class InvoiceDTO {
    private String invoiceNumber;
    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;
    private String contactEmail;
    private Date invoiceDate;
    private int agreementId;
    private int activityId;
    private double invoiceVat;

    // Constructors
    public InvoiceDTO() {}

    public InvoiceDTO(String invoiceNumber, String recipientName, String recipientTaxId, String recipientAddress, String contactEmail, Date invoiceDate, int agreementId, int activityId, double invoiceVat) {
        this.invoiceNumber = invoiceNumber;
        this.recipientName = recipientName;
        this.recipientTaxId = recipientTaxId;
        this.recipientAddress = recipientAddress;
        this.contactEmail = contactEmail;
        this.invoiceDate = invoiceDate;
        this.agreementId = agreementId;
        this.activityId = activityId;
        this.invoiceVat = invoiceVat;
    }

    // Getters and Setters
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getRecipientTaxId() { return recipientTaxId; }
    public void setRecipientTaxId(String recipientTaxId) { this.recipientTaxId = recipientTaxId; }
    public String getRecipientAddress() { return recipientAddress; }
    public void setRecipientAddress(String recipientAddress) { this.recipientAddress = recipientAddress; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public Date getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(Date invoiceDate) { this.invoiceDate = invoiceDate; }
    public int getAgreementId() { return agreementId; }
    public void setAgreementId(int agreementId) { this.agreementId = agreementId; }
    public int getActivityId() { return activityId; }
    public void setActivityId(int activityId) { this.activityId = activityId; }
    public double getInvoiceVat() { return invoiceVat; }
    public void setInvoiceVat(double invoiceVat) { this.invoiceVat = invoiceVat; }
}