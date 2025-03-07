package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;

public class InvoiceModel {
    private Database db = new Database();

    public void saveInvoice(InvoiceDTO invoice) throws Exception {
        String sql = "INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat) " +
                     "VALUES (?, ?, ?, ?)";
        try {
            db.executeUpdate(sql,
                invoice.getAgreementId(),
                new Date(invoice.getInvoiceDate().getTime()),
                invoice.getInvoiceNumber(),
                invoice.getInvoiceVat()
            );
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public String generateInvoiceNumber() {
        // Genera un número de factura de 9 dígitos usando los últimos 9 dígitos del tiempo actual en milisegundos.
        long number = System.currentTimeMillis() % 1000000000;
        return String.format("%09d", number);
    }
}
