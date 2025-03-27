package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import giis.demo.tkrun.*;

public class SwingMain {
    private JFrame frame;
    private JTextField textDate;
    private LocalDate workingDate;

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
        
     // Initialize with current date
        workingDate = LocalDate.now();
        textDate = new JTextField(workingDate.toString());
        textDate.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(textDate, "grow, wrap");

        JButton btnSetDate = new JButton("Set Working Date");
        btnSetDate.setFont(buttonFont);
        btnSetDate.addActionListener((ActionEvent e) -> {
            try {
                LocalDate newDate = LocalDate.parse(textDate.getText());
                LocalDate currentDate = LocalDate.now();
                
                if (newDate.isBefore(currentDate)) {
                    JOptionPane.showMessageDialog(frame, 
                        "Date cannot be earlier than today (" + currentDate + ")",
                        "Invalid Date", 
                        JOptionPane.ERROR_MESSAGE);
                    textDate.setText(currentDate.toString());
                    workingDate = currentDate;
                } else {
                    workingDate = newDate;
                    JOptionPane.showMessageDialog(frame, 
                        "Date set correctly to " + workingDate.toString(), 
                        "Date set", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Invalid date format. Use YYYY-MM-DD", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                textDate.setText(workingDate.toString());
            }
        });
        panel.add(btnSetDate, "grow, wrap");

        JButton btnRegisterAgreement = new JButton("Register Sponsorship Agreement");
        btnRegisterAgreement.setFont(buttonFont);
        btnRegisterAgreement.addActionListener(e -> {
            RegisterSponsorshipAgreementView view = new RegisterSponsorshipAgreementView();
            new RegisterSponsorshipAgreementController(new RegisterSponsorshipAgreementModel(), view, workingDate);
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
        
        
     // Renamed button to avoid duplicate variable names
        JButton btnRegisterEstimatedIncomeExpense = new JButton("Register estimated income/expense");
        btnRegisterEstimatedIncomeExpense.setFont(buttonFont);
        btnRegisterEstimatedIncomeExpense.addActionListener(e -> {
            OtherIncomeExpenseView view = new OtherIncomeExpenseView(frame);
            new OtherIncomeExpenseController(new OtherIncomeExpenseModel(), view);
            view.setVisible(true);
        });
        panel.add(btnRegisterEstimatedIncomeExpense, "wrap");
        
        

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

        scrollPane.setViewportView(panel);
    }

    public JFrame getFrame() {
        return frame;
    }
}