package giis.demo.tkrun;

import giis.demo.util.Database; // Assuming this utility class is available

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Model for managing Financial Report data from events, agreements, invoices,
 * and other incomes/expenses and their movements in the database.
 * Retrieves data for financial reporting, combining sponsorship and other income/expenses.
 */
public class FinancialReportModel {

    // Database connection handled by the utility class instance
    private Database db; // Declare the instance variable

    // Explicit instance initialization block
    {
        try {
            db = new Database(); // Initialize the instance in a block
        } catch (Exception e) {
            // Handle potential exceptions during Database initialization
            // Depending on your Database class, it might throw SQLException or other exceptions
            System.err.println("Error initializing Database connection: " + e.getMessage());
            e.printStackTrace();
            // Optionally, re-throw as a RuntimeException if the application cannot proceed without a database
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    // Constructor might take Database instance if it's managed externally
    // public FinancialReportModel(Database db) { this.db = db; }

    /**
     * Retrieves financial report data for events within a specified date range and status.
     * Combines Sponsorship Income (from Agreements/Invoices) with Other Income/Expenses
     * (from IncomesExpenses table and their Movements) into estimated and actual totals.
     *
     * @param startDate The start date of the report interval (yyyy-MM-dd). Required.
     * @param endDate The end date of the report interval (yyyy-MM-dd). Required.
     * @param status The status to filter events by ("All", "Planned", "Open", "Closed"). Required.
     * @return A list of FinancialReportDTO objects, one for each matching event.
     * @throws SQLException if a database access error occurs.
     * @throws NullPointerException if startDate, endDate, or status are null.
     * @throws IllegalArgumentException if startDate or endDate are empty (after trim).
     */
    public List<FinancialReportDTO> getFinancialReport(String startDate, String endDate, String status) throws SQLException {

        // Basic null/empty validation for required parameters
        Objects.requireNonNull(startDate, "startDate cannot be null");
        Objects.requireNonNull(endDate, "endDate cannot be null");
        Objects.requireNonNull(status, "status cannot be null");

        if (startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
             throw new IllegalArgumentException("Start and End dates cannot be empty.");
        }

        boolean isAllStatus = "All".equalsIgnoreCase(status.trim());

        StringBuilder sql = new StringBuilder();
        // Selecting event details
        sql.append("SELECT e.event_id AS event_id, e.event_name AS name, e.event_status AS status, ")
           .append("e.event_inidate AS start_date, e.event_enddate AS end_date, ");

        // --- Estimated Income: SUM(Agreement Amounts) + SUM(IncomesExpenses Amounts > 0) ---
        // Sum of all agreement amounts linked to the event (Estimated Sponsorship)
        // PLUS sum of positive amounts from IncomesExpenses linked to the event (Estimated Other Income)
        sql.append("(")
           .append("COALESCE((SELECT SUM(a.agreement_amount) ")
           .append("          FROM Agreement a ")
           .append("          JOIN LevelOfSponsorship l ON a.level_id = l.level_id ")
           .append("          WHERE l.event_id = e.event_id), 0.0) ")
           .append(" + ")
           // Sum of positive amounts from IncomesExpenses for this event (Estimated Other Income)
           .append("COALESCE((SELECT SUM(ie.incexp_amount) ")
           .append("          FROM IncomesExpenses ie ")
           .append("          WHERE ie.event_id = e.event_id AND ie.incexp_amount > 0), 0.0) ")
           .append(") AS estimated_income, ");


        // --- Estimated Expenses: SUM(ABS(incexp_amount) for negative IncomesExpenses linked to event ---
        // Sum of absolute values of negative amounts from IncomesExpenses linked to the event (Estimated Expenses)
        sql.append("COALESCE((SELECT SUM(ABS(ie.incexp_amount)) ") // Use ABS to report expenses as positive
           .append("          FROM IncomesExpenses ie ")
           .append("          WHERE ie.event_id = e.event_id AND ie.incexp_amount < 0), 0.0) AS estimated_expenses, ");


        // --- Actual Income: SUM(Agreement Amounts from Sent Invoices) + SUM(Movement Amounts for Income Entries) ---
        // Sum of agreement amounts for invoices that have been sent (Invoice Date is not NULL) (Actual Sponsorship Income)
        // PLUS sum of movement amounts for IncomesExpenses entries where the original estimated amount was >= 0 (Actual Other Income)
        sql.append("(")
           .append("COALESCE((SELECT SUM(a.agreement_amount) ") // Actual Sponsorship Income (from SENT Invoices) - based on AGREEMENT amount
           .append("          FROM Invoice i ")
           .append("          JOIN Agreement a ON i.agreement_id = a.agreement_id ")
           .append("          JOIN LevelOfSponsorship l ON a.level_id = l.level_id ")
           .append("          WHERE l.event_id = e.event_id AND i.invoice_date IS NOT NULL), 0.0) ") // Filter for sent invoices
           .append(" + ")
           // Sum of movement amounts for IncomesExpenses entries linked to this event, where the original entry was Income (incexp_amount >= 0)
           // Need to join from Event -> IncomesExpenses -> Movement
           .append("COALESCE((SELECT SUM(m.movement_amount) ") // Sum of movements
           .append("          FROM IncomesExpenses ie_m ") // Alias for IncomesExpenses when joining Movement
           .append("          JOIN Movement m ON ie_m.incexp_id = m.incexp_id ")
           .append("          WHERE ie_m.event_id = e.event_id AND ie_m.incexp_amount >= 0), 0.0) ") // Filter for movements on Income entries
           .append(") AS actual_income, ");


        // --- Actual Expenses: SUM(ABS(Movement Amounts for Expense Entries)) ---
        // Sum of absolute values of movement amounts for IncomesExpenses entries where the original estimated amount was < 0 (Actual Expenses)
        // Need to join from Event -> IncomesExpenses -> Movement
        sql.append("COALESCE((SELECT SUM(ABS(m.movement_amount)) ") // Sum of absolute movements
           .append("          FROM IncomesExpenses ie_m ") // Alias for IncomesExpenses when joining Movement
           .append("          JOIN Movement m ON ie_m.incexp_id = m.incexp_id ")
           .append("          WHERE ie_m.event_id = e.event_id AND ie_m.incexp_amount < 0), 0.0) AS actual_expenses "); // Filter for movements on Expense entries


        sql.append("FROM Event e ");

        List<Object> params = new ArrayList<>();

        // Add WHERE clause for event status and date range
        sql.append("WHERE e.event_inidate BETWEEN ? AND ? "); // Filter events by start date range
        params.add(startDate.trim()); // Use trimmed dates
        params.add(endDate.trim());

        // Add status filter if not "All"
        if (!isAllStatus) {
            sql.append("AND e.event_status = ? ");
            params.add(status.trim()); // Use trimmed status
        }

        sql.append("ORDER BY e.event_inidate"); // Order by event start date

        // Execute the query using executeQueryMap which returns List<Map<String, Object>>
        // The Database utility method might throw SQLException or wrap it
        // Use the initialized db instance
        List<Map<String, Object>> rows = db.executeQueryMap(sql.toString(), params.toArray());

        List<FinancialReportDTO> reports = new ArrayList<>();
        // Iterate through the results and create DTOs
        for (Map<String, Object> row : rows) {
            reports.add(new FinancialReportDTO(
                 String.valueOf(row.get("name")),
                 String.valueOf(row.get("status")),
                 String.valueOf(row.get("start_date")),
                 String.valueOf(row.get("end_date")),
                 toDouble(row.get("estimated_income")), // Combined Estimated Income (Sponsorship + Other)
                 toDouble(row.get("estimated_expenses")), // Total Estimated Expenses
                 toDouble(row.get("actual_income")), // Combined Actual Income (Sponsorship + Other - based on MOVEMENTS)
                 toDouble(row.get("actual_expenses")) // Total Actual Expenses (based on MOVEMENTS)
            ));
        }

        return reports;
    }

    /**
     * Helper method to safely convert database result objects to double.
     * Handles various Number types and String representations.
     * Returns 0.0 if the object is null or cannot be parsed.
     *
     * @param obj The object from the database result.
     * @return The value as a double, or 0.0 if conversion fails.
     */
    private double toDouble(Object obj) {
        if (obj == null) {
            return 0.0;
        }
        try {
            // Handle different Number types that databases might return (Integer, Long, BigDecimal, etc.)
            if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            }
            // Attempt to parse String representation
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            System.err.println("Warning: Could not parse object to double: " + obj + " - " + e.getMessage());
            return 0.0; // Return 0.0 on parse failure
        }
    }

    
}