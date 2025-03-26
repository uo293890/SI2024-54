package giis.demo.tkrun;

import java.time.LocalDate;

public class ConsultActivityFinancialStatusDTO {
    // Event details
    private int eventId;
    private String eventName;
    private String eventInidate;
    private String eventEnddate;
    private String eventStatus;
    private String typeName;

    // Sponsor/Agreement details
    private int agreementId;
    private String sponsorName;
    private LocalDate agreementDate;
    private double agreementAmount;
    private String agreementStatus;
    private String levelName;

    // Income/Expense details
    private int incexpId;
    private String incexpConcept;
    private double incexpAmount;
    private String incexpStatus;

    // Invoice details
    private int invoiceId;
    private LocalDate invoiceDate;
    private double invoiceAmount;
    private String invoiceNumber;

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventInidate() { return eventInidate; }
    public void setEventInidate(String eventInidate) { this.eventInidate = eventInidate; }

    public String getEventEnddate() { return eventEnddate; }
    public void setEventEnddate(String eventEnddate) { this.eventEnddate = eventEnddate; }

    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

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

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }

    public int getIncexpId() { return incexpId; }
    public void setIncexpId(int incexpId) { this.incexpId = incexpId; }

    public String getIncexpConcept() { return incexpConcept; }
    public void setIncexpConcept(String incexpConcept) { this.incexpConcept = incexpConcept; }

    public double getIncexpAmount() { return incexpAmount; }
    public void setIncexpAmount(double incexpAmount) { this.incexpAmount = incexpAmount; }

    public String getIncexpStatus() { return incexpStatus; }
    public void setIncexpStatus(String incexpStatus) { this.incexpStatus = incexpStatus; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public double getInvoiceAmount() { return invoiceAmount; }
    public void setInvoiceAmount(double invoiceAmount) { this.invoiceAmount = invoiceAmount; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
}