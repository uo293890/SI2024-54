package giis.demo.tkrun;

import giis.demo.util.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Map;

/**
 * Model for managing Incomes/Expenses entries and their Movements in the database.
 * Handles data retrieval, insertion, and status updates for Other Income/Expense entries and their movements.
 * Now includes methods to create new estimated entries and retrieve events, and filters by event.
 */
public class OtherIncomeExpenseModel extends Database {

    // Constructor might take Database instance if it's managed externally
    // public OtherIncomeExpenseModel(Database db) { this.db = db; }

    /**
     * Retrieves Income/Expense entries and their total paid amount, filtered by status and optionally by event.
     * Joins IncomesExpenses, Event, and Movement tables.
     *
     * @param allowedStatuses List of statuses to include, or null/empty for default statuses.
     * @param eventIdFilter   Optional Event ID to filter by, or null to include all events.
     * @return List of OtherIncomeExpenseDTO summary objects.
     * @throws SQLException if a database error occurs.
     */
    public List<OtherIncomeExpenseDTO> getAllIncomesExpensesWithTotalPaid(List<String> allowedStatuses, Integer eventIdFilter) throws SQLException {
        String sql = "SELECT ie.incexp_id, ie.event_id, e.event_name, ie.incexp_concept, ie.incexp_amount, ie.incexp_status, " +
                     "COALESCE(SUM(m.movement_amount), 0.0) AS total_paid " + // COALESCE handles entries with no movements
                     "FROM IncomesExpenses ie " +
                     "JOIN Event e ON ie.event_id = e.event_id " +
                     "LEFT JOIN Movement m ON ie.incexp_id = m.incexp_id "; // Left join to include entries with no movements

        List<String> whereClauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        // --- Add Status Filter ---
        List<String> statusesToFilter = (allowedStatuses != null && !allowedStatuses.isEmpty()) ? allowedStatuses : getAllRelevantStatusesForQuery();
        String inClause = statusesToFilter.stream()
                                          .map(s -> "'" + s.replace("'", "''") + "'") // Escape quotes and wrap in single quotes
                                          .collect(Collectors.joining(","));

        if (!inClause.isEmpty()) {
             whereClauses.add("ie.incexp_status IN (" + inClause + ")");
        } else {
             // If the list of statuses is empty, add a false condition to return no rows
             whereClauses.add("1 = 0");
        }

        // --- Add Event Filter ---
        if (eventIdFilter != null) {
            whereClauses.add("e.event_id = ?");
            params.add(eventIdFilter);
        }

        // Combine WHERE clauses
        if (!whereClauses.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", whereClauses);
        }


        // Add grouping and ordering
        sql += " GROUP BY ie.incexp_id, ie.event_id, e.event_name, ie.incexp_concept, ie.incexp_amount, ie.incexp_status " +
               " ORDER BY e.event_name, ie.incexp_id"; // Order by event name, then entry ID


        // Execute the query
        List<Object[]> rows = this.executeQueryArray(sql, params.toArray());

        List<OtherIncomeExpenseDTO> summaryList = new ArrayList<>();
        // Map results to DTOs
        for (Object[] row : rows) {
            try {
                 // Safely cast and convert data from the row
                 int incexpId = ((Number) Objects.requireNonNull(row[0], "incexp_id is null")).intValue();
                 int eventId = ((Number) Objects.requireNonNull(row[1], "event_id is null")).intValue();
                 String eventName = (String) Objects.requireNonNull(row[2], "event_name is null");
                 String concept = (String) Objects.requireNonNull(row[3], "incexp_concept is null"); // Assuming concept is not null or empty string is acceptable
                 double estimatedAmount = ((Number) Objects.requireNonNull(row[4], "incexp_amount is null")).doubleValue();
                 String status = (String) Objects.requireNonNull(row[5], "incexp_status is null");
                 // total_paid comes from SUM, which COALESCE turns into 0.0 if no movements, so it's never null here
                 double totalPaid = ((Number) Objects.requireNonNull(row[6], "total_paid is null")).doubleValue();


                summaryList.add(new OtherIncomeExpenseDTO(
                    incexpId, eventId, eventName, concept, estimatedAmount, status, totalPaid
                ));
            } catch (NullPointerException | ClassCastException e) {
                 // Log or handle unexpected data row structure/types
                 System.err.println("Skipping row due to unexpected data structure or types: " + (row == null ? "null" : java.util.Arrays.toString(row)) + " - " + e.getMessage());
            }
        }
        return summaryList;
    }

    /**
     * Gets the list of all relevant status strings used by the controller and view filters.
     * Should match possible values in the IncomesExpenses.incexp_status column.
     */
    public List<String> getAllRelevantStatusesForQuery() {
        List<String> all = new ArrayList<>();
        all.add("Estimated");
        all.add("Open");
        all.add("Closed");
        all.add("Partial Paid");
        all.add("Paid");
        return all;
    }

    /**
     * Retrieves a list of all events (ID and Name) for populating a selection component.
     * @return List of Maps, where each Map contains "event_id" (Integer) and "event_name" (String).
     * @throws SQLException if a database error occurs.
     */
    public List<Map<String, Object>> getAllEvents() throws SQLException {
        String sql = "SELECT event_id, event_name FROM Event ORDER BY event_name";
        // Use executeQueryMap to get results as List<Map>
        return this.executeQueryMap(sql);
    }


    /**
     * Retrieves all Movement records for a specific entry, ordered by date.
     *
     * @throws SQLException if a database error occurs.
     */
    public List<Object[]> getMovementsByIncexpId(int incexpId) throws SQLException {
        String sql = "SELECT m.movement_date, m.movement_amount, m.movement_concept " +
                     "FROM Movement m " +
                     "WHERE m.incexp_id = ? " + // Filter by incexp_id
                     "ORDER BY m.movement_date";

        return this.executeQueryArray(sql, incexpId);
    }

    /**
     * Inserts a new Movement record linked to an entry.
     * invoice_id is set to NULL for these movements.
     *
     * @param incexpId The ID of the entry.
     * @param movementDate Date of the movement (yyyy-MM-dd string).
     * @param concept Description of the movement (can be null or empty).
     * @param amount Amount of the movement (positive for receipt, negative for payment out relative to the *entry type* - controller handles sign).
     * @throws SQLException if a database error occurs.
     */
    public void insertMovement(int incexpId, String movementDate, String concept, double amount) throws SQLException {
        // Pass the concept string directly; Database class needs to handle inserting null/empty
        String conceptToStore = (concept == null || concept.trim().isEmpty()) ? null : concept.trim(); // Store null if empty/blank

        String sql = "INSERT INTO Movement (incexp_id, invoice_id, movement_date, movement_concept, movement_amount) " +
                     "VALUES (?, NULL, ?, ?, ?)"; // invoice_id is NULL for these movements

        Object[] params = new Object[]{
            incexpId,
            movementDate,
            conceptToStore, // This can be null
            amount
        };

        this.executeUpdate(sql, params);
    }

    /**
     * Inserts a new estimated IncomesExpenses entry.
     * Initial status is 'Estimated'.
     *
     * @param eventId The ID of the event the entry belongs to.
     * @param concept The concept for the new entry.
     * @param amount The estimated amount for the entry (can be positive for income, negative for expense).
     * @throws SQLException if a database error occurs.
     */
    public void insertIncomesExpensesEntry(int eventId, String concept, double amount) throws SQLException {
        // Ensure concept is not null if DB column is not nullable, pass empty string or throw error if needed
        // Assuming incexp_concept is nullable or allows empty string based on schema.
         String conceptToStore = (concept == null || concept.trim().isEmpty()) ? null : concept.trim(); // Store null if empty/blank


        String sql = "INSERT INTO IncomesExpenses (event_id, incexp_concept, incexp_amount, incexp_status) " +
                     "VALUES (?, ?, ?, 'Estimated')"; // New entries start with 'Estimated' status

        Object[] params = new Object[]{
            eventId,
            conceptToStore, // This can be null
            amount
        };

        this.executeUpdate(sql, params);
    }


    /**
     * Gets the original estimated amount for an entry.
     *
     * @throws SQLException if database error or entry not found or data type is unexpected.
     */
    public double getIncexpEstimatedAmount(int incexpId) throws SQLException {
        String sql = "SELECT incexp_amount FROM IncomesExpenses WHERE incexp_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, incexpId);

        if (res == null || res.isEmpty() || res.get(0) == null || res.get(0).length == 0 || res.get(0)[0] == null) {
             throw new SQLException("Entry with ID " + incexpId + " not found or estimated amount is null.");
        }
         Object amountObj = res.get(0)[0];
         if (amountObj instanceof Number) {
             return ((Number) amountObj).doubleValue();
         } else {
             throw new SQLException("Unexpected data type for estimated amount for ID " + incexpId + ": " + (amountObj != null ? amountObj.getClass().getName() : "null"));
         }
    }

    /**
     * Calculates the total sum of movement amounts for an entry.
     *
     * @throws SQLException if a database error occurs.
     */
    public double getIncexpTotalPaid(int incexpId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(m.movement_amount), 0.0) FROM Movement m " +
                     "WHERE m.incexp_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, incexpId);

        if (res == null || res.isEmpty() || res.get(0) == null || res.get(0).length == 0 || res.get(0)[0] == null) {
            return 0.0; // COALESCE should prevent this, but added for safety
        }
        Object sumObj = res.get(0)[0];
        if (sumObj instanceof Number) {
            return ((Number) sumObj).doubleValue();
        } else {
            throw new SQLException("Unexpected data type for total paid amount for ID " + incexpId + ": " + (sumObj != null ? sumObj.getClass().getName() : "null"));
        }
    }

     /**
      * Gets the current status of a specific IncomesExpenses entry from the database.
      * Used to check the status before potentially updating it based on payments.
      * @throws SQLException if a database error occurs or entry not found.
      */
     public String getIncexpStatus(int incexpId) throws SQLException {
         String sql = "SELECT incexp_status FROM IncomesExpenses WHERE incexp_id = ?";
         List<Object[]> res = this.executeQueryArray(sql, incexpId);

         if (res == null || res.isEmpty() || res.get(0) == null || res.get(0).length == 0 || res.get(0)[0] == null) {
             throw new SQLException("Entry with ID " + incexpId + " not found.");
         }
          Object statusObj = res.get(0)[0];
          if (statusObj instanceof String) {
              return (String) statusObj;
          } else {
              throw new SQLException("Unexpected data type for status for ID " + incexpId + ": " + (statusObj != null ? statusObj.getClass().getName() : "null"));
          }
     }

    /**
     * Updates the status of an entry.
     *
     * @throws SQLException if a database error occurs.
     */
    public void updateIncexpStatus(int incexpId, String newStatus) throws SQLException {
        String sql = "UPDATE IncomesExpenses SET incexp_status = ? WHERE incexp_id = ?";
        this.executeUpdate(sql, newStatus, incexpId);
    }

    // Method to get event name by ID (if needed for displaying selected event in creation section)
    // public String getEventNameById(int eventId) throws SQLException { ... }
}