package giis.demo.tkrun;

import java.util.Date;

public class InvoiceDTO {
    private String invoiceNumber;
    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;
    private String contactEmail;
    private Date invoiceDate;
    private int agreementId;
    private double invoiceVat;

    // Getters y setters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public double getInvoiceVat() {
        return invoiceVat;
    }

    public void setInvoiceVat(double invoiceVat) {
        this.invoiceVat = invoiceVat;
    }
}
