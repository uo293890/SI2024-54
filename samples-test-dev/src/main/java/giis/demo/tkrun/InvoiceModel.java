package giis.demo.tkrun;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import giis.demo.util.Database;

public class InvoiceModel {
    private Database db = new Database();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void saveInvoice(String invoiceNumber, int agreementId, java.util.Date invoiceDate, double invoiceVat, String recipientName, String recipientTaxId, String recipientAddress) throws Exception {
        String sql = "INSERT INTO Invoice (invoice_number, agreement_id, invoice_date, invoice_vat, recipient_name, recipient_tax_id, recipient_address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            db.executeUpdate(sql, invoiceNumber, agreementId, new Date(invoiceDate.getTime()), invoiceVat, recipientName, recipientTaxId, recipientAddress);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public void updateInvoiceSentDate(String invoiceNumber, java.util.Date sentDate) throws Exception {
        String sql = "UPDATE Invoice SET sent_date = ? WHERE invoice_number = ?";
        try {
            db.executeUpdate(sql, new Date(sentDate.getTime()), invoiceNumber);
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public List<InvoiceDTO> getAllInvoices() throws Exception {
        List<InvoiceDTO> invoices = new ArrayList<>();
        String sql = "SELECT i.invoice_number, i.agreement_id, e.edition_title, i.invoice_date, i.invoice_vat, i.recipient_name, i.recipient_tax_id, i.recipient_address " +
                     "FROM Invoice i " +
                     "JOIN Agreement a ON i.agreement_id = a.agreement_id " +
                     "JOIN Edition e ON a.edition_id = e.edition_id";
        try {
            List<List<Object>> results = db.executeQuery(sql);
            for (List<Object> row : results) {
                invoices.add(new InvoiceDTO(
                    row.get(0).toString(),
                    Integer.parseInt(row.get(1).toString()),
                    row.get(2).toString(),
                    row.get(3).toString(),
                    Double.parseDouble(row.get(4).toString()),
                    row.get(5).toString(),
                    row.get(6).toString(),
                    row.get(7).toString()
                ));
            }
            return invoices;
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }

    public void generateAutomaticInvoices() throws Exception {
        try {
            List<List<Object>> agreements = db.executeQuery("SELECT agreement_id, edition_id FROM Agreement");
            for (List<Object> agreementRow : agreements) {
                int agreementId = Integer.parseInt(agreementRow.get(0).toString());
                int editionId = Integer.parseInt(agreementRow.get(1).toString());
                
                List<List<Object>> editionResults = db.executeQuery("SELECT edition_inidate FROM Edition WHERE edition_id = " + editionId);
                if (!editionResults.isEmpty() && editionResults.get(0).get(0) != null) {
                    String editionDateStr = editionResults.get(0).get(0).toString();
                    Date editionDate = Date.valueOf(editionDateStr);
                    
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(editionDate);
                    calendar.add(Calendar.WEEK_OF_YEAR, -4);
                    Date invoiceDate = new Date(calendar.getTimeInMillis());
                    
                    List<List<Object>> earlyInvoiceResults = db.executeQuery("SELECT early_invoice_request FROM Agreement WHERE agreement_id = " + agreementId);
                    if (!earlyInvoiceResults.isEmpty() && earlyInvoiceResults.get(0).get(0) != null) {
                        String earlyInvoiceDateStr = earlyInvoiceResults.get(0).get(0).toString();
                        Date earlyInvoiceDate = Date.valueOf(earlyInvoiceDateStr);
                        if (earlyInvoiceDate.before(invoiceDate)) {
                            invoiceDate = earlyInvoiceDate;
                        }
                    }
                    
                    String invoiceNumber = UUID.randomUUID().toString().substring(0, 9).toUpperCase();
                    saveInvoice(invoiceNumber, agreementId, invoiceDate, 0.0, "", "", "");
                }
            }
        } catch (Exception e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }
}
