package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;

    public InvoiceController(InvoiceModel model, InvoiceView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getBtnGenerate().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
            if(validateTaxData()) {
                model.generateInvoice(
                    view.getSelectedActivityId(),
                    view.getSelectedSponsorId(),
                    view.getAmount()
                );
                JOptionPane.showMessageDialog(view, "Factura generada exitosamente!");
                view.refreshData();
            }
        }));
    }

    private boolean validateTaxData() {
        String taxId = view.getTaxId();
        return taxId.matches("^[0-9]{8}[A-Z]$");
    }
}