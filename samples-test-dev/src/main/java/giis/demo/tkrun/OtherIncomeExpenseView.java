package giis.demo.tkrun;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Vista para registrar otros ingresos o gastos.
 * Simula el prototipo: Type, Event, Amount (€), Concept, y botones Cancel/Register.
 */
public class OtherIncomeExpenseView extends JDialog {

    // -- 1) Combo para seleccionar el tipo: None / INCOME / EXPENSE
    private JComboBox<String> comboType;

    // -- 2) Combo para seleccionar el evento (p.ej. "Anniversary")
    private JComboBox<String> comboEvent;

    // -- 3) Campo para el monto (Amount)
    private JTextField txtAmount;
    private JLabel lblCurrency; // etiqueta "€"

    // -- 4) Campo para el concepto
    private JTextField txtConcept;

    // -- 5) Botones Cancel y Register
    private JButton btnCancel;
    private JButton btnRegister;

    public OtherIncomeExpenseView(Frame parent) {
        super(parent, "Register other income or expenses", true);
        initComponents();
        setLocationRelativeTo(parent);
        setSize(400, 250); // tamaño aproximado
    }

    private void initComponents() {
        // Layout MIG: 2 columnas, filas dinámicas
        JPanel mainPanel = new JPanel(new MigLayout("", "[grow,fill][grow,fill]", "[][][][][]"));
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));

        // 1) Label y combo "Type"
        mainPanel.add(new JLabel("Type:"), "cell 0 0");
        comboType = new JComboBox<>(new String[] { "--None--", "INCOME", "EXPENSE" });
        mainPanel.add(comboType, "cell 1 0");

        // 2) Label y combo "Event"
        mainPanel.add(new JLabel("Event:"), "cell 0 1");
        comboEvent = new JComboBox<>();
        // Se llenará desde el controlador con la lista de eventos
        mainPanel.add(comboEvent, "cell 1 1");

        // 3) Label y campo "Amount" con etiqueta "€"
        mainPanel.add(new JLabel("Amount:"), "cell 0 2");
        // Usamos un panel para Amount y el label "€"
        JPanel amountPanel = new JPanel(new BorderLayout());
        txtAmount = new JTextField();
        lblCurrency = new JLabel("€");
        amountPanel.add(txtAmount, BorderLayout.CENTER);
        amountPanel.add(lblCurrency, BorderLayout.EAST);
        mainPanel.add(amountPanel, "cell 1 2, growx");

        // 4) Label y campo "Concept"
        mainPanel.add(new JLabel("Concept:"), "cell 0 3");
        txtConcept = new JTextField();
        mainPanel.add(txtConcept, "cell 1 3");

        // 5) Panel con botones Cancel y Register
        btnCancel = new JButton("Cancel");
        btnRegister = new JButton("Register");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnRegister);

        mainPanel.add(buttonPanel, "cell 0 4 2 1, align right");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // Getters para el controlador
    public JComboBox<String> getComboType() {
        return comboType;
    }

    public JComboBox<String> getComboEvent() {
        return comboEvent;
    }

    public JTextField getTxtAmount() {
        return txtAmount;
    }

    public JTextField getTxtConcept() {
        return txtConcept;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }
}
