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

    private JButton btnCheckClosure;
    private JButton btnCloseEvent;
    private JButton btnForceClose;
    private JButton btnReopen;
    private JButton btnCloseDialog;

    public ClosureEventView() {
        setTitle("Event Closure Management");
        setModal(true);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(contentPanel);

        // Events table
        tableEvents = new JTable();
        JScrollPane scrollEvents = new JScrollPane(tableEvents);
        scrollEvents.setBorder(BorderFactory.createTitledBorder("Planned or Closed Events"));
        scrollEvents.setPreferredSize(new Dimension(850, 180));
        contentPanel.add(scrollEvents, BorderLayout.NORTH);

        // Dynamic status panel
        panelStatus = new JPanel(new GridLayout(5, 1, 10, 10));
        panelStatus.setBorder(BorderFactory.createTitledBorder("Closure Requirements Status"));

        lblAgreementStatus = createStatusLabel();
        lblInvoicesStatus = createStatusLabel();
        lblIncomeExpensesStatus = createStatusLabel();
        lblPendingMovementsStatus = createStatusLabel();
        lblFinalResult = new JLabel("", SwingConstants.CENTER);
        lblFinalResult.setFont(new Font("SansSerif", Font.BOLD, 14));

        panelStatus.add(lblAgreementStatus);
        panelStatus.add(lblInvoicesStatus);
        panelStatus.add(lblIncomeExpensesStatus);
        panelStatus.add(lblPendingMovementsStatus);
        panelStatus.add(lblFinalResult);

        JScrollPane scrollPanelStatus = new JScrollPane(panelStatus);
        scrollPanelStatus.setPreferredSize(new Dimension(850, 200));
        contentPanel.add(scrollPanelStatus, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnCheckClosure = new JButton("Check Criteria");
        btnCloseEvent = new JButton("Close Event");
        btnForceClose = new JButton("Force Close");
        btnReopen = new JButton("Reopen");
        btnCloseDialog = new JButton("Exit");

        buttonPanel.add(btnCheckClosure);
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
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
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

    public JButton getBtnCheckClosure() {
        return btnCheckClosure;
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

    public void clearStatusLabels() {
        lblAgreementStatus.setText("");
        lblInvoicesStatus.setText("");
        lblIncomeExpensesStatus.setText("");
        lblPendingMovementsStatus.setText("");
        lblFinalResult.setText("");
    }

    public void setStatusLabel(JLabel label, boolean ok, String okMessage, String failMessage) {
        if (ok) {
            label.setText("✅ " + okMessage);
            label.setForeground(new Color(0, 128, 0));
        } else {
            label.setText("❌ " + failMessage);
            label.setForeground(Color.RED);
        }
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
