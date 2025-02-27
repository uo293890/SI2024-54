package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import giis.demo.jdbc.Invoices.*;

public class BillingScreen extends JDialog {

    private final InvoiceController invoiceController;

    public BillingScreen() {
        this(new InvoiceRepository(), new SponsorRepository());
    }

    public BillingScreen(InvoiceRepository invoiceRepository, SponsorRepository sponsorRepository) {
        super((Frame) null, "Generar y Enviar Facturas", true);
        this.invoiceController = new InvoiceController(invoiceRepository, sponsorRepository);

        setSize(600, 400);
        setLocationRelativeTo(null);

        JButton generateButton = new JButton("Generar Facturas");
        JButton sendButton = new JButton("Enviar Facturas");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Invoice> invoices = invoiceController.generateInvoices();
                JOptionPane.showMessageDialog(BillingScreen.this,
                        "Facturas generadas correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean sent = invoiceController.sendInvoices();
                if (sent) {
                    JOptionPane.showMessageDialog(BillingScreen.this,
                            "Facturas enviadas correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(BillingScreen.this,
                            "Hubo un problema al enviar las facturas.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(generateButton);
        panel.add(sendButton);

        add(panel);
    }
}