package giis.demo.tkrun;

import giis.demo.util.Database;

import java.util.Date;
import java.util.List;

public class InvoiceModel {
    private Database db = new Database();

    /**
     * Guarda una nueva factura SIN fecha de envío (invoice_date se registra al mandarla).
     */
    public void saveInvoice(String invoiceNumber, int agreementId, double invoiceVat) throws Exception {
        String sql = "INSERT INTO Invoice (agreement_id, invoice_number, invoice_vat) " +
                     "VALUES ('" + agreementId + "', '" + invoiceNumber + "', '" + invoiceVat + "')";
        db.executeUpdate(sql);
    }


    /**
     * Establece la fecha de envío de la factura (cuando realmente se manda).
     */
    public void setInvoiceDate(String invoiceNumber, String invoiceDate) throws Exception {
        String sql = "UPDATE Invoice SET invoice_date = '" + invoiceDate + "' WHERE invoice_number = '" + invoiceNumber + "'";
        db.executeUpdate(sql);
    }

    /**
     * Registra un movimiento de tipo "Invoice Sent" en la tabla Movement.
     */
    public void recordMovementForInvoice(String invoiceNumber, String sentDate) throws Exception {
        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) " +
                     "SELECT invoice_id, '" + sentDate + "', 'Invoice Sent', 0 FROM Invoice WHERE invoice_number = '" + invoiceNumber + "'";
        db.executeUpdate(sql);
    }

    /**
     * Devuelve todas las actividades (eventos) disponibles.
     */
    public List<Object[]> getAllEvents() throws Exception {
        String sql = "SELECT DISTINCT event_name FROM Event";
        return db.executeQueryArray(sql);
    }

    /**
     * Devuelve todos los acuerdos de patrocinio asociados a una actividad.
     * Incluye un campo "has_invoice" para saber si ya se generó factura.
     */
    public List<Object[]> getAgreementsForActivity(String activityName) throws Exception {
        String sql = "SELECT a.agreement_id, s.sponsor_name, c.spcontact_name, c.spcontact_email, " +
                     "a.agreement_amount, a.agreement_status, " +
                     "(CASE WHEN EXISTS (SELECT 1 FROM Invoice i WHERE i.agreement_id = a.agreement_id) THEN 'YES' ELSE 'NO' END) AS has_invoice " +
                     "FROM Agreement a " +
                     "JOIN SpContact c ON a.spcontact_id = c.spcontact_id " +
                     "JOIN Sponsor s ON c.sponsor_id = s.sponsor_id " +
                     "JOIN LevelOfSponsorship l ON a.level_id = l.level_id " +
                     "JOIN Event e ON l.event_id = e.event_id " +
                     "WHERE e.event_name = ?";
        
        return db.executeQueryArray(sql, new Object[]{ activityName });
    }



    /**
     * Devuelve la fecha de inicio del evento (actividad).
     */
    public Date getEventStartDate(String activityName) {
        String sql = "SELECT event_inidate FROM Event WHERE event_name = '" + activityName + "'";
        List<Object[]> result = db.executeQueryArray(sql);
        if (!result.isEmpty()) {
            return java.sql.Date.valueOf(result.get(0)[0].toString()); // esto debe funcionar bien
        } else {
            throw new RuntimeException("Event not found: " + activityName);
        }
        
    }
    
    

}
