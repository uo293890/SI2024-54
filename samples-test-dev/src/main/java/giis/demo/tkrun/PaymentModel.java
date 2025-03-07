package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.util.List;

public class PaymentModel {
    private Database db = new Database();

    public void registerPayment(PaymentDTO payment) throws Exception {
        validatePayment(payment);

        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) "
                   + "VALUES (?, ?, 'Payment Received', ?)";
        db.executeUpdate(sql, payment.getInvoiceId(), new Date(payment.getPaymentDate().getTime()), payment.getAmount());
    }

    private void validatePayment(PaymentDTO payment) throws Exception {
        String sql = "SELECT invoice_date, invoice_vat FROM Invoice WHERE invoice_id = ?";
        List<Object[]> result = db.executeQueryArray(sql, payment.getInvoiceId());

        if (result.isEmpty()) {
            throw new Exception("Invoice not found.");
        }

        Object[] invoice = result.get(0);
        Date invoiceDate = (Date) invoice[0];
        double invoiceAmount = (double) invoice[1];

        if (payment.getPaymentDate().before(invoiceDate)) {
            throw new Exception("Payment date cannot be before the invoice date.");
        }

        if (payment.getAmount() != invoiceAmount) {
            throw new Exception("Payment amount must match the invoice amount exactly.");
        }
    }
}
