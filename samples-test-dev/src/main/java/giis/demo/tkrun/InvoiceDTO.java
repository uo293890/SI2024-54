package giis.demo.tkrun;

public class InvoiceDTO {
    private String invoiceNumber;
    private int agreementId;
    private String activityName;
    private String invoiceDate;
    private double invoiceVat;
    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;

    public InvoiceDTO(String invoiceNumber, int agreementId, String activityName, String invoiceDate, double invoiceVat, String recipientName, String recipientTaxId, String recipientAddress) {
        this.invoiceNumber = invoiceNumber;
        this.agreementId = agreementId;
        this.activityName = activityName;
        this.invoiceDate = invoiceDate;
        this.invoiceVat = invoiceVat;
        this.recipientName = recipientName;
        this.recipientTaxId = recipientTaxId;
        this.recipientAddress = recipientAddress;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getInvoiceVat() {
        return invoiceVat;
    }

    public void setInvoiceVat(double invoiceVat) {
        this.invoiceVat = invoiceVat;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientTaxId() {
        return recipientTaxId;
    }

    public void setRecipientTaxId(String recipientTaxId) {
        this.recipientTaxId = recipientTaxId;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}