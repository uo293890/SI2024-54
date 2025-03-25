package giis.demo.tkrun;

import giis.demo.util.SwingUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;

public class ClosureEventController {
    private final CloseEventModel model;
    private final ClosureEventView view;

    public ClosureEventController(CloseEventModel model, ClosureEventView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        loadFilteredEvents();

        view.getFilterComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadFilteredEvents();
                view.clearClosurePanel();
            }
        });

        view.getTableEvents().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateStatusPanel();
            }
        });

        //view.getBtnCheckClosure().addActionListener(e -> updateStatusPanel());

        view.getBtnCloseEvent().addActionListener(e -> {
            int row = view.getTableEvents().getSelectedRow();
            if (row == -1) return;
            String status = view.getTableEvents().getValueAt(row, 4).toString();
            if ("Closed".equalsIgnoreCase(status)) {
                showError("This event is already closed.");
                return;
            }
            int eventId = (int) view.getTableEvents().getValueAt(row, 0);
            model.closeEvent(eventId);
            JOptionPane.showMessageDialog(view, "✔ Event closed successfully.", "Closed", JOptionPane.INFORMATION_MESSAGE);
            loadFilteredEvents();
            view.clearClosurePanel();
        });

        view.getBtnForceClose().addActionListener(e -> {
            int row = view.getTableEvents().getSelectedRow();
            if (row == -1) return;
            String status = view.getTableEvents().getValueAt(row, 4).toString();
            if ("Closed".equalsIgnoreCase(status)) {
                showError("This event is already closed.");
                return;
            }
            int eventId = (int) view.getTableEvents().getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(view, "There are pending issues. Are you sure you want to force close this event?", "Force Close Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.closeEvent(eventId);
                JOptionPane.showMessageDialog(view, "Event forcefully closed with issues.", "Warning", JOptionPane.WARNING_MESSAGE);
                loadFilteredEvents();
                view.clearClosurePanel();
            }
        });

        view.getBtnReopen().addActionListener(e -> {
            int row = view.getTableEvents().getSelectedRow();
            if (row == -1) return;
            String status = view.getTableEvents().getValueAt(row, 4).toString();
            if ("Planned".equalsIgnoreCase(status)) {
                showError("Event is already planned and cannot be reopened.");
                return;
            }
            int eventId = (int) view.getTableEvents().getValueAt(row, 0);
            model.reopenEvent(eventId);
            JOptionPane.showMessageDialog(view, "Event has been reopened.", "Reopened", JOptionPane.INFORMATION_MESSAGE);
            loadFilteredEvents();
            view.clearClosurePanel();
        });

        view.getBtnCloseDialog().addActionListener(e -> view.dispose());
    }

    private void loadFilteredEvents() {
        String filter = (String) view.getFilterComboBox().getSelectedItem();

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Start Date", "End Date", "Status"}, 0);
        for (Object[] row : model.getEventsToClose()) {
            String status = row[4].toString();
            if ("Only Closed".equals(filter) && !"Closed".equalsIgnoreCase(status)) continue;
            if ("Only Planned".equals(filter) && !"Planned".equalsIgnoreCase(status)) continue;
            tableModel.addRow(row);
        }
        view.setTableModelEvents(tableModel);
    }

    private int getSelectedEventId() {
        int selectedRow = view.getTableEvents().getSelectedRow();
        if (selectedRow == -1) return -1;
        return (int) view.getTableEvents().getValueAt(selectedRow, 0);
    }

    private void updateStatusPanel() {
        int eventId = getSelectedEventId();
        if (eventId == -1) return;

        boolean agreementsOk = model.allAgreementsClosedOrPaid(eventId);
        boolean invoicesOk = model.allInvoicesGenerated(eventId);
        boolean incomeOk = model.allIncomesExpensesPaid(eventId);
        boolean noMovements = !model.hasPendingMovements(eventId);

        view.setStatusLabel(view.getLblAgreementStatus(), agreementsOk, "All agreements are paid or closed.", "Some agreements are not paid or closed.");
        view.setStatusLabel(view.getLblInvoicesStatus(), invoicesOk, "All invoices have been generated.", "Some invoices are missing.");
        view.setStatusLabel(view.getLblIncomeExpensesStatus(), incomeOk, "All income/expenses marked as paid.", "Some income/expenses are still estimated.");
        view.setStatusLabel(view.getLblPendingMovementsStatus(), noMovements, "No pending financial movements.", "There are pending financial movements.");

        if (agreementsOk && invoicesOk && incomeOk && noMovements) {
            view.getLblFinalResult().setText("✔ All closure conditions met.");
            view.getLblFinalResult().setForeground(new java.awt.Color(0, 128, 0));
            view.getBtnCloseEvent().setEnabled(true);
            view.getBtnForceClose().setEnabled(false);
        } else {
            view.getLblFinalResult().setText("X Closure blocked due to unresolved issues.");
            view.getLblFinalResult().setForeground(java.awt.Color.RED);
            view.getBtnCloseEvent().setEnabled(false);
            view.getBtnForceClose().setEnabled(true);
        }

        view.getBtnReopen().setEnabled(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
