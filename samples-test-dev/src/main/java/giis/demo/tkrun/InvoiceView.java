package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InvoiceView extends JFrame {

    private JTextField txtInvoiceNumber;
    private JTextField txtRecipientName;
    private JTextField txtRecipientTaxId;
    private JTextField txtRecipientAddress;
    private JTextField txtContactEmail;
    private JTextField txtInvoiceDate;
    private JTextField txtEventDate;
    private JTextField txtAgreementId;
    private JTextField txtInvoiceVat;

    private JButton btnGenerate;
    private JButton btnSend;

    private JTable invoicesTable;
    private DefaultTableModel invoicesTableModel;

    private JTable availableIdsTable;
    private DefaultTableModel availableIdsTableModel;

    public InvoiceView() {
        setTitle("Generar y Enviar Facturas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Generar y Enviar Facturas a Patrocinadores"));

        txtInvoiceNumber = new JTextField();
        txtInvoiceNumber.setPreferredSize(new Dimension(200, 30));
        txtInvoiceNumber.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtInvoiceNumber.setToolTipText("ID de la Factura (puede ingresarse manualmente o seleccionarse de la tabla)");

        txtRecipientName = new JTextField();
        txtRecipientName.setPreferredSize(new Dimension(200, 30));
        txtRecipientName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtRecipientName.setToolTipText("Nombre del Destinatario");

        txtRecipientTaxId = new JTextField();
        txtRecipientTaxId.setPreferredSize(new Dimension(200, 30));
        txtRecipientTaxId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtRecipientTaxId.setToolTipText("NIF/CIF del destinatario");

        txtRecipientAddress = new JTextField();
        txtRecipientAddress.setPreferredSize(new Dimension(200, 30));
        txtRecipientAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtRecipientAddress.setToolTipText("Dirección Fiscal del destinatario");

        txtContactEmail = new JTextField();
        txtContactEmail.setPreferredSize(new Dimension(200, 30));
        txtContactEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtContactEmail.setToolTipText("Correo Electrónico de Contacto");

        txtInvoiceDate = new JTextField();
        txtInvoiceDate.setPreferredSize(new Dimension(200, 30));
        txtInvoiceDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtInvoiceDate.setToolTipText("Fecha de la Factura (dd/MM/yyyy)");

        txtEventDate = new JTextField();
        txtEventDate.setPreferredSize(new Dimension(200, 30));
        txtEventDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtEventDate.setToolTipText("Fecha del Evento (dd/MM/yyyy) - Opcional");

        txtAgreementId = new JTextField();
        txtAgreementId.setPreferredSize(new Dimension(200, 30));
        txtAgreementId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtAgreementId.setToolTipText("ID del Acuerdo");

        txtInvoiceVat = new JTextField();
        txtInvoiceVat.setPreferredSize(new Dimension(200, 30));
        txtInvoiceVat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtInvoiceVat.setToolTipText("IVA de la Factura");

        btnGenerate = new JButton("Generar/Registrar Factura");
        btnSend = new JButton("Mandar Factura");

        formPanel.add(createFieldPanel("ID de la Factura:", txtInvoiceNumber));
        formPanel.add(createFieldPanel("Nombre del Destinatario:", txtRecipientName));
        formPanel.add(createFieldPanel("NIF/CIF:", txtRecipientTaxId));
        formPanel.add(createFieldPanel("Dirección Fiscal:", txtRecipientAddress));
        formPanel.add(createFieldPanel("Correo Electrónico:", txtContactEmail));
        formPanel.add(createFieldPanel("Fecha de Factura:", txtInvoiceDate));
        formPanel.add(createFieldPanel("Fecha del Evento (Opcional):", txtEventDate));
        formPanel.add(createFieldPanel("ID del Acuerdo:", txtAgreementId));
        formPanel.add(createFieldPanel("IVA de la Factura:", txtInvoiceVat));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.add(btnGenerate);
        buttonsPanel.add(btnSend);
        formPanel.add(buttonsPanel);

        JPanel invoicesPanel = new JPanel(new BorderLayout());
        invoicesPanel.setBorder(BorderFactory.createTitledBorder("Facturas Generadas"));
        invoicesTableModel = new DefaultTableModel(new Object[]{
                "ID Factura", "Nombre", "NIF/CIF", "Dirección", "Correo Electrónico", "Fecha Factura", "Fecha Envío", "Acciones"
        }, 0);
        invoicesTable = new JTable(invoicesTableModel);
        JScrollPane invoicesScroll = new JScrollPane(invoicesTable);
        invoicesPanel.add(invoicesScroll, BorderLayout.CENTER);

        JPanel idsPanel = new JPanel(new BorderLayout());
        idsPanel.setBorder(BorderFactory.createTitledBorder("ID Disponibles (Vista tipo Excel)"));
        availableIdsTableModel = new DefaultTableModel(new Object[]{"ID", "Descripción"}, 0);
        availableIdsTable = new JTable(availableIdsTableModel);
        // Se elimina la carga estática de datos para que se actualice de forma dinámica.
        JScrollPane idsScroll = new JScrollPane(availableIdsTable);
        idsPanel.add(idsScroll, BorderLayout.CENTER);

        // Al seleccionar una fila, se establece el ID en el campo de la factura.
        availableIdsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = availableIdsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object id = availableIdsTableModel.getValueAt(selectedRow, 0);
                    txtInvoiceNumber.setText(id.toString());
                }
            }
        });

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(invoicesPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(idsPanel);

        add(mainPanel);
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(180, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // Getter para acceder a la tabla de facturas
    public JTable getInvoicesTable() {
        return invoicesTable;
    }

    public JButton getGenerateButton() {
        return btnGenerate;
    }

    public JButton getSendButton() {
        return btnSend;
    }

    public String getInvoiceNumber() {
        return txtInvoiceNumber.getText();
    }
    public void setInvoiceNumber(String number) {
        txtInvoiceNumber.setText(number);
    }

    public String getInvoiceDate() {
        return txtInvoiceDate.getText();
    }
    public void setInvoiceDate(String date) {
        txtInvoiceDate.setText(date);
    }

    public String getRecipientName() {
        return txtRecipientName.getText();
    }

    public String getRecipientTaxId() {
        return txtRecipientTaxId.getText();
    }

    public String getRecipientAddress() {
        return txtRecipientAddress.getText();
    }

    public String getContactEmail() {
        return txtContactEmail.getText();
    }

    public String getEventDate() {
        return txtEventDate.getText();
    }

    public String getAgreementId() {
        return txtAgreementId.getText();
    }

    public String getInvoiceVat() {
        return txtInvoiceVat.getText();
    }

    public void addInvoiceToTable(String id, String name, String taxId, String address, String email, String invoiceDate, String sentDate) {
        invoicesTableModel.addRow(new Object[]{id, name, taxId, address, email, invoiceDate, sentDate, "Enviar"});
    }

    /**
     * Actualiza la tabla de ID Disponibles con nuevos datos.
     * @param data Matriz de datos, donde cada fila es un array con el ID y su descripción.
     */
    public void updateAvailableIdsTable(Object[][] data) {
        availableIdsTableModel.setRowCount(0);  // Limpia la tabla
        for (Object[] row : data) {
            availableIdsTableModel.addRow(row);
        }
    }
    
    
    

    /**
     * Método getter para acceder a la tabla de IDs disponibles.
     */
    public JTable getAvailableIdsTable() {
        return availableIdsTable;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearForm() {
        txtInvoiceNumber.setText("");
        txtRecipientName.setText("");
        txtRecipientTaxId.setText("");
        txtRecipientAddress.setText("");
        txtContactEmail.setText("");
        txtInvoiceDate.setText("");
        txtEventDate.setText("");
        txtAgreementId.setText("");
        txtInvoiceVat.setText("");
    }
}
