package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.*;

public class SwingMain {
    private JFrame frame;
    private JTable dataTable; // Table to display data
    private Database db = new Database(); // Single instance of Database

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SwingMain window = new SwingMain();
            window.frame.setVisible(true);
        });
    }

    public SwingMain() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Sponsorship Management - COIIPA Events");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Top panel with control buttons
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialization buttons
        JButton btnInitDb = new JButton("Initialize DB");
        btnInitDb.addActionListener(e -> {
            db.createDatabase(true);
            loadAndDisplayData("SELECT * FROM Event");
        });

        JButton btnLoadData = new JButton("Load Data");
        btnLoadData.addActionListener(e -> {
            db.loadDatabase();
            loadAndDisplayData("SELECT * FROM Event");
        });

        // Buttons for User Stories
        JButton btnReport = new JButton("Financial Report");
        btnReport.addActionListener(e -> showFinancialReportDialog());

        JButton btnInvoicing = new JButton("Invoice Management");
        btnInvoicing.addActionListener(e -> showInvoiceDialog());

        // Button for Registering Payments
        JButton btnRegisterPayment = new JButton("Register Payment");
        btnRegisterPayment.addActionListener(e -> showPaymentDialog());

        // Button for Registering Other Income/Expenses
        JButton btnOtherMovement = new JButton("Register Other Movements");
        btnOtherMovement.addActionListener(e -> showOtherMovementDialog());

        // Button styling
        Color btnColor = new Color(51, 102, 153);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 12);
        
        styleButton(btnInitDb, btnColor, btnFont);
        styleButton(btnLoadData, btnColor, btnFont);
        styleButton(btnReport, new Color(76, 175, 80), btnFont);
        styleButton(btnInvoicing, new Color(244, 67, 54), btnFont);
        styleButton(btnRegisterPayment, new Color(255, 193, 7), btnFont); // Yellow color for payment button
        styleButton(btnOtherMovement, new Color(156, 39, 176), btnFont); // Purple color for other movements button

        // Add components to the top panel
        topPanel.add(btnInitDb);
        topPanel.add(btnLoadData);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnReport);
        topPanel.add(btnInvoicing);
        topPanel.add(btnRegisterPayment);
        topPanel.add(btnOtherMovement);

        // Configure main table
        dataTable = new JTable();
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Event List"));

        // Status panel
        JPanel statusPanel = new JPanel();
        JLabel statusLabel = new JLabel("Status: ");
        JTextField statusField = new JTextField(20);
        statusField.setEditable(false);
        statusPanel.add(statusLabel);
        statusPanel.add(statusField);

        // Organize main layout
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);

        // Load initial data
        loadAndDisplayData("SELECT event_id AS ID, event_title AS Event FROM Event");
    }

    private void styleButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void showFinancialReportDialog() {
        ReportView reportView = new ReportView();
        ReportModel reportModel = new ReportModel();
        new ReportController(reportModel, reportView);
        reportView.setLocationRelativeTo(frame);
        reportView.setVisible(true);
    }

    private void showInvoiceDialog() {
        InvoiceView invoiceView = new InvoiceView();
        InvoiceModel invoiceModel = new InvoiceModel();
        new InvoiceController(invoiceModel, invoiceView);
        invoiceView.setLocationRelativeTo(frame);
        invoiceView.setVisible(true);
    }

    private void showPaymentDialog() {
        PaymentView paymentView = new PaymentView();
        PaymentModel paymentModel = new PaymentModel();
        new PaymentController(paymentModel, paymentView);
        paymentView.setLocationRelativeTo(frame);
        paymentView.setVisible(true);
    }

    private void showOtherMovementDialog() {
        OtherMovementView otherMovementView = new OtherMovementView();
        OtherMovementModel otherMovementModel = new OtherMovementModel();
        new OtherMovementController(otherMovementModel, otherMovementView);
        otherMovementView.setLocationRelativeTo(frame);
        otherMovementView.setVisible(true);
    }

    /**
     * Loads data from the database and updates the table
     */
    private void loadAndDisplayData(String query) {
        try {
            // Execute generic query (adapt as needed)
            List<Object[]> results = db.executeQueryArray(query);
            
            // Create table model
            DefaultTableModel model = new DefaultTableModel();
            
            // Get column names (simplified)
            if (!results.isEmpty()) {
                model.setColumnIdentifiers(results.get(0)); // First row as headers
                for (int i = 1; i < results.size(); i++) {
                    model.addRow(results.get(i));
                }
            }
            
            dataTable.setModel(model);
            SwingUtil.autoAdjustColumns(dataTable); // Use existing utility
            
        } catch (Exception e) {
            SwingUtil.showMessage("Error loading data: " + e.getMessage(), 
                                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JFrame getFrame() { return this.frame; }
}