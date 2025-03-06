package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class InvoiceView extends JDialog {
    private JComboBox<Integer> cmbActivities;
    private JComboBox<Integer> cmbSponsors;
    private JTextField txtAmount;
    private JTextField txtTaxId;
    private JButton btnGenerate;
    private JButton btnSend;
    private JTable invoiceTable;
    private DefaultTableModel tableModel;

    public InvoiceView(JFrame parent) {
        super(parent, "Invoice Management", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);

        // Panel superior con controles
        JPanel controlPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        controlPanel.add(new JLabel("Activity:"));
        cmbActivities = new JComboBox<>();
        controlPanel.add(cmbActivities);
        
        controlPanel.add(new JLabel("Sponsor:"));
        cmbSponsors = new JComboBox<>();
        controlPanel.add(cmbSponsors);
        
        controlPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        controlPanel.add(txtAmount);
        
        controlPanel.add(new JLabel("Tax ID:"));
        txtTaxId = new JTextField();
        controlPanel.add(txtTaxId);
        
        btnGenerate = new JButton("Generate Invoice");
        controlPanel.add(btnGenerate);
        
        btnSend = new JButton("Send Invoice");
        controlPanel.add(btnSend);
        
        add(controlPanel, BorderLayout.NORTH);

        // Tabla de facturas
        tableModel = new DefaultTableModel(
            new Object[]{"Invoice Number", "Date", "Sponsor", "Amount", "Status"}, 0);
        invoiceTable = new JTable(tableModel);
        add(new JScrollPane(invoiceTable), BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
    }

    public void loadSponsors(List<Integer> sponsorIds) {
        sponsorIds.forEach(id -> cmbSponsors.addItem(id));
    }

    public void loadActivities(List<Integer> activityIds) {
        activityIds.forEach(id -> cmbActivities.addItem(id));
    }

    public void refreshInvoices(List<InvoiceDTO> invoices) {
        tableModel.setRowCount(0);
        for(InvoiceDTO invoice : invoices) {
            tableModel.addRow(new Object[]{
                invoice.getInvoiceNumber(),
                invoice.getInvoiceDate(),
                invoice.getRecipientName(),
                invoice.getTotalAmount(),
                invoice.getSentDate() == null ? "Pending" : "Sent"
            });
        }
    }

    // Getters
    public JButton getBtnGenerate() { return btnGenerate; }
    public JButton getBtnSend() { return btnSend; }
    public int getSelectedActivity() { return (int) cmbActivities.getSelectedItem(); }
    public int getSelectedSponsor() { return (int) cmbSponsors.getSelectedItem(); }
    public double getAmount() { return Double.parseDouble(txtAmount.getText()); }
    public String getTaxId() { return txtTaxId.getText(); }

	public int getSelectedActivityId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSelectedSponsorId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void refreshData() {
		// TODO Auto-generated method stub
		
	}
}