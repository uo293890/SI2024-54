package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

// Import necessary AWT classes for GridBagLayout
// Explicitly import GridBagBagConstraints
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
        setSize(1100, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        setResizable(true);

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
        tableEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollEvents = new JScrollPane(tableEvents);
        scrollEvents.setBorder(BorderFactory.createTitledBorder("Events (Planned or Closed)"));
        scrollEvents.setPreferredSize(new Dimension(600, 300));
        contentPanel.add(scrollEvents, BorderLayout.WEST);

        // Dynamic status panel
        panelStatus = new JPanel(new GridBagLayout());
        panelStatus.setBorder(BorderFactory.createTitledBorder("Closure Requirements Status"));
        GridBagConstraints gbcStatus = new GridBagConstraints(); // Use the imported class
        gbcStatus.insets = new Insets(5, 5, 5, 5);
        gbcStatus.anchor = GridBagConstraints.WEST;
        gbcStatus.fill = GridBagConstraints.HORIZONTAL;
        gbcStatus.weightx = 1.0;

        int row = 0;
        lblAgreementStatus = createStatusLabel();
        lblInvoicesStatus = createStatusLabel();
        lblIncomeExpensesStatus = createStatusLabel();
        lblPendingMovementsStatus = createStatusLabel();
        lblFinalResult = new JLabel("", SwingConstants.CENTER);
        lblFinalResult.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Add labels to the status panel
        gbcStatus.gridx = 0; gbcStatus.gridy = row++; panelStatus.add(lblAgreementStatus, gbcStatus);
        gbcStatus.gridx = 0; gbcStatus.gridy = row++; panelStatus.add(lblInvoicesStatus, gbcStatus);
        gbcStatus.gridx = 0; gbcStatus.gridy = row++; panelStatus.add(lblIncomeExpensesStatus, gbcStatus);
        gbcStatus.gridx = 0; gbcStatus.gridy = row++; panelStatus.add(lblPendingMovementsStatus, gbcStatus);

        // Final result label
        gbcStatus.gridx = 0; gbcStatus.gridy = row++; gbcStatus.gridwidth = 1; gbcStatus.anchor = GridBagConstraints.CENTER; panelStatus.add(lblFinalResult, gbcStatus);


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

        // Clear status labels initially
        clearStatusLabels();
    }

    /** Helper method to create a standard status label */
    private JLabel createStatusLabel() {
        JLabel label = new JLabel("", SwingConstants.LEFT);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    // --- Getters for Controller ---
    public JTable getTableEvents() {
        return tableEvents;
    }

    public void setTableModelEvents(DefaultTableModel model) {
        tableEvents.setModel(model);
        tableEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Optional: Hide the ID column (index 0) if it's in the model
        if (tableEvents.getColumnModel().getColumnCount() > 0) {
            tableEvents.getColumnModel().getColumn(0).setMinWidth(0);
            tableEvents.getColumnModel().getColumn(0).setMaxWidth(0);
            tableEvents.getColumnModel().getColumn(0).setPreferredWidth(0);
            tableEvents.getColumnModel().getColumn(0).setResizable(false);
        }
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

    /**
     * Clears the text of all status labels and the final result label.
     */
    public void clearStatusLabels() {
        lblAgreementStatus.setText("");
        lblInvoicesStatus.setText("");
        lblIncomeExpensesStatus.setText("");
        lblPendingMovementsStatus.setText("");
        lblFinalResult.setText("");
        // Reset colors to default (optional, setStatusLabel handles color)
        lblAgreementStatus.setForeground(Color.BLACK);
        lblInvoicesStatus.setForeground(Color.BLACK);
        lblIncomeExpensesStatus.setForeground(Color.BLACK);
        lblPendingMovementsStatus.setForeground(Color.BLACK);
        lblFinalResult.setForeground(Color.BLACK);
    }

    /**
     * Sets the text and color of a status label based on a boolean check result.
     * @param label The JLabel to update.
     * @param ok The boolean result of the check (true for OK, false for issues).
     * @param okMessage The message to display if the check is OK.
     * @param failMessage The message to display if the check fails.
     */
    public void setStatusLabel(JLabel label, boolean ok, String okMessage, String failMessage) {
        String prefix = ok ? "✔ " : "X ";
        label.setText(prefix + (ok ? okMessage : failMessage));
        label.setForeground(ok ? new Color(0, 128, 0) : Color.RED);
    }

    /**
     * Clears the closure status panel and resets button states to initial (disabled).
     * Called when table selection is cleared.
     */
    public void clearClosurePanel() {
        clearStatusLabels();
        btnCloseEvent.setEnabled(false);
        btnForceClose.setEnabled(false);
        btnReopen.setEnabled(false);
    }
}