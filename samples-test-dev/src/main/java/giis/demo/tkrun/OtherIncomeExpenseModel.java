package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

/**
 * Modelo para manejar datos de otros ingresos/gastos (join de Type, Event, IncomesExpenses, Movement).
 */
public class OtherIncomeExpenseModel extends Database {

    public void saveMovement(OtherIncomeExpenseDTO dto) {
        // Si es PAID, usamos paidAmount; si no, estimatedAmount
        double amountToStore = (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID)
                ? dto.getPaidAmount()
                : dto.getEstimatedAmount();

        // Mapeamos el enum a la forma que la BD espera
        String dbStatus = (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID)
                ? "Paid"
                : "Estimated";

        String sqlInsert = "INSERT INTO IncomesExpenses (event_id, incexp_concept, incexp_amount, incexp_status) " +
                           "VALUES (?, ?, ?, ?)";
        Object[] params = new Object[] {
            dto.getEventId(),
            dto.getConcept(),
            amountToStore,
            dbStatus
        };
        this.executeUpdate(sqlInsert, params);

        // Recuperar el ID recién insertado
        List<Object[]> result = this.executeQueryArray("SELECT last_insert_rowid()");
        int newIncexpId = ((Number) result.get(0)[0]).intValue();

        // Si es PAID, también insertamos en Movement
        if (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID && dto.getPaidDate() != null) {
            String sqlInsertMovement = "INSERT INTO Movement (incexp_id, invoice_id, movement_date, movement_concept, movement_amount) " +
                                       "VALUES (?, NULL, ?, ?, ?)";
            java.sql.Date movementDate = java.sql.Date.valueOf(dto.getPaidDate());
            Object[] paramsMovement = new Object[] {
                newIncexpId,
                movementDate,
                dto.getConcept(),
                dto.getPaidAmount()
            };
            this.executeUpdate(sqlInsertMovement, paramsMovement);
        }
    }

    /**
     * Devuelve los datos (17 columnas) del join de IncomesExpenses + Event + Type + Movement.
     */
    public Object[][] getAllIncomesExpensesFull() {
        String sql = "SELECT i.incexp_id, " +
                     "i.event_id, " +
                     "e.event_name, " +
                     "e.type_id, " +
                     "t.type_name, " +
                     "e.event_inidate, " +
                     "e.event_enddate, " +
                     "e.event_location, " +
                     "e.event_status, " +
                     "i.incexp_concept, " +
                     "i.incexp_amount, " +
                     "i.incexp_status, " +
                     "m.movement_id, " +
                     "m.invoice_id, " +
                     "m.movement_date, " +
                     "m.movement_concept, " +
                     "m.movement_amount " +
                     "FROM IncomesExpenses i " +
                     "JOIN Event e ON i.event_id = e.event_id " +
                     "LEFT JOIN Type t ON e.type_id = t.type_id " +
                     "LEFT JOIN Movement m ON i.incexp_id = m.incexp_id " +
                     "ORDER BY i.incexp_id";

        List<Object[]> list = this.executeQueryArray(sql);
        Object[][] data = new Object[list.size()][17];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }

    /**
     * Devuelve id y nombre de todos los eventos con status 'Planned' o 'Closed'.
     */
    public Object[][] getAllEvents() {
        String sql = "SELECT event_id, event_name FROM Event WHERE event_status IN ('Planned','Closed')";
        List<Object[]> list = this.executeQueryArray(sql);
        Object[][] data = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }
}
