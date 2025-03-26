package giis.demo.tkrun;

import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementStatus;
import giis.demo.tkrun.OtherIncomeExpenseDTO.MovementType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista para manejar otros ingresos/gastos. 
 * Incluye pestaña de formulario (registro) y pestaña para mostrar la tabla completa (17 columnas).
 */
public class OtherIncomeExpenseView extends JDialog {

    private static final long serialVersionUID = 1L;

    // -- Pestaña 1: Registro --
    private JComboBox<MovementType> comboType;
    private JComboBox<MovementStatus> comboStatus;
    private JComboBox<String> comboEvent;
    private JTextField txtEstimatedAmount;
    private JTextField txtPaidAmount;
    private JTextField txtPaidDate;
    private JTextField txtConcept;
    private JButton btnRegister;
    private JButton btnClear;

    // -- Pestaña 2: Tabla completa --
    private JTable tableMovements;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;

    public OtherIncomeExpenseView(Frame parent) {
        super(parent, "Gestión de Otros Ingresos/Gastos", true);
        initializeComponents();
        setLocationRelativeTo(parent);
        setSize(900, 600);
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // --- Pestaña 1: Formulario ---
        JPanel registerPanel = new JPanel(new MigLayout("", "[grow, fill][grow, fill]", "[][][][][][][][]"));
        registerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        registerPanel.add(new JLabel("Tipo de Movimiento:"), "cell 0 0");
        comboType = new JComboBox<>(MovementType.values());
        registerPanel.add(comboType, "cell 1 0");

        registerPanel.add(new JLabel("Estado del Movimiento:"), "cell 0 1");
        comboStatus = new JComboBox<>(MovementStatus.values());
        registerPanel.add(comboStatus, "cell 1 1");

        registerPanel.add(new JLabel("Evento:"), "cell 0 2");
        comboEvent = new JComboBox<>(new String[]{"Evento 1", "Evento 2", "Evento 3"});
        registerPanel.add(comboEvent, "cell 1 2");

        registerPanel.add(new JLabel("Monto Estimado:"), "cell 0 3");
        txtEstimatedAmount = new JTextField();
        registerPanel.add(txtEstimatedAmount, "cell 1 3");

        registerPanel.add(new JLabel("Monto Pagado:"), "cell 0 4");
        txtPaidAmount = new JTextField();
        registerPanel.add(txtPaidAmount, "cell 1 4");

        registerPanel.add(new JLabel("Fecha de Pago (yyyy-MM-dd):"), "cell 0 5");
        txtPaidDate = new JTextField();
        registerPanel.add(txtPaidDate, "cell 1 5");

        registerPanel.add(new JLabel("Concepto:"), "cell 0 6");
        txtConcept = new JTextField();
        registerPanel.add(txtConcept, "cell 1 6");

        btnRegister = new JButton("Registrar Movimiento");
        btnClear = new JButton("Limpiar Campos");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnClear);
        registerPanel.add(buttonPanel, "cell 0 7 2 1, align right");

        tabbedPane.addTab("Registrar Movimiento", registerPanel);

        // --- Pestaña 2: Tabla con todas las columnas (join) ---
        JPanel listPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(
            new String[]{
                "incexp_id", "event_id", "event_name", "type_id", "type_name",
                "event_inidate", "event_enddate", "event_location", "event_status",
                "incexp_concept", "incexp_amount", "incexp_status",
                "movement_id", "invoice_id", "movement_date", "movement_concept", "movement_amount"
            },
            0
        );
        tableMovements = new JTable(tableModel);
        tableMovements.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // scroll horizontal
        tableMovements.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tableMovements);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Todas las Columnas DB", listPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    // -- Getters para el Controller --
    public JComboBox<MovementType> getComboType() { return comboType; }
    public JComboBox<MovementStatus> getComboStatus() { return comboStatus; }
    public JComboBox<String> getComboEvent() { return comboEvent; }
    public JTextField getTxtEstimatedAmount() { return txtEstimatedAmount; }
    public JTextField getTxtPaidAmount() { return txtPaidAmount; }
    public JTextField getTxtPaidDate() { return txtPaidDate; }
    public JTextField getTxtConcept() { return txtConcept; }
    public JButton getBtnRegister() { return btnRegister; }
    public JButton getBtnClear() { return btnClear; }

    public JTable getTableMovements() { return tableMovements; }
    public DefaultTableModel getTableModel() { return tableModel; }

    /**
     * Actualiza la tabla con los datos (17 columnas) del join.
     */
    public void updateMovementsTable(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}
