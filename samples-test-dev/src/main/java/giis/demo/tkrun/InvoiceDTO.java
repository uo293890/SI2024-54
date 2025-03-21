// InvoiceDTO.java
package giis.demo.tkrun;

public class InvoiceDTO {
    private String invoiceNumber;
    private int agreementId;
    private String invoiceDate;
    private double invoiceVat;

    public InvoiceDTO(String invoiceNumber, int agreementId, String invoiceDate, double invoiceVat) {
        this.invoiceNumber = invoiceNumber;
        this.agreementId = agreementId;
        this.invoiceDate = invoiceDate;
        this.invoiceVat = invoiceVat;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public int getAgreementId() { return agreementId; }
    public String getInvoiceDate() { return invoiceDate; }
    public double getInvoiceVat() { return invoiceVat; }

    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public void setAgreementId(int agreementId) { this.agreementId = agreementId; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setInvoiceVat(double invoiceVat) { this.invoiceVat = invoiceVat; }
} 