package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class ConsultActivityFinancialStatusModel {
    private Database db = new Database();

    // SQL Queries with proper column aliases
    private static final String SQL_GET_EVENTS = 
        "SELECT e.event_id AS eventId, e.event_name AS eventName, " +
        "e.event_inidate AS eventInidate, e.event_enddate AS eventEnddate, " +
        "e.event_status AS eventStatus, t.type_name AS typeName " +
        "FROM Event e JOIN Type t ON e.type_id = t.type_id";
    
    private static final String SQL_GET_SPONSORSHIPS = 
        "SELECT a.agreement_id AS agreementId, s.sponsor_name AS sponsorName, " +
        "a.agreement_date AS agreementDate, a.agreement_amount AS agreementAmount, " +
        "a.agreement_status AS agreementStatus, l.level_name AS levelName " +
        "FROM Agreement a " +
        "JOIN SpContact sc ON a.spcontact_id = sc.spcontact_id " +
        "JOIN Sponsor s ON sc.sponsor_id = s.sponsor_id " +
        "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
        "WHERE l.event_id = ?";
    
    private static final String SQL_GET_INVOICES = 
        "SELECT i.invoice_id AS invoiceId, i.invoice_date AS invoiceDate, " +
        "i.invoice_number AS invoiceNumber, a.agreement_amount AS agreementAmount, " +
        "a.agreement_status AS agreementStatus " +
        "FROM Invoice i " +
        "JOIN Agreement a ON i.agreement_id = a.agreement_id " +
        "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
        "WHERE l.event_id = ?";
    
    private static final String SQL_GET_INCOMES = 
        "SELECT incexp_id AS incexpId, incexp_concept AS incexpConcept, " +
        "incexp_amount AS incexpAmount, incexp_status AS incexpStatus " +
        "FROM IncomesExpenses " +
        "WHERE event_id = ? AND incexp_amount > 0";
    
    private static final String SQL_GET_EXPENSES = 
        "SELECT incexp_id AS incexpId, incexp_concept AS incexpConcept, " +
        "ABS(incexp_amount) AS incexpAmount, incexp_status AS incexpStatus " +
        "FROM IncomesExpenses " +
        "WHERE event_id = ? AND incexp_amount < 0";

    public List<ConsultActivityFinancialStatusDTO> getAllEvents() {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_EVENTS);
    }

    public List<ConsultActivityFinancialStatusDTO> getSponsorshipsForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_SPONSORSHIPS, eventId);
    }

    public List<ConsultActivityFinancialStatusDTO> getInvoicesForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_INVOICES, eventId);
    }

    public List<ConsultActivityFinancialStatusDTO> getIncomesForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_INCOMES, eventId);
    }

    public List<ConsultActivityFinancialStatusDTO> getExpensesForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_EXPENSES, eventId);
    }

    public double[] calculateTotals(int eventId) {
        // Sponsorships (only count paid ones as actual income)
        double sponsorshipIncome = getSponsorshipsForEvent(eventId).stream()
            .filter(dto -> "Paid".equals(dto.getAgreementStatus()))
            .mapToDouble(ConsultActivityFinancialStatusDTO::getAgreementAmount)
            .sum();
        
        // Other incomes (ticket sales, etc.)
        double otherIncome = getIncomesForEvent(eventId).stream()
            .filter(dto -> "Paid".equals(dto.getIncexpStatus()))
            .mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount)
            .sum();
        
        // Expenses (convert to positive for calculation)
        double expenses = getExpensesForEvent(eventId).stream()
            .filter(dto -> "Paid".equals(dto.getIncexpStatus()))
            .mapToDouble(dto -> Math.abs(dto.getIncexpAmount()))
            .sum();
        
        double totalIncome = sponsorshipIncome + otherIncome;
        double totalExpenses = expenses;
        double balance = totalIncome - totalExpenses;
        
        return new double[]{totalIncome, totalExpenses, balance};
    }
}