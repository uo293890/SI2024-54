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
    private JTable dataTable; // Tabla para mostrar datos
    private Database db = new Database(); // Instancia única de Database

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
        frame = new JFrame("Gestión de Sponsorships - COIIPA Events");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel superior con botones de control
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botones de inicialización
        JButton btnInitDb = new JButton("Inicializar BD");
        btnInitDb.addActionListener(e -> {
            db.createDatabase(true);
            loadAndDisplayData("SELECT * FROM Event");
        });

        JButton btnLoadData = new JButton("Cargar Datos");
        btnLoadData.addActionListener(e -> {
            db.loadDatabase();
            loadAndDisplayData("SELECT * FROM Event");
        });

        // Botones para User Stories
        JButton btnReporte = new JButton("Informe Económico");
        btnReporte.addActionListener(e -> showFinancialReportDialog());

        JButton btnFacturacion = new JButton("Gestión de Facturas");
        btnFacturacion.addActionListener(e -> showInvoiceDialog());

        // Botón para Registrar Pagos
        JButton btnRegistrarPago = new JButton("Registrar Pago");
        btnRegistrarPago.addActionListener(e -> showPaymentDialog());

        // Botón para Registrar Otros Ingresos/Gastos
        JButton btnOtherMovement = new JButton("Registrar Otros Movimientos");
        btnOtherMovement.addActionListener(e -> showOtherMovementDialog());

        // Configuración de botones
        Color btnColor = new Color(51, 102, 153);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 12);
        
        styleButton(btnInitDb, btnColor, btnFont);
        styleButton(btnLoadData, btnColor, btnFont);
        styleButton(btnReporte, new Color(76, 175, 80), btnFont);
        styleButton(btnFacturacion, new Color(244, 67, 54), btnFont);
        styleButton(btnRegistrarPago, new Color(255, 193, 7), btnFont); // Color amarillo para el botón de pagos
        styleButton(btnOtherMovement, new Color(156, 39, 176), btnFont); // Color morado para el botón de otros movimientos

        // Añadir componentes al panel superior
        topPanel.add(btnInitDb);
        topPanel.add(btnLoadData);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnReporte);
        topPanel.add(btnFacturacion);
        topPanel.add(btnRegistrarPago);
        topPanel.add(btnOtherMovement);

        // Configurar tabla principal
        dataTable = new JTable();
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Eventos"));

        // Panel de estado
        JPanel statusPanel = new JPanel();
        JLabel statusLabel = new JLabel("Estado: ");
        JTextField statusField = new JTextField(20);
        statusField.setEditable(false);
        statusPanel.add(statusLabel);
        statusPanel.add(statusField);

        // Organizar layout principal
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);

        // Cargar datos iniciales
        loadAndDisplayData("SELECT event_id AS ID, event_title AS Evento FROM Event");
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
     * Carga datos de la base de datos y actualiza la tabla
     */
    private void loadAndDisplayData(String query) {
        try {
            // Ejecutar consulta genérica (adaptar según necesidad)
            List<Object[]> results = db.executeQueryArray(query);
            
            // Crear modelo de tabla
            DefaultTableModel model = new DefaultTableModel();
            
            // Obtener nombres de columnas (simplificado)
            if (!results.isEmpty()) {
                model.setColumnIdentifiers(results.get(0)); // Primera fila como headers
                for (int i = 1; i < results.size(); i++) {
                    model.addRow(results.get(i));
                }
            }
            
            dataTable.setModel(model);
            SwingUtil.autoAdjustColumns(dataTable); // Usar utilidad existente
            
        } catch (Exception e) {
            SwingUtil.showMessage("Error al cargar datos: " + e.getMessage(), 
                                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JFrame getFrame() { return this.frame; }
}