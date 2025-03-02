package giis.demo.tkrun;

import giis.demo.util.SwingUtil;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;

    public InvoiceController(InvoiceModel m, InvoiceView v) {
        this.model = m;
        this.view = v;
        this.initController();
    }

    public void initController() {
        view.getBtnGenerateInvoice().addActionListener(e -> SwingUtil.exceptionWrapper(() -> generateInvoice()));
        view.getBtnSendInvoice().addActionListener(e -> SwingUtil.exceptionWrapper(() -> sendInvoice()));
    }

    private void generateInvoice() {
        String activity = view.getActivity();
        String invoiceDate = view.getInvoiceDate();
        String invoiceId = view.getInvoiceId();
        String name = view.getName();
        String taxId = view.getTaxId();
        String address = view.getAddress();

        // Guardar en la base de datos
        model.saveInvoice(activity, invoiceDate, invoiceId, name, taxId, address);
        view.showMessage("Invoice generated successfully!");
    }

    private void sendInvoice() {
        String invoiceId = view.getInvoiceId();
        model.sendInvoice(invoiceId);
        view.setSentDate("10/10/2023"); // Ejemplo de fecha de envío
        view.showMessage("Invoice sent successfully!");
    }
}