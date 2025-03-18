package giis.demo.tkrun;

import java.time.LocalDate;

public class ConsultActivityFinancialStatusDTO {
    // Event details
    private int eventId;
    private String eventTitle;

    // Edition details
    private int editionId;
    private String editionTitle;
    private String editionStartDate;
    private String editionEndDate;
    private String editionStatus;

    // Agreement details
    private int agreementId;
    private String sponsorName;
    private LocalDate agreementDate;
    private double agreementAmount;
    private String agreementStatus;

    // Otherie (expenses) details
    private int otherieId;
    private double otherieAmount;
    private String otherieDescription;
    private String otherieStatus;

    // Invoice (income) details
    private int invoiceId;
    private LocalDate invoiceDate;
    private double invoiceAmount;
    private String invoiceStatus;

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public int getEditionId() { return editionId; }
    public void setEditionId(int editionId) { this.editionId = editionId; }

    public String getEditionTitle() { return editionTitle; }
    public void setEditionTitle(String editionTitle) { this.editionTitle = editionTitle; }

    public String getEditionStartDate() { return editionStartDate; }
    public void setEditionStartDate(String editionStartDate) { this.editionStartDate = editionStartDate; }

    public String getEditionEndDate() { return editionEndDate; }
    public void setEditionEndDate(String editionEndDate) { this.editionEndDate = editionEndDate; }

    public String getEditionStatus() { return editionStatus; }
    public void setEditionStatus(String editionStatus) { this.editionStatus = editionStatus; }

    public int getAgreementId() { return agreementId; }
    public void setAgreementId(int agreementId) { this.agreementId = agreementId; }

    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }

    public LocalDate getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDate agreementDate) { this.agreementDate = agreementDate; }

    public double getAgreementAmount() { return agreementAmount; }
    public void setAgreementAmount(double agreementAmount) { this.agreementAmount = agreementAmount; }

    public String getAgreementStatus() { return agreementStatus; }
    public void setAgreementStatus(String agreementStatus) { this.agreementStatus = agreementStatus; }

    public int getOtherieId() { return otherieId; }
    public void setOtherieId(int otherieId) { this.otherieId = otherieId; }

    public double getOtherieAmount() { return otherieAmount; }
    public void setOtherieAmount(double otherieAmount) { this.otherieAmount = otherieAmount; }

    public String getOtherieDescription() { return otherieDescription; }
    public void setOtherieDescription(String otherieDescription) { this.otherieDescription = otherieDescription; }

    public String getOtherieStatus() { return otherieStatus; }
    public void setOtherieStatus(String otherieStatus) { this.otherieStatus = otherieStatus; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public double getInvoiceAmount() { return invoiceAmount; }
    public void setInvoiceAmount(double invoiceAmount) { this.invoiceAmount = invoiceAmount; }

    public String getInvoiceStatus() { return invoiceStatus; }
    public void setInvoiceStatus(String invoiceStatus) { this.invoiceStatus = invoiceStatus; }
}