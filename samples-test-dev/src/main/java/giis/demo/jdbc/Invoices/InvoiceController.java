package giis.demo.jdbc.Invoices;

import java.util.List;

import giis.demo.jdbc.Invoices.SponsorRepository;

public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final SponsorRepository sponsorRepository;

    public InvoiceController(InvoiceRepository invoiceRepository, SponsorRepository sponsorRepository) {
        this.invoiceRepository = invoiceRepository;
        this.sponsorRepository = sponsorRepository;
    }

    // Generar las facturas para los patrocinadores
    public List<Invoice> generateInvoices() {
        // Obtener todos los patrocinadores y generar las facturas según las regulaciones
        List<Sponsor> sponsors = sponsorRepository.getAllSponsors();
        for (Sponsor sponsor : sponsors) {
            Invoice invoice = new Invoice();
            invoice.setAgreementId(sponsor.getId());  // Relacionar con el acuerdo
            invoice.setAmount(sponsor.getAmount());
            invoice.setVatPercentage(21);  // Ejemplo de porcentaje de IVA
            invoice.setProfitOrNonprofit("Profit");  // Ajustar según el tipo

            // Guardar la factura
            invoiceRepository.save(invoice);
        }
        return invoiceRepository.getAllInvoices();
    }

    // Enviar las facturas por correo electrónico
    public boolean sendInvoices() {
        // Lógica para enviar las facturas generadas por correo
        // Se puede usar JavaMail para enviar correos, por ejemplo
        return true;  // Devolver si las facturas se enviaron correctamente
    }
}
