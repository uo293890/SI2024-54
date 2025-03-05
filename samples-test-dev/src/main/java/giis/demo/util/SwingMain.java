package giis.demo.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import giis.demo.tkrun.*;
/**
 * Main entry point that includes buttons for executing example application screens
 * and database initialization actions.
 * Does not follow MVC as it is only temporary for development purposes.
 */
public class SwingMain {

    private JFrame frame;

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
        frame.setBounds(0, 0, 350, 250); // Increased height to accommodate the new button
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Button for executing giis.demo.tkrun
        JButton btnEjecutarTkrun = new JButton("Ejecutar giis.demo.tkrun");
        btnEjecutarTkrun.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
            public void actionPerformed(ActionEvent e) {
            }
        });
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(btnEjecutarTkrun);

        // Button for initializing the database
        JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
        btnInicializarBaseDeDatos.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
            }
        });
        frame.getContentPane().add(btnInicializarBaseDeDatos);

        // Button for loading initial test data
        JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
        btnCargarDatosIniciales.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                db.createDatabase(false);
                db.loadDatabase();
            }
        });
        frame.getContentPane().add(btnCargarDatosIniciales);

        // Button for launching the Sponsorship Agreement Registration view
        JButton btnSponsorshipAgreement = new JButton("Registrar Acuerdo de Patrocinio");
        btnSponsorshipAgreement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Launch the Sponsorship Agreement Registration view
                SponsorshipAgreementRegistrationView view = new SponsorshipAgreementRegistrationView();
                view.setVisible(true);
            }
        });
        frame.getContentPane().add(btnSponsorshipAgreement);
    }

    public JFrame getFrame() {
        return this.frame;
    }
}