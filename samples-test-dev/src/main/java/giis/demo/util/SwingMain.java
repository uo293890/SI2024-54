package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import giis.demo.tkrun.*;
import net.miginfocom.swing.MigLayout;

public class SwingMain {
    private JFrame frame;
    private JTextField textDate;
    public Date date;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR codigo autogenerado
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR codigo autogenerado
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
		frame.setBounds(0, 0, 400, 300);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "[grow]", "[][][][][]"));
		
     // Botón para inicializar base de datos
        JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
        btnInicializarBaseDeDatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
            }
        });
        panel.add(btnInicializarBaseDeDatos, "grow, wrap");	
			
     // Botón para cargar datos iniciales
        JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
        btnCargarDatosIniciales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
                db.loadDatabase();
            }
        });
        panel.add(btnCargarDatosIniciales, "grow, wrap");
        
     // Botón para Sponsorship Agreement Registration
        JButton btnSponsorshipAgreementRegistration = new JButton("Sponsorship Agreement Registration");
        btnSponsorshipAgreementRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterSponsorshipAgreementModel model = new RegisterSponsorshipAgreementModel();
                RegisterSponsorshipAgreementView view = new RegisterSponsorshipAgreementView();
                new RegisterSponsorshipAgreementController(model, view);
                view.setVisible(true);
            }
        });
        panel.add(btnSponsorshipAgreementRegistration, "grow, wrap");
        
     // Botón para Activity Financial Status
        JButton btnActivityFinancialStatus = new JButton("Activity Financial Status");
        btnActivityFinancialStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ActivityFinancialStatusModel model = new ActivityFinancialStatusModel();
            	ActivityFinancialStatusView view = new ActivityFinancialStatusView();
                new ActivityFinancialStatusController(model, view);
                view.setVisible(true);
            }
        });
        panel.add(btnActivityFinancialStatus, "grow, wrap");
        
     // Botón para Report
        JButton btnReport = new JButton("Report");
        btnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	FinancialReportView reportView = new FinancialReportView();
                FinancialReportModel reportModel = new FinancialReportModel();
                new FinancialReportController(reportModel, reportView);
                reportView.setVisible(true);
            }
        });
        panel.add(btnReport, "grow, wrap");
        
     // Botón para Invoice
        JButton btnInvoice = new JButton("Invoice");
        btnInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	InvoiceView invoiceView = new InvoiceView();
                InvoiceModel invoiceModel = new InvoiceModel();
                new InvoiceController(invoiceModel, invoiceView);
                invoiceView.setVisible(true);
            }
        });
        panel.add(btnInvoice, "grow, wrap");
        
     // Botón para Payment
        JButton btnPayment = new JButton("Payment");
        btnPayment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	PaymentView paymentView = new PaymentView();
                PaymentModel paymentModel = new PaymentModel();
                new PaymentController(paymentModel, paymentView);
                paymentView.setVisible(true);
            }
        });
        panel.add(btnPayment, "grow, wrap");
        
     // Botón para Other Movement
        JButton btnOtherMovement = new JButton("Other Movement");
        btnOtherMovement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	OtherMovementView otherMovementView = new OtherMovementView();
                OtherMovementModel otherMovementModel = new OtherMovementModel();
                new OtherMovementController(otherMovementModel, otherMovementView);
                otherMovementView.setVisible(true);
            }
        });
        panel.add(btnOtherMovement, "grow, wrap");

	textDate = new JTextField();
    	textDate.setText(LocalDate.now().toString());
    	panel.add(textDate, "grow, wrap");
    			
    	JButton btnSetDate = new JButton("Set Date");
    	btnSetDate.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {	
    			try{
    				date = Util.isoStringToDate(textDate.getText());
    			} catch(ApplicationException a) {
    				JOptionPane.showMessageDialog(frame, "Date Format Invalid, Try: YYYY-MM-DD", "Set Date Error", JOptionPane.ERROR_MESSAGE);
    			}
    			JOptionPane.showMessageDialog(frame, "Date set correctly", "Date set", JOptionPane.INFORMATION_MESSAGE);
    		}});
    	panel.add(btnSetDate, "grow, wrap");
                
        scrollPane.setViewportView(panel);
	}

	public JFrame getFrame() { return this.frame; }
	
}
