package giis.demo.tkrun;

import giis.demo.util.Database;

public class InvoiceModel {
    private Database db = new Database();

    public void generateInvoice(int agreementId, String invoiceDate, String invoiceNumber, 
                               String recipientName, String recipientTaxId, 
                               String recipientAddress, double baseAmount, double vat) {
        String sql = "INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, "
                   + "recipient_name, recipient_tax_id, recipient_address, base_amount, vat) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        db.executeUpdate(sql, agreementId, invoiceDate, invoiceNumber, recipientName, recipientTaxId, recipientAddress, baseAmount, vat);
    }

    public void sendInvoice(String invoiceNumber) {
        String sql = "UPDATE Invoice SET sent_date = CURRENT_DATE WHERE invoice_number = ?";
        db.executeUpdate(sql, invoiceNumber);
    }
}