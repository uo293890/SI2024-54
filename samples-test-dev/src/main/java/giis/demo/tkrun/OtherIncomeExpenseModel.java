package giis.demo.tkrun;

import giis.demo.util.Database;
import java.util.List;

/**
 * Modelo para manejar datos de "otros ingresos/gastos",
 * mostrando todas las columnas (join) de Type, Event, IncomesExpenses y Movement.
 */
public class OtherIncomeExpenseModel extends Database {

    /**
     * Inserta un nuevo movimiento en la base de datos.
     *
     * 1) Inserta en IncomesExpenses con (event_id, incexp_concept, incexp_amount, incexp_status).
     *    - Para un movimiento con estado "Paid", se usa el paidAmount en lugar del estimatedAmount.
     *    - Para un movimiento con estado "Estimated", se usa el estimatedAmount.
     *
     * 2) Si el movimiento es "Paid", se inserta también en Movement (con la fecha y el monto pagado).
     */
    public void saveMovement(OtherIncomeExpenseDTO dto) {
        // Determinamos el monto que se guarda en IncomesExpenses
        double amountToStore = (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID)
                ? dto.getPaidAmount()
                : dto.getEstimatedAmount();

        // La base de datos tiene un CHECK que requiere 'Estimated' o 'Paid' (tal cual).
        // Mapeamos el enum (ESTIMATED/PAID) a esos valores exactos.
        String dbStatus = (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.ESTIMATED)
                ? "Estimated"
                : "Paid";

        // Insert en IncomesExpenses
        String sqlInsert = """
            INSERT INTO IncomesExpenses (event_id, incexp_concept, incexp_amount, incexp_status)
            VALUES (?, ?, ?, ?)
        """;
        Object[] params = new Object[] {
            dto.getEventId(),
            dto.getConcept(),
            amountToStore,
            dbStatus // "Estimated" o "Paid"
        };
        this.executeUpdate(sqlInsert, params);

        // Obtenemos el ID recién insertado
        List<Object[]> result = this.executeQueryArray("SELECT last_insert_rowid()");
        int newIncexpId = ((Number) result.get(0)[0]).intValue();

        // Si está pagado, insertamos también en Movement
        if (dto.getMovementStatus() == OtherIncomeExpenseDTO.MovementStatus.PAID && dto.getPaidDate() != null) {
            String sqlInsertMovement = """
                INSERT INTO Movement (incexp_id, invoice_id, movement_date, movement_concept, movement_amount)
                VALUES (?, NULL, ?, ?, ?)
            """;
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
     * Devuelve una matriz con todas las columnas del join entre:
     * - IncomesExpenses (i)
     * - Event (e) y Type (t)
     * - Movement (m)
     *
     * Orden de columnas (17):
     * 0.  incexp_id
     * 1.  event_id
     * 2.  event_name
     * 3.  type_id
     * 4.  type_name
     * 5.  event_inidate
     * 6.  event_enddate
     * 7.  event_location
     * 8.  event_status
     * 9.  incexp_concept
     * 10. incexp_amount
     * 11. incexp_status
     * 12. movement_id
     * 13. invoice_id
     * 14. movement_date
     * 15. movement_concept
     * 16. movement_amount
     */
    public Object[][] getAllIncomesExpensesFull() {
        String sql = """
            SELECT i.incexp_id,
                   i.event_id,
                   e.event_name,
                   e.type_id,
                   t.type_name,
                   e.event_inidate,
                   e.event_enddate,
                   e.event_location,
                   e.event_status,
                   i.incexp_concept,
                   i.incexp_amount,
                   i.incexp_status,
                   m.movement_id,
                   m.invoice_id,
                   m.movement_date,
                   m.movement_concept,
                   m.movement_amount
              FROM IncomesExpenses i
              JOIN Event e ON i.event_id = e.event_id
              LEFT JOIN Type t ON e.type_id = t.type_id
              LEFT JOIN Movement m ON i.incexp_id = m.incexp_id
             ORDER BY i.incexp_id
        """;

        List<Object[]> list = this.executeQueryArray(sql);
        Object[][] data = new Object[list.size()][17];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }

    /**
     * Obtiene todos los eventos (id, name) de la tabla Event
     * cuyo estado sea 'Planned' o 'Closed'.
     */
    public Object[][] getAllEvents() {
        String sql = "SELECT event_id, event_name FROM Event WHERE event_status IN ('Planned', 'Closed')";
        List<Object[]> list = this.executeQueryArray(sql);
        Object[][] data = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }
}
