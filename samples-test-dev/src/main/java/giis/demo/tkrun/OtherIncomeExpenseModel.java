package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Model class for handling "Other Income/Expense" movements.
 * <p>
 * This model saves the details of a movement into the IncomesExpenses table.
 * For PAID movements, the paid date and paid amount are stored.
 * For ESTIMATED movements, these values remain null.
 * </p>
 */
public class OtherIncomeExpenseModel extends Database {

    /**
     * Saves a new income/expense movement to the database.
     *
     * @param dto The DTO containing the movement details.
     */
    public void saveMovement(OtherIncomeExpenseDTO dto) {
        String sql = """
            INSERT INTO IncomesExpenses 
                (event_id, incexp_concept, incexp_amount, incexp_status, paid_date, paid_amount)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        // For PAID movements, set the paid date; for ESTIMATED, leave as null.
        Date paidDate = null;
        if (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID && dto.getPaidDate() != null) {
            paidDate = Date.valueOf(dto.getPaidDate());
        }

        // For a PAID movement, record the paid amount; for ESTIMATED, it can be left null.
        Object[] params = new Object[] {
            dto.getEventId(),
            dto.getConcept(),
            dto.getEstimatedAmount(),  // Initially, the estimated amount is recorded.
            dto.getMovementStatus().name(), // Must be "Estimated" or "Paid"
            paidDate,
            (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID) ? dto.getPaidAmount() : null
        };

        // Execute the update statement.
        this.executeUpdate(sql, params);
    }
}
