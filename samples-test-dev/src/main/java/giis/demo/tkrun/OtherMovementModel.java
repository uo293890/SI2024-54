package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.util.List;

public class OtherMovementModel {
    private Database db = new Database();

    /**
     * Registers an income or expense movement in the database.
     * @param movement The movement details.
     * @throws Exception If the movement is invalid (e.g., missing payment date for "Paid" status).
     */
    public void registerMovement(OtherMovementDTO movement) throws Exception {
        // Validate the movement
        validateMovement(movement);

        // Insert the movement into the Otherie table
        String sql = "INSERT INTO Otherie (edition_id, otherie_amount, otherie_description, otherie_status) "
                   + "VALUES (?, ?, ?, ?)";
        db.executeUpdate(sql, movement.getEditionId(), movement.getAmount(), movement.getConcept(), movement.getStatus());

        // If the status is "Paid", insert the payment into the Movement table
        if ("Paid".equals(movement.getStatus())) {
            String movementSql = "INSERT INTO Movement (otherie_id, movement_date, movement_concept, movement_amount) "
                              + "VALUES (?, ?, ?, ?)";
            db.executeUpdate(movementSql, getLastInsertedId(), new Date(movement.getPaymentDate().getTime()), movement.getConcept(), movement.getAmount());
        }
    }

    /**
     * Validates the movement.
     * @param movement The movement details.
     * @throws Exception If the movement is invalid.
     */
    private void validateMovement(OtherMovementDTO movement) throws Exception {
        // Fetch the edition details
        String sql = "SELECT edition_inidate FROM Edition WHERE edition_id = ?";
        List<Object[]> result = db.executeQueryArray(sql, movement.getEditionId());

        if (result.isEmpty()) {
            throw new Exception("Edition not found.");
        }

        Date editionStartDate = (Date) result.get(0)[0];

        // Validate the payment date for "Paid" status
        if ("Paid".equals(movement.getStatus())) {
            if (movement.getPaymentDate() == null) {
                throw new Exception("Payment date is required for 'Paid' status.");
            }
            if (movement.getPaymentDate().before(editionStartDate)) {
                throw new Exception("Payment date cannot be before the edition start date.");
            }
        }
    }

    /**
     * Gets the ID of the last inserted record in the Otherie table.
     * @return The last inserted ID.
     */
    private int getLastInsertedId() {
        String sql = "SELECT last_insert_rowid()";
        List<Object[]> result = db.executeQueryArray(sql);
        return (int) result.get(0)[0];
    }
}