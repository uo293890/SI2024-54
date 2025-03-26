package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import giis.demo.tkrun.*;

public class SwingMain {
    private JFrame frame;
    private JTextField textDate;
    public java.util.Date date;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SwingMain window = new SwingMain();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SwingMain() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("COIIPA Sponsorship Management");
        frame.setBounds(100, 100, 800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "[grow,fill]", "[][][][][][][][][][][][]"));

        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        JButton btnInitDB = new JButton("Initialize Blank Database");
        btnInitDB.setFont(buttonFont);
        btnInitDB.addActionListener(e -> new Database().createDatabase(false));
        panel.add(btnInitDB, "wrap");

        JButton btnLoadData = new JButton("Load Sample Data");
        btnLoadData.setFont(buttonFont);
        btnLoadData.addActionListener(e -> {
            Database db = new Database();
            db.createDatabase(false);
            db.loadDatabase();
        });
        panel.add(btnLoadData, "wrap");

        JButton btnRegisterAgreement = new JButton("Register Sponsorship Agreement");
        btnRegisterAgreement.setFont(buttonFont);
        btnRegisterAgreement.addActionListener(e -> {
            RegisterSponsorshipAgreementView view = new RegisterSponsorshipAgreementView();
            new RegisterSponsorshipAgreementController(new RegisterSponsorshipAgreementModel(), view);
            view.setVisible(true);
        });
        panel.add(btnRegisterAgreement, "wrap");

        JButton btnFinancialStatus = new JButton("Consult Activity Financial Status");
        btnFinancialStatus.setFont(buttonFont);
        btnFinancialStatus.addActionListener(e -> {
            ConsultActivityFinancialStatusView view = new ConsultActivityFinancialStatusView();
            new ConsultActivityFinancialStatusController(new ConsultActivityFinancialStatusModel(), view);
            view.setVisible(true);
        });
        panel.add(btnFinancialStatus, "wrap");

        JButton btnReport = new JButton("Generate Income & Expense Report");
        btnReport.setFont(buttonFont);
        btnReport.addActionListener(e -> {
            FinancialReportView view = new FinancialReportView();
            new FinancialReportController(new FinancialReportModel(), view);
            view.setVisible(true);
        });
        panel.add(btnReport, "wrap");

        JButton btnInvoice = new JButton("Manage Invoices");
        btnInvoice.setFont(buttonFont);
        btnInvoice.addActionListener(e -> {
            InvoiceView view = new InvoiceView();
            new InvoiceController(new InvoiceModel(), view);
            view.setVisible(true);
        });
        panel.add(btnInvoice, "wrap");

        JButton btnRegisterPayment = new JButton("Register Payment");
        btnRegisterPayment.setFont(buttonFont);
        btnRegisterPayment.addActionListener(e -> {
            RegisterPaymentView view = new RegisterPaymentView();
            new RegisterPaymentController(new RegisterPaymentModel(), view);
            view.setVisible(true);
        });
        panel.add(btnRegisterPayment, "wrap");

        JButton btnEventClosure = new JButton("Close Event");
        btnEventClosure.setFont(buttonFont);
        btnEventClosure.addActionListener(e -> {
            ClosureEventView view = new ClosureEventView();
            new ClosureEventController(new CloseEventModel(), view);
            view.setVisible(true);
        });
        panel.add(btnEventClosure, "wrap");

        

        JButton btnOtherMovement = new JButton("Other Financial Movement");
        btnOtherMovement.setFont(buttonFont);
        btnOtherMovement.addActionListener(e -> {
            OtherMovementView view = new OtherMovementView();
            new OtherMovementController(new OtherMovementModel(), view);
            view.setVisible(true);
        });
        panel.add(btnOtherMovement, "wrap");

        textDate = new JTextField(LocalDate.now().toString());
        textDate.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(textDate, "grow, wrap");

        JButton btnSetDate = new JButton("Set Working Date");
        btnSetDate.setFont(buttonFont);
        btnSetDate.addActionListener((ActionEvent e) -> {
            try {
                date = Util.isoStringToDate(textDate.getText());
                JOptionPane.showMessageDialog(frame, "Date set correctly", "Date set", JOptionPane.INFORMATION_MESSAGE);
            } catch (ApplicationException ex) {
                JOptionPane.showMessageDialog(frame, "Date Format Invalid, Try: YYYY-MM-DD", "Set Date Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnSetDate, "grow, wrap");

        scrollPane.setViewportView(panel);
    }

    public JFrame getFrame() {
        return frame;
    }
}