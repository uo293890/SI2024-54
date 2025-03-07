package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.Date;
import java.util.List;

public class PaymentModel {
    private Database db = new Database();

    /**
     * Registers a payment in the database.
     * @param payment The payment details.
     * @throws Exception If the payment is invalid (e.g., amount mismatch or date before invoice date).
     */
    public void registerPayment(PaymentDTO payment) throws Exception {
        // Validate the payment
        validatePayment(payment);

        // Insert the payment into the Movement table
        String sql = "INSERT INTO Movement (invoice_id, movement_date, movement_concept, movement_amount) "
                   + "VALUES (?, ?, 'Payment Received', ?)";
        db.executeUpdate(sql, payment.getInvoiceId(), new Date(payment.getPaymentDate().getTime()), payment.getAmount());
    }

    /**
     * Validates the payment against the invoice.
     * @param payment The payment details.
     * @throws Exception If the payment is invalid.
     */
    private void validatePayment(PaymentDTO payment) throws Exception {
        // Fetch the invoice details
        String sql = "SELECT invoice_date, invoice_vat FROM Invoice WHERE invoice_id = ?";
        List<Object[]> result = db.executeQueryArray(sql, payment.getInvoiceId());

        if (result.isEmpty()) {
            throw new Exception("Invoice not found.");
        }

        Object[] invoice = result.get(0);
        Date invoiceDate = (Date) invoice[0];
        double invoiceAmount = (double) invoice[1];

        // Validate the payment date
        if (payment.getPaymentDate().before(invoiceDate)) {
            throw new Exception("Payment date cannot be before the invoice date.");
        }

        // Validate the payment amount
        if (payment.getAmount() != invoiceAmount) {
            throw new Exception("Payment amount must match the invoice amount exactly.");
        }
    }
}