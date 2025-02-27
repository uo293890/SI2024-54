package giis.demo.jdbc.Invoices;

import java.util.List;

public class InvoiceService {
    private InvoiceRepository invoiceRepository;

    // Constructor
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // Guardar factura
    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    // Obtener todas las facturas
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.getAllInvoices();
    }

    // Obtener factura por ID
    public Invoice getInvoiceById(int id) {
        return invoiceRepository.getInvoiceById(id);
    }
}
