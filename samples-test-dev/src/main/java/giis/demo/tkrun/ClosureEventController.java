package giis.demo.tkrun;

import giis.demo.util.SwingUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClosureEventController {
    private final CloseEventModel model;
    private final ClosureEventView view;

    public ClosureEventController(CloseEventModel model, ClosureEventView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        loadPlannedEvents();

        view.getTableEvents().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateStatusPanel();
            }
        });

        view.getBtnCheckClosure().addActionListener(e -> updateStatusPanel());

        view.getBtnCloseEvent().addActionListener(e -> {
            int eventId = getSelectedEventId();
            if (eventId == -1) return;
            model.closeEvent(eventId);
            JOptionPane.showMessageDialog(view, "✅ Event closed successfully.", "Closed", JOptionPane.INFORMATION_MESSAGE);
            loadPlannedEvents();
            view.clearStatusLabels();
        });

        view.getBtnForceClose().addActionListener(e -> {
            int eventId = getSelectedEventId();
            if (eventId == -1) return;
            int confirm = JOptionPane.showConfirmDialog(view, "There are pending issues. Are you sure you want to force close this event?", "Force Close Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.closeEvent(eventId);
                JOptionPane.showMessageDialog(view, "⚠️ Event forcefully closed with issues.", "Warning", JOptionPane.WARNING_MESSAGE);
                loadPlannedEvents();
                view.clearStatusLabels();
            }
        });

        view.getBtnReopen().addActionListener(e -> {
            int eventId = getSelectedEventId();
            if (eventId == -1) return;
            model.reopenEvent(eventId);
            JOptionPane.showMessageDialog(view, "🔓 Event has been reopened.", "Reopened", JOptionPane.INFORMATION_MESSAGE);
            loadPlannedEvents();
            view.clearStatusLabels();
        });

        view.getBtnCloseDialog().addActionListener(e -> view.dispose());
    }

    private void loadPlannedEvents() {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Start Date", "End Date", "Status"}, 0);
        for (Object[] row : model.getEventsToClose()) {
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
            view.getLblFinalResult().setText("✅ All closure conditions met.");
            view.getLblFinalResult().setForeground(new java.awt.Color(0, 128, 0));
            view.getBtnCloseEvent().setEnabled(true);
            view.getBtnForceClose().setEnabled(false);
        } else {
            view.getLblFinalResult().setText("⚠️ Closure blocked due to unresolved issues.");
            view.getLblFinalResult().setForeground(java.awt.Color.RED);
            view.getBtnCloseEvent().setEnabled(false);
            view.getBtnForceClose().setEnabled(true);
        }

        view.getBtnReopen().setEnabled(true);
    }
}