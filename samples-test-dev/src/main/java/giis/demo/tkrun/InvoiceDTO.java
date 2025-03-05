package giis.demo.tkrun;

import java.sql.Date;

public class InvoiceDTO {
    private String invoiceId;
    private Date invoiceDate;
    private String invoiceNumber;
    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;
    private double baseAmount;
    private double vat;
    private Date sentDate;

    public InvoiceDTO(String invoiceId, Date invoiceDate, String invoiceNumber, 
                      String recipientName, String recipientTaxId, String recipientAddress, 
                      double baseAmount, double vat, Date sentDate) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.recipientName = recipientName;
        this.recipientTaxId = recipientTaxId;
        this.recipientAddress = recipientAddress;
        this.baseAmount = baseAmount;
        this.vat = vat;
        this.sentDate = sentDate;
    }

    // Getters y Setters
    public String getInvoiceId() { return invoiceId; }
    public Date getInvoiceDate() { return invoiceDate; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientTaxId() { return recipientTaxId; }
    public String getRecipientAddress() { return recipientAddress; }
    public double getBaseAmount() { return baseAmount; }
    public double getVat() { return vat; }
    public Date getSentDate() { return sentDate; }
}