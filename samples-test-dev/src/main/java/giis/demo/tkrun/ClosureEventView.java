package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClosureEventView extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTable tableEvents;
    private JPanel panelStatus;
    private JLabel lblAgreementStatus;
    private JLabel lblInvoicesStatus;
    private JLabel lblIncomeExpensesStatus;
    private JLabel lblPendingMovementsStatus;
    private JLabel lblFinalResult;
    private JComboBox<String> filterComboBox;

    private JButton btnCloseEvent;
    private JButton btnForceClose;
    private JButton btnReopen;
    private JButton btnCloseDialog;

    public ClosureEventView() {
        setTitle("Event Closure Management");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 700); // Proportional size, not full screen
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPanel);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        filterComboBox = new JComboBox<>(new String[]{"All Events", "Only Planned", "Only Closed"});
        filterComboBox.setFont(new Font("Arial", Font.PLAIN, 15));
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        contentPanel.add(filterPanel, BorderLayout.NORTH);

        // Events table
        tableEvents = new JTable();
        tableEvents.setFont(new Font("Arial", Font.PLAIN, 15));
        tableEvents.setRowHeight(28);
        JScrollPane scrollEvents = new JScrollPane(tableEvents);
        scrollEvents.setBorder(BorderFactory.createTitledBorder("Events (Planned or Closed)"));
        scrollEvents.setPreferredSize(new Dimension(500, 300));
        contentPanel.add(scrollEvents, BorderLayout.WEST);

        // Dynamic status panel
        panelStatus = new JPanel(new GridLayout(5, 1, 10, 10));
        panelStatus.setBorder(BorderFactory.createTitledBorder("Closure Requirements Status"));

        lblAgreementStatus = createStatusLabel();
        lblInvoicesStatus = createStatusLabel();
        lblIncomeExpensesStatus = createStatusLabel();
        lblPendingMovementsStatus = createStatusLabel();
        lblFinalResult = new JLabel("", SwingConstants.CENTER);
        lblFinalResult.setFont(new Font("SansSerif", Font.BOLD, 16));

        panelStatus.add(lblAgreementStatus);
        panelStatus.add(lblInvoicesStatus);
        panelStatus.add(lblIncomeExpensesStatus);
        panelStatus.add(lblPendingMovementsStatus);
        panelStatus.add(lblFinalResult);

        JScrollPane scrollPanelStatus = new JScrollPane(panelStatus);
        contentPanel.add(scrollPanelStatus, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnCloseEvent = new JButton("Close Event");
        btnForceClose = new JButton("Force Close");
        btnReopen = new JButton("Reopen");
        btnCloseDialog = new JButton("Exit");

        Font btnFont = new Font("Arial", Font.BOLD, 15);
        btnCloseEvent.setFont(btnFont);
        btnForceClose.setFont(btnFont);
        btnReopen.setFont(btnFont);
        btnCloseDialog.setFont(btnFont);

        buttonPanel.add(btnCloseEvent);
        buttonPanel.add(btnForceClose);
        buttonPanel.add(btnReopen);
        buttonPanel.add(btnCloseDialog);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Initial button states
        btnCloseEvent.setEnabled(false);
        btnForceClose.setEnabled(false);
        btnReopen.setEnabled(false);
    }

    private JLabel createStatusLabel() {
        JLabel label = new JLabel("", SwingConstants.LEFT);
        return label;
    }

    public JTable getTableEvents() {
        return tableEvents;
    }

    public void setTableModelEvents(DefaultTableModel model) {
        tableEvents.setModel(model);
        tableEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public JPanel getPanelStatus() {
        return panelStatus;
    }

    public JLabel getLblAgreementStatus() {
        return lblAgreementStatus;
    }

    public JLabel getLblInvoicesStatus() {
        return lblInvoicesStatus;
    }

    public JLabel getLblIncomeExpensesStatus() {
        return lblIncomeExpensesStatus;
    }

    public JLabel getLblPendingMovementsStatus() {
        return lblPendingMovementsStatus;
    }

    public JLabel getLblFinalResult() {
        return lblFinalResult;
    }

    public JButton getBtnCloseEvent() {
        return btnCloseEvent;
    }

    public JButton getBtnForceClose() {
        return btnForceClose;
    }

    public JButton getBtnReopen() {
        return btnReopen;
    }

    public JButton getBtnCloseDialog() {
        return btnCloseDialog;
    }

    public JComboBox<String> getFilterComboBox() {
        return filterComboBox;
    }

    public void clearStatusLabels() {
        lblAgreementStatus.setText("");
        lblInvoicesStatus.setText("");
        lblIncomeExpensesStatus.setText("");
        lblPendingMovementsStatus.setText("");
        lblFinalResult.setText("");
    }

    public void setStatusLabel(JLabel label, boolean ok, String okMessage, String failMessage) {
        String prefix = ok ? "✔ " : "X ";
        label.setText(prefix + (ok ? okMessage : failMessage));
        label.setForeground(ok ? new Color(0, 128, 0) : Color.RED);
    }

    public void clearClosurePanel() {
        panelStatus.removeAll();
        lblAgreementStatus.setText("");
        lblInvoicesStatus.setText("");
        lblIncomeExpensesStatus.setText("");
        lblPendingMovementsStatus.setText("");
        lblFinalResult.setText("");

        panelStatus.add(lblAgreementStatus);
        panelStatus.add(lblInvoicesStatus);
        panelStatus.add(lblIncomeExpensesStatus);
        panelStatus.add(lblPendingMovementsStatus);
        panelStatus.add(lblFinalResult);

        panelStatus.revalidate();
        panelStatus.repaint();
    }
}
