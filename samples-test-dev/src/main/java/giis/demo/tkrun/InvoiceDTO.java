package giis.demo.tkrun;

import java.sql.Date;

public class InvoiceDTO {
    private String invoiceNumber;
    private Date invoiceDate;
    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;
    private double baseAmount;
    private double vat;
    private double totalAmount;
    private Date sentDate;

    // Constructor
    public InvoiceDTO(String invoiceNumber, Date invoiceDate, String recipientName, 
                      String recipientTaxId, String recipientAddress, double baseAmount, 
                      double vat, double totalAmount, Date sentDate) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.recipientName = recipientName;
        this.recipientTaxId = recipientTaxId;
        this.recipientAddress = recipientAddress;
        this.baseAmount = baseAmount;
        this.vat = vat;
        this.totalAmount = totalAmount;
        this.sentDate = sentDate;
    }

    // Getters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientTaxId() {
        return recipientTaxId;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public double getVat() {
        return vat;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getSentDate() {
        return sentDate;
    }
}