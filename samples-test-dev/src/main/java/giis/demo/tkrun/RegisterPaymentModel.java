package giis.demo.tkrun;

import java.util.List; // Necesario para usar listas (como los resultados de las consultas)
import giis.demo.util.Database; // Heredamos de esta clase para tener métodos de acceso a base de datos

/**
 * Esta clase representa el MODELO (M en MVC) para la funcionalidad de registro de pagos.
 * Se encarga de toda la lógica de acceso y manipulación de datos relacionada con
 * los acuerdos (agreements) y los movimientos de pago (movements) en la base de datos.
 * Hereda de 'Database' para usar sus métodos de ejecución de consultas SQL.
 */
public class RegisterPaymentModel extends Database {

    /**
     * Recupera una lista de acuerdos basándose en su estado actual.
     * @param status El estado del acuerdo que se busca (ej: "Agreed", "Paid").
     * @return Una lista de arrays de Object, donde cada array es una fila de resultados de la consulta.
     * Las columnas corresponden a: agreement_id, sponsor_name, spcontact_name, agreement_amount, agreement_status.
     */
    public List<Object[]> getAgreementsByStatus(String status) {
        // Definimos la consulta SQL para obtener los datos necesarios.
        // Unimos las tablas Agreement, SpContact y Sponsor para obtener nombres relacionados.
        // Filtramos por el estado del acuerdo proporcionado como parámetro.
        // Mantenemos 'agreement_id' como la primera columna aunque se oculte en la vista,
        // porque el controlador lo necesita internamente.
        String sql = "SELECT a.agreement_id, s.sponsor_name, c.spcontact_name, a.agreement_amount, a.agreement_status " +
                     "FROM Agreement a " +
                     "JOIN SpContact c ON a.spcontact_id = c.spcontact_id " +
                     "JOIN Sponsor s ON c.sponsor_id = s.sponsor_id " +
                     "WHERE a.agreement_status = ?"; // El '?' es un placeholder para el parámetro 'status'
        // Ejecutamos la consulta usando el método heredado 'executeQueryArray'
        // Pasamos la consulta SQL y un array con los valores para los placeholders (?)
        return this.executeQueryArray(sql, new Object[]{ status });
    }

    /**
     * Recupera el historial de movimientos de pago registrados para un acuerdo específico.
     * @param agreementId El ID del acuerdo para el que se buscan los pagos.
     * @return Una lista de arrays de Object, donde cada array representa un movimiento de pago:
     * movement_date, movement_amount, movement_concept.
     */
    public List<Object[]> getPreviousPayments(int agreementId) {
        // Consulta SQL para obtener los movimientos de la tabla Movement.
        // Unimos con la tabla Invoice para poder filtrar por el agreement_id asociado a esa factura.
        // Asumimos que movement_concept puede ser nulo o vacío en la base de datos.
        String sql = "SELECT m.movement_date, m.movement_amount, m.movement_concept " +
                     "FROM Movement m " +
                     "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "WHERE i.agreement_id = ?"; // Filtramos por el ID del acuerdo
        // Ejecutamos la consulta. El 'agreementId' se usa para reemplazar el '?'
        return this.executeQueryArray(sql, agreementId);
    }

    /**
     * Obtiene la cantidad total esperada (el importe acordado) para un acuerdo.
     * @param agreementId El ID del acuerdo.
     * @return El importe esperado como un double. Devuelve 0.0 si el acuerdo no existe o no tiene importe.
     */
    public double getAgreementExpectedAmount(int agreementId) {
        // Consulta SQL simple para obtener el importe del acuerdo por su ID.
        String sql = "SELECT agreement_amount FROM Agreement WHERE agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        // Procesamos el resultado:
        // - Verificamos si la lista de resultados está vacía (acuerdo no encontrado).
        // - Verificamos si el primer elemento (el importe) es nulo (podría pasar en DB).
        // - Si está vacío o es nulo, devolvemos 0.0.
        // - Si hay resultado, casteamos el valor (que viene como Object) a un tipo numérico
        //   (Number) y luego a double. Usar Number es más seguro que castear directamente a
        //   Double o BigDecimal ya que la DB podría devolver Integer, Long, etc.
        return res.isEmpty() || res.get(0)[0] == null ? 0.0 : ((Number) res.get(0)[0]).doubleValue();
    }

    /**
     * Calcula la suma total de todos los pagos registrados para un acuerdo específico.
     * @param agreementId El ID del acuerdo.
     * @return La suma total pagada como un double. Devuelve 0.0 si no hay pagos registrados.
     */
    public double getAgreementTotalPaid(int agreementId) {
        // Consulta SQL para sumar los importes (movement_amount) de todos los movimientos
        // asociados a la factura ligada al acuerdo.
        // COALESCE(SUM(m.movement_amount), 0.0) es crucial: si no hay *ningún* movimiento
        // para este acuerdo (la subconsulta de SUM no encuentra filas), SUM devolvería NULL.
        // COALESCE(NULL, 0.0) lo convierte en 0.0, evitando un NullPointerException.
        String sql = "SELECT COALESCE(SUM(m.movement_amount), 0.0) FROM Movement m " +
                     "JOIN Invoice i ON m.invoice_id = i.invoice_id " +
                     "WHERE i.agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        // Procesamos el resultado. Similar a getAgreementExpectedAmount, pero aquí
        // el COALESCE ya nos asegura que no será nulo si no hay pagos, pero igual
        // verificamos si la lista de resultados está vacía (aunque con SUM y COALESCE
        // en teoría siempre debería devolver una fila con 0.0 o la suma).
        return res.isEmpty() || res.get(0)[0] == null ? 0.0 : ((Number) res.get(0)[0]).doubleValue();
    }

    /**
     * Obtiene el ID de la factura asociada a un acuerdo específico.
     * @param agreementId El ID del acuerdo.
     * @return El ID de la factura como int, o -1 si no se encuentra una factura para ese acuerdo.
     */
    public int getInvoiceIdByAgreement(int agreementId) {
        // Consulta SQL para seleccionar el ID de la factura.
        // Filtramos la tabla Invoice por el ID del acuerdo asociado.
        String sql = "SELECT invoice_id FROM Invoice WHERE agreement_id = ?";
        List<Object[]> res = this.executeQueryArray(sql, agreementId);
        // Procesamos el resultado:
        // - Si la lista está vacía (no hay factura asociada), devolvemos -1.
        // - Si hay resultado, casteamos el valor a int.
        return res.isEmpty() ? -1 : ((Number) res.get(0)[0]).intValue();
    }

    /**
     * Inserta un nuevo movimiento de pago en la base de datos.
     * @param dto Un objeto RegisterPaymentDTO que contiene todos los detalles del pago (factura, fecha, concepto, importe).
     */
    public void insertPayment(RegisterPaymentDTO dto) {
        // Consulta SQL para insertar un nuevo registro en la tabla Movement.
        // Usamos placeholders (?) para los valores que vienen en el DTO.
        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) VALUES (?, ?, ?, ?)";
        // Ejecutamos la actualización (INSERT) usando el método heredado 'executeUpdate'.
        // Le pasamos la consulta SQL y los valores del DTO en el orden correcto.
        // La clase Database heredada debe manejar correctamente la inserción de un String que podría ser null.
        this.executeUpdate(sql, dto.getInvoiceId(), dto.getPaymentDate(), dto.getConcept(), dto.getAmount());
    }

    /**
     * Actualiza el estado (status) de un acuerdo específico en la base de datos.
     * Es un método genérico para cambiar el estado a cualquier valor.
     * @param agreementId El ID del acuerdo a actualizar.
     * @param status El nuevo estado para el acuerdo (ej: "Agreed", "Paid").
     */
     public void updateAgreementStatus(int agreementId, String status) {
         // Consulta SQL para actualizar la columna agreement_status de la tabla Agreement.
         // Filtramos por el ID del acuerdo para asegurar que solo se actualiza el correcto.
         String sql = "UPDATE Agreement SET agreement_status = ? WHERE agreement_id = ?";
         // Ejecutamos la actualización (UPDATE).
         this.executeUpdate(sql, status, agreementId);
     }

    /**
     * Método para actualizar el estado de un acuerdo específicamente a "Paid".
     * Nota: Según los comentarios originales, este método ya no se usa directamente en el controlador,
     * pero se mantiene aquí. Llama al método más genérico 'updateAgreementStatus'.
     * @param agreementId El ID del acuerdo a marcar como pagado.
     */
     public void updateAgreementStatusToPaid(int agreementId) {
         // Simplemente llama al método genérico updateAgreementStatus con el estado "Paid".
         updateAgreementStatus(agreementId, "Paid");
     }
}