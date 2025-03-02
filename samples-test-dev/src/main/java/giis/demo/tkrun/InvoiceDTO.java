package giis.demo.tkrun;

public class InvoiceDTO {
    private String invoiceId;
    private String invoiceDate;
    private String sponsorId;

    public InvoiceDTO(String invoiceId, String invoiceDate, String sponsorId) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.sponsorId = sponsorId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getSponsorId() {
        return sponsorId;
    }
}