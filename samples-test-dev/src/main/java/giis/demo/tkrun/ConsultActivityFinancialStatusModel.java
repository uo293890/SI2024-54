package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

public class ConsultActivityFinancialStatusModel {
    private Database db = new Database();

    // SQL Queries
    private static final String SQL_GET_EVENTS = 
        "SELECT e.event_id as eventId, e.event_name as eventName, " +
        "e.event_inidate as eventInidate, e.event_enddate as eventEnddate, " +
        "e.event_status as eventStatus, t.type_name as typeName " +
        "FROM Event e JOIN Type t ON e.type_id = t.type_id";

    private static final String SQL_GET_SPONSORS = 
        "SELECT a.agreement_id as agreementId, s.sponsor_name as sponsorName, " +
        "a.agreement_date as agreementDate, a.agreement_amount as agreementAmount, " +
        "a.agreement_status as agreementStatus, l.level_name as levelName " +
        "FROM Agreement a " +
        "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
        "JOIN SpContact sc ON a.spcontact_id = sc.spcontact_id " +
        "JOIN Sponsor s ON sc.sponsor_id = s.sponsor_id " +
        "WHERE l.event_id = ?";

    private static final String SQL_GET_PAID_SPONSORS = 
        "SELECT a.agreement_amount as agreementAmount " +
        "FROM Agreement a " +
        "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
        "WHERE l.event_id = ? AND a.agreement_status = 'Paid'";

    private static final String SQL_GET_INCOMES = 
        "SELECT incexp_id as incexpId, incexp_concept as incexpConcept, " +
        "incexp_amount as incexpAmount, incexp_status as incexpStatus " +
        "FROM IncomesExpenses WHERE event_id = ? AND incexp_amount > 0";

    private static final String SQL_GET_PAID_INCOMES = 
        "SELECT incexp_amount as incexpAmount " +
        "FROM IncomesExpenses " +
        "WHERE event_id = ? AND incexp_amount > 0 AND incexp_status = 'Paid'";

    private static final String SQL_GET_EXPENSES = 
        "SELECT incexp_id as incexpId, incexp_concept as incexpConcept, " +
        "ABS(incexp_amount) as incexpAmount, incexp_status as incexpStatus " +
        "FROM IncomesExpenses WHERE event_id = ? AND incexp_amount < 0";

    private static final String SQL_GET_PAID_EXPENSES = 
        "SELECT ABS(incexp_amount) as incexpAmount " +
        "FROM IncomesExpenses " +
        "WHERE event_id = ? AND incexp_amount < 0 AND incexp_status = 'Paid'";

    public List<ConsultActivityFinancialStatusDTO> getAllEvents() {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_EVENTS);
    }

    public List<ConsultActivityFinancialStatusDTO> getSponsorsForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_SPONSORS, eventId);
    }

    public List<ConsultActivityFinancialStatusDTO> getIncomesForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_INCOMES, eventId);
    }

    public List<ConsultActivityFinancialStatusDTO> getExpensesForEvent(int eventId) {
        return db.executeQueryPojo(ConsultActivityFinancialStatusDTO.class, SQL_GET_EXPENSES, eventId);
    }

    public double getSponsorsPaidForEvent(int eventId) {
        List<ConsultActivityFinancialStatusDTO> results = db.executeQueryPojo(
            ConsultActivityFinancialStatusDTO.class, SQL_GET_PAID_SPONSORS, eventId);
        return results.stream().mapToDouble(ConsultActivityFinancialStatusDTO::getAgreementAmount).sum();
    }

    public double getIncomesPaidForEvent(int eventId) {
        List<ConsultActivityFinancialStatusDTO> results = db.executeQueryPojo(
            ConsultActivityFinancialStatusDTO.class, SQL_GET_PAID_INCOMES, eventId);
        return results.stream().mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount).sum();
    }

    public double getExpensesPaidForEvent(int eventId) {
        List<ConsultActivityFinancialStatusDTO> results = db.executeQueryPojo(
            ConsultActivityFinancialStatusDTO.class, SQL_GET_PAID_EXPENSES, eventId);
        return results.stream().mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount).sum();
    }

    public double getSponsorsEstimatedForEvent(int eventId) {
        return getSponsorsForEvent(eventId).stream()
            .mapToDouble(ConsultActivityFinancialStatusDTO::getAgreementAmount)
            .sum();
    }

    public double getIncomesEstimatedForEvent(int eventId) {
        return getIncomesForEvent(eventId).stream()
            .mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount)
            .sum();
    }

    public double getExpensesEstimatedForEvent(int eventId) {
        return getExpensesForEvent(eventId).stream()
            .mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount)
            .sum();
    }
}