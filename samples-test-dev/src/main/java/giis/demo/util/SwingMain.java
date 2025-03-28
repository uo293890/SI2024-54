package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
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
        frame.setBounds(100, 100, 850, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("wrap, fillx, insets 0", "[grow,fill]", "[]0"));
        contentPanel.setBackground(new Color(250, 250, 250));

        // Header
        JLabel headerLabel = new JLabel("COIIPA Sponsorship Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        headerLabel.setForeground(Color.BLACK);
        contentPanel.add(headerLabel, "span, grow, gapbottom 20");

        // Setup buttons
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color buttonColor = new Color(245, 245, 245);
        Color borderColor = new Color(220, 220, 220);
        Color sectionColor = new Color(240, 240, 240);

        // 1. Database Setup Section
        JPanel dbPanel = createSectionPanel("Database Setup", sectionColor);
        JButton btnInitDB = createMinimalButton("Initialize Blank Database", buttonFont, buttonColor, borderColor);
        btnInitDB.addActionListener(e -> new Database().createDatabase(false));
        dbPanel.add(btnInitDB, "grow, wrap");

        JButton btnLoadData = createMinimalButton("Load Sample Data", buttonFont, buttonColor, borderColor);
        btnLoadData.addActionListener(e -> {
            Database db = new Database();
            db.createDatabase(false);
            db.loadDatabase();
        });
        dbPanel.add(btnLoadData, "grow");
        contentPanel.add(dbPanel, "grow, gapbottom 15");

        // 2. Date Configuration Section
        JPanel datePanel = createSectionPanel("Working Date", sectionColor);
        workingDate = LocalDate.now();
        textDate = new JTextField(workingDate.toString());
        textDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textDate.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            new EmptyBorder(8, 10, 8, 10)
        ));
        datePanel.add(textDate, "grow, wrap");

        JButton btnSetDate = createMinimalButton("Set Working Date", buttonFont, buttonColor, borderColor);
        btnSetDate.addActionListener((ActionEvent e) -> handleDateChange());
        datePanel.add(btnSetDate, "grow");
        contentPanel.add(datePanel, "grow, gapbottom 15");

        // 3. Event Management Section
        JPanel eventPanel = createSectionPanel("Event Management", sectionColor);
        
        JButton btnRegisterEvent = createMinimalButton("Register New Event", buttonFont, buttonColor, borderColor);
        btnRegisterEvent.addActionListener(e -> {
            RegisterEventView view = new RegisterEventView();
            new RegisterEventController(new RegisterEventModel(), view, workingDate);
            view.setVisible(true);
        });
        eventPanel.add(btnRegisterEvent, "grow, wrap");

        JButton btnEventClosure = createMinimalButton("Close Event", buttonFont, buttonColor, borderColor);
        btnEventClosure.addActionListener(e -> {
            ClosureEventView view = new ClosureEventView();
            new ClosureEventController(new CloseEventModel(), view);
            view.setVisible(true);
        });
        eventPanel.add(btnEventClosure, "grow");
        contentPanel.add(eventPanel, "grow, gapbottom 15");

        // 4. Sponsorship Management Section
        JPanel sponsorshipPanel = createSectionPanel("Sponsorship Management", sectionColor);
        
        JButton btnRegisterAgreement = createMinimalButton("Register Sponsorship Agreement", buttonFont, buttonColor, borderColor);
        btnRegisterAgreement.addActionListener(e -> {
            RegisterSponsorshipAgreementView view = new RegisterSponsorshipAgreementView();
            new RegisterSponsorshipAgreementController(new RegisterSponsorshipAgreementModel(), view, workingDate);
            view.setVisible(true);
        });
        sponsorshipPanel.add(btnRegisterAgreement, "grow, wrap");

        JButton btnFinancialStatus = createMinimalButton("Consult Financial Status", buttonFont, buttonColor, borderColor);
        btnFinancialStatus.addActionListener(e -> {
            ConsultActivityFinancialStatusView view = new ConsultActivityFinancialStatusView();
            new ConsultActivityFinancialStatusController(new ConsultActivityFinancialStatusModel(), view);
            view.setVisible(true);
        });
        sponsorshipPanel.add(btnFinancialStatus, "grow");
        contentPanel.add(sponsorshipPanel, "grow, gapbottom 15");

        // 5. Financial Operations Section
        JPanel financialPanel = createSectionPanel("Financial Operations", sectionColor);
        
        JButton btnInvoice = createMinimalButton("Manage Invoices", buttonFont, buttonColor, borderColor);
        btnInvoice.addActionListener(e -> {
            InvoiceView view = new InvoiceView();
            new InvoiceController(new InvoiceModel(), view);
            view.setVisible(true);
        });
        financialPanel.add(btnInvoice, "grow, wrap");

        JButton btnRegisterPayment = createMinimalButton("Register Payment", buttonFont, buttonColor, borderColor);
        btnRegisterPayment.addActionListener(e -> {
            RegisterPaymentView view = new RegisterPaymentView();
            new RegisterPaymentController(new RegisterPaymentModel(), view);
            view.setVisible(true);
        });
        financialPanel.add(btnRegisterPayment, "grow, wrap");

        JButton btnOtherMovement = createMinimalButton("Other Financial Movement", buttonFont, buttonColor, borderColor);
        btnOtherMovement.addActionListener(e -> {
            OtherMovementView view = new OtherMovementView();
            new OtherMovementController(new OtherMovementModel(), view);
            view.setVisible(true);
        });
        financialPanel.add(btnOtherMovement, "grow, wrap");

        JButton btnRegisterEstimatedIncomeExpense = createMinimalButton("Register Estimated Income/Expense", buttonFont, buttonColor, borderColor);
        btnRegisterEstimatedIncomeExpense.addActionListener(e -> {
            OtherIncomeExpenseView view = new OtherIncomeExpenseView(frame);
            new OtherIncomeExpenseController(new OtherIncomeExpenseModel(), view);
            view.setVisible(true);
        });
        financialPanel.add(btnRegisterEstimatedIncomeExpense, "grow, wrap");

        JButton btnReport = createMinimalButton("Generate Financial Report", buttonFont, buttonColor, borderColor);
        btnReport.addActionListener(e -> {
            FinancialReportView view = new FinancialReportView();
            new FinancialReportController(new FinancialReportModel(), view);
            view.setVisible(true);
        });
        financialPanel.add(btnReport, "grow");
        contentPanel.add(financialPanel, "grow");

        scrollPane.setViewportView(contentPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(mainPanel);
    }

    private JPanel createSectionPanel(String title, Color bgColor) {
        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 15", "[grow,fill]", "[]5[]"));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(80, 80, 80));
        panel.add(titleLabel, "gapbottom 5");
        
        return panel;
    }

    private JButton createMinimalButton(String text, Font font, Color bgColor, Color borderColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            new EmptyBorder(8, 15, 8, 15)
        ));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        return button;
    }

    private void handleDateChange() {
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
    }

    public JFrame getFrame() {
        return frame;
    }
}