package giis.demo.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import giis.demo.tkrun.*;

/**
 * Punto de entrada principal que incluye botones para la ejecución de las pantallas 
 * de las aplicaciones de ejemplo y acciones de inicialización de la base de datos.
 */
public class SwingMain {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SwingMain window = new SwingMain();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public SwingMain() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Main");
        frame.setBounds(0, 0, 287, 185);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnFinancialReport = new JButton("Financial Report");
        btnFinancialReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FinancialReportController controller = new FinancialReportController(new FinancialReportModel(), new FinancialReportView());
                controller.initController();
            }
        });
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(btnFinancialReport);

        JButton btnInvoiceManagement = new JButton("Invoice Management");
        btnInvoiceManagement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InvoiceController controller = new InvoiceController(new InvoiceModel(), new InvoiceView());
                controller.initController();
            }
        });
        frame.getContentPane().add(btnInvoiceManagement);

        JButton btnInitializeDatabase = new JButton("Initialize Database");
        btnInitializeDatabase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
                db.loadDatabase();
            }
        });
        frame.getContentPane().add(btnInitializeDatabase);
    }

    public JFrame getFrame() {
        return this.frame;
    }
}