package giis.demo.tkrun;

import java.util.List;

import giis.demo.util.Database;

public class InvoiceModel {
    private Database db = new Database();

    public void generateInvoice(int activityId, int sponsorId, double amount) {
        String sql = "INSERT INTO Invoice(activity_id, sponsor_id, base_amount, total_amount) "
                   + "VALUES (?, ?, ?, ?)";
        double total = amount * 1.21;
        db.executeUpdate(sql, activityId, sponsorId, amount, total);
    }
    public void sendInvoice(String invoiceNumber) {
        db.executeUpdate("UPDATE Invoice SET sent_date = CURRENT_DATE WHERE invoice_number = ?", invoiceNumber);
    }
    
    public List<InvoiceDTO> getPendingInvoices() {
        String sql = "SELECT * FROM Invoice WHERE sent_date IS NULL";
        return db.executeQueryPojo(InvoiceDTO.class, sql);
    }
}