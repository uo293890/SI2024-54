package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import java.awt.event.ActionEvent;
import giis.demo.tkrun.*;
import net.miginfocom.swing.MigLayout;

/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas 
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
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
		frame.setBounds(0, 0, 287, 185);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "[grow]", "[][][][][]"));
		
     // Boton para inicializar base de datos
        JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
        btnInicializarBaseDeDatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
            }
        });
        panel.add(btnInicializarBaseDeDatos, "grow, wrap");	
			
     // Boton para cargar datos iniciales
        JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
        btnCargarDatosIniciales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
                db.loadDatabase();
            }
        });
        panel.add(btnCargarDatosIniciales, "grow, wrap");
        
        
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