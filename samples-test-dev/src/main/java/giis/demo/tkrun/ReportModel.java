package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.util.List;

public class ReportModel {
    private Database db = new Database();

    /**
     * Obtiene el reporte financiero de las ediciones (actividades) dentro de un intervalo de fechas y
     * con un estado determinado.
     *
     * La consulta realiza lo siguiente:
     * - Selecciona los datos básicos de la edición: título, fecha de inicio, fecha de fin y estado.
     * - Calcula el ingreso estimado a partir de:
     *     * La suma de los montos de los acuerdos (Agreement.agreement_amount).
     *     * La suma de los otros ingresos estimados (valores positivos de Otherie.otherie_amount).
     * - Calcula el gasto estimado a partir de:
     *     * La suma de los otros gastos estimados (valor absoluto de los valores negativos de Otherie.otherie_amount).
     * - Obtiene el ingreso pagado a partir de las facturas (Invoice.invoice_vat) asociadas a los acuerdos de la edición.
     * - Obtiene el gasto pagado a partir de los movimientos (Movement.movement_amount) asociados a los registros de Otherie.
     *
     * Se aplican los filtros por fecha (edition_inidate) y por estado de la actividad.
     *
     * @param startDate Fecha de inicio para filtrar las ediciones.
     * @param endDate Fecha de fin para filtrar las ediciones.
     * @param status Estado de la edición a filtrar (o 'Todos' para no filtrar por estado).
     * @return Lista de ReportDTO con los datos financieros por cada edición.
     */
    public List<ReportDTO> getFinancialReport(Date startDate, Date endDate, String status) {
        String sql = "SELECT " +
                     "  e.edition_title AS editionTitle, " +
                     "  e.edition_inidate AS editionStartDate, " +
                     "  e.edition_enddate AS editionEndDate, " +
                     "  e.edition_status AS editionStatus, " +
                     // Ingresos estimados por acuerdos
                     "  COALESCE(SUM(a.agreement_amount), 0) AS totalEstimatedAgreement, " +
                     // Otros ingresos estimados: suma de valores positivos en Otherie
                     "  COALESCE(SUM(CASE WHEN o.otherie_amount > 0 THEN o.otherie_amount ELSE 0 END), 0) AS totalEstimatedOtherIncome, " +
                     // Otros gastos estimados: suma de valores negativos (convertidos a positivos) en Otherie
                     "  COALESCE(SUM(CASE WHEN o.otherie_amount < 0 THEN -o.otherie_amount ELSE 0 END), 0) AS totalEstimatedOtherExpenses, " +
                     // Ingresos pagados: obtenidos de las facturas asociadas a los acuerdos de la edición
                     "  (SELECT COALESCE(SUM(i.invoice_vat), 0) " +
                     "     FROM Invoice i " +
                     "     INNER JOIN Agreement a2 ON i.agreement_id = a2.agreement_id " +
                     "     WHERE a2.edition_id = e.edition_id) AS totalPaidIncome, " +
                     // Gastos pagados: obtenidos de los movimientos asociados a registros de Otherie de la edición
                     "  (SELECT COALESCE(SUM(m.movement_amount), 0) " +
                     "     FROM Movement m " +
                     "     WHERE m.otherie_id IN (SELECT o2.otherie_id FROM Otherie o2 WHERE o2.edition_id = e.edition_id)) AS totalPaidExpenses " +
                     "FROM Edition e " +
                     "LEFT JOIN Agreement a ON e.edition_id = a.edition_id " +
                     "LEFT JOIN Otherie o ON e.edition_id = o.edition_id " +
                     "WHERE e.edition_inidate BETWEEN ? AND ? " +
                     "AND (? = 'Todos' OR e.edition_status = ?) " +
                     "GROUP BY e.edition_id";
        return db.executeQueryPojo(ReportDTO.class, sql, startDate, endDate, status, status);
    }
}
