package giis.demo.tkrun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class PaymentController {
    private PaymentModel model;
    private PaymentView view;

    public PaymentController(PaymentModel model, PaymentView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getRegisterButton().addActionListener(e -> registerPayment());
    }

    private void registerPayment() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date paymentDate = sdf.parse(view.getPaymentDate());

            PaymentDTO payment = new PaymentDTO();
            payment.setInvoiceId(view.getInvoiceId());
            payment.setPaymentDate(paymentDate);
            payment.setAmount(view.getAmount());

            model.registerPayment(payment);
            JOptionPane.showMessageDialog(view, "Payment registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(view, "Invalid date format (Use dd/MM/yyyy).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}