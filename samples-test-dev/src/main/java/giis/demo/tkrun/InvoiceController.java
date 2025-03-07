package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat;

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        initView();
        initController();
    }

    private void initView() {
        view.setVisible(true);
    }

    private void initController() {
        view.getGenerateButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateInvoice();
            }
        });

        view.getRegisterButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerInvoice();
            }
        });
    }

    private void generateInvoice() {
        try {
            InvoiceDTO newInvoice = new InvoiceDTO();
            newInvoice.setInvoiceNumber(model.generateInvoiceNumber());
            newInvoice.setInvoiceDate(new Date());

            view.setInvoiceNumber(newInvoice.getInvoiceNumber());
            view.setInvoiceDate(dateFormat.format(newInvoice.getInvoiceDate()));
        } catch (Exception ex) {
            view.showError("Error al generar la factura: " + ex.getMessage());
        }
    }

    private void registerInvoice() {
        try {
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setInvoiceNumber(view.getInvoiceNumber());
            invoice.setInvoiceDate(dateFormat.parse(view.getInvoiceDate()));
            invoice.setAgreementId(Integer.parseInt(view.getAgreementId()));
            invoice.setInvoiceVat(Double.parseDouble(view.getInvoiceVat()));

            // Aquí se podría agregar validación adicional de datos si fuese necesario
            model.saveInvoice(invoice);
            view.showMessage("Factura registrada correctamente");
            view.clearForm();
        } catch (Exception ex) {
            view.showError("Error al registrar la factura: " + ex.getMessage());
        }
    }
}
