package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;

    public InvoiceController(InvoiceModel m, InvoiceView v) {
        this.model = m;
        this.view = v;
        initController();
    }

    public void initController() {
        // Vincular el botón de generar factura
        view.getBtnGenerateInvoice().addActionListener(e -> generateInvoice());

        // Vincular el botón de enviar factura
        view.getBtnSendInvoice().addActionListener(e -> sendInvoice());
    }

    private void generateInvoice() {
        // Obtener los datos de entrada de la vista
        int agreementId = view.getAgreementId(); // Obtener el agreement_id
        String invoiceDate = view.getInvoiceDate();
        String invoiceNumber = view.getInvoiceNumber();
        String recipientName = view.getRecipientName();
        String recipientTaxId = view.getRecipientTaxId();
        String recipientAddress = view.getRecipientAddress();
        double baseAmount = view.getBaseAmount();
        double vat = view.getVat();

        // Generar la factura usando el modelo
        model.generateInvoice(agreementId, invoiceDate, invoiceNumber, recipientName, recipientTaxId, recipientAddress, baseAmount, vat);

        // Mostrar un mensaje de éxito
        JOptionPane.showMessageDialog(view, "Invoice generated successfully!");
    }

    private void sendInvoice() {
        // Obtener el número de factura de la vista
        String invoiceNumber = view.getInvoiceNumber();

        // Enviar la factura usando el modelo
        model.sendInvoice(invoiceNumber);

        // Mostrar un mensaje de éxito
        JOptionPane.showMessageDialog(view, "Invoice sent successfully!");
    }
}