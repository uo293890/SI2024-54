package giis.demo.tkrun;

import java.util.Date;

public class InvoiceDTO {
    private String invoiceNumber;
    private Date invoiceDate;
    private int agreementId;
    private double invoiceVat;
    
    // Getters and Setters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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
