package giis.demo.tkrun;

/**
 * Data Transfer Object (DTO) representing an invoice.
 * Used to pass invoice data between layers without exposing database details.
 */
public class InvoiceDTO {
    private String invoiceNumber;
    private int agreementId;
    private String invoiceDate; // puede ser null
    private double invoiceVat;

    private String recipientName;
    private String recipientTaxId;
    private String recipientAddress;

    private final String issuerName = "COIIPA";
    private final String issuerTaxId = "G33026339";
    private final String issuerAddress = "Calle Asturias, 20, 33004 Oviedo, Asturias, España";

    /**
     * Constructs a new InvoiceDTO with the required fields.
     *
     * @param invoiceNumber Unique identifier for the invoice.
     * @param agreementId   Associated agreement ID.
     * @param invoiceVat    VAT percentage applied to the invoice.
     */
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

    public String getIssuerName() { return issuerName; }
    public String getIssuerTaxId() { return issuerTaxId; }
    public String getIssuerAddress() { return issuerAddress; }

    // Setters
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public void setAgreementId(int agreementId) { this.agreementId = agreementId; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setInvoiceVat(double invoiceVat) { this.invoiceVat = invoiceVat; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setRecipientTaxId(String recipientTaxId) { this.recipientTaxId = recipientTaxId; }
    public void setRecipientAddress(String recipientAddress) { this.recipientAddress = recipientAddress; }

    /**
     * Validates if all mandatory recipient tax data is present according to Spanish simplified invoice rules.
     *
     * @return true if recipient name, tax ID and address are not null or empty.
     */
    public boolean hasValidRecipientData() {
        return recipientName != null && !recipientName.trim().isEmpty()
            && recipientTaxId != null && !recipientTaxId.trim().isEmpty()
            && recipientAddress != null && !recipientAddress.trim().isEmpty();
    }
} 