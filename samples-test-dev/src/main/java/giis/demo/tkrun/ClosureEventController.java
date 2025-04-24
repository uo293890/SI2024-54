package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.time.LocalDate; // Import LocalDate
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClosureEventController {
    private final CloseEventModel model;
    private final ClosureEventView view;
    private final LocalDate workingDate; // Add the workingDate field

    // Modify constructor to accept workingDate
    public ClosureEventController(CloseEventModel model, ClosureEventView view, LocalDate workingDate) {
        this.model = Objects.requireNonNull(model, "CloseEventModel cannot be null");
        this.view = Objects.requireNonNull(view, "ClosureEventView cannot be null");
        this.workingDate = Objects.requireNonNull(workingDate, "Working date cannot be null"); // Initialize the field
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

        view.getBtnCloseEvent().addActionListener(e -> closeEvent(false));
        view.getBtnForceClose().addActionListener(e -> closeEvent(true));
        view.getBtnReopen().addActionListener(e -> reopenEvent());
        view.getBtnCloseDialog().addActionListener(e -> view.dispose());
    }

    private void loadFilteredEvents() {
        SwingUtil.exceptionWrapper(() -> {
            String filter = (String) view.getFilterComboBox().getSelectedItem();

            DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"ID", "Type", "Name", "Start Date", "End Date", "Location", "Status"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };

            List<Object[]> events = model.getEventsToClose();
            for (Object[] row : events) {
                String status = row[6].toString();

                boolean matchesFilter = false;
                if ("All Events".equals(filter)) {
                    matchesFilter = true;
                } else if ("Only Closed".equals(filter) && "Closed".equalsIgnoreCase(status)) {
                    matchesFilter = true;
                } else if ("Only Planned".equals(filter) && "Planned".equalsIgnoreCase(status)) {
                    matchesFilter = true;
                }

                if (matchesFilter) {
                    tableModel.addRow(row);
                }
            }
            view.setTableModelEvents(tableModel);
        });
    }

    private int getSelectedEventId() {
        int selectedRow = view.getTableEvents().getSelectedRow();
        if (selectedRow == -1) return -1;
        // Assuming ID is in the first column (index 0)
        Object idValue = view.getTableEvents().getModel().getValueAt(selectedRow, 0);
        if (idValue instanceof Integer) {
            return (int) idValue;
        } else {
            // Handle cases where ID might be a String or another type initially
            try {
                return Integer.parseInt(idValue.toString());
            } catch (NumberFormatException e) {
                showError("Invalid event ID format: " + idValue);
                return -1; // Indicate invalid ID
            }
        }
    }


    private void updateStatusPanel() {
        int eventId = getSelectedEventId();
        if (eventId == -1) {
            view.clearClosurePanel();
            return;
        }

        SwingUtil.exceptionWrapper(() -> {
            int selectedRow = view.getTableEvents().getSelectedRow();
            // Assuming status is in the 7th column (index 6)
            String currentStatus = view.getTableEvents().getModel().getValueAt(selectedRow, 6).toString();

            boolean agreementsOk = model.allAgreementsClosedOrPaid(eventId);
            boolean invoicesOk = model.allInvoicesGenerated(eventId);
            boolean incomeOk = model.allIncomesExpensesPaid(eventId);
            boolean noMovements = !model.hasPendingMovements(eventId);

            view.setStatusLabel(view.getLblAgreementStatus(), agreementsOk,
                "All agreements are paid or closed.", "Some agreements are not paid or closed.");
            view.setStatusLabel(view.getLblInvoicesStatus(), invoicesOk,
                "All invoices have been generated.", "Some invoices are missing.");
            view.setStatusLabel(view.getLblIncomeExpensesStatus(), incomeOk,
                "All incomes/expenses are marked as paid.", "Some incomes/expenses are still pending.");
            view.setStatusLabel(view.getLblPendingMovementsStatus(), noMovements,
                "No pending financial movements.", "There are pending financial movements.");

            boolean isReadyToClose = agreementsOk && invoicesOk && incomeOk && noMovements;

            if ("Closed".equalsIgnoreCase(currentStatus)) {
                view.getLblFinalResult().setText("Event is currently Closed.");
                view.getLblFinalResult().setForeground(Color.BLUE);
                view.getBtnCloseEvent().setEnabled(false);
                view.getBtnForceClose().setEnabled(false);
                view.getBtnReopen().setEnabled(true);
            } else {
                view.getBtnReopen().setEnabled(false);

                if (isReadyToClose) {
                    view.getLblFinalResult().setText("✔ All conditions met for closure.");
                    view.getLblFinalResult().setForeground(new java.awt.Color(0, 128, 0));
                    view.getBtnCloseEvent().setEnabled(true);
                    view.getBtnForceClose().setEnabled(false);
                } else {
                    view.getLblFinalResult().setText("X Closure blocked due to unresolved issues.");
                    view.getLblFinalResult().setForeground(java.awt.Color.RED);
                    view.getBtnCloseEvent().setEnabled(false);
                    view.getBtnForceClose().setEnabled(true);
                }
            }
        });
    }

    private void closeEvent(boolean forced) {
        int eventId = getSelectedEventId();
        if (eventId == -1) return;

        SwingUtil.exceptionWrapper(() -> {
            String currentStatus;
            try {
                currentStatus = model.getEventStatus(eventId); // Wrap in try-catch
            } catch (SQLException e) {
                showError("Error retrieving event status: " + e.getMessage());
                return; // Stop processing on error
            }

            if ("Closed".equalsIgnoreCase(currentStatus)) {
                showError("This event is already closed.");
                loadFilteredEvents();
                view.clearClosurePanel();
                return;
            }

            boolean isReadyToClose = model.allAgreementsClosedOrPaid(eventId) &&
                                     model.allInvoicesGenerated(eventId) &&
                                     model.allIncomesExpensesPaid(eventId) &&
                                     !model.hasPendingMovements(eventId);

            if (!forced && !isReadyToClose) {
                showError("Event is not ready for standard closure. Please check requirements.");
                updateStatusPanel(); // Refresh status display
                return;
            }

            if (forced && !isReadyToClose) {
                List<String> issues = new ArrayList<>();
                if (!model.allAgreementsClosedOrPaid(eventId)) issues.add("Some agreements are not paid or closed.");
                if (!model.allInvoicesGenerated(eventId)) issues.add("Some agreements are missing invoices.");
                if (!model.allIncomesExpensesPaid(eventId)) issues.add("Some incomes or expenses are still pending.");
                if (model.hasPendingMovements(eventId)) issues.add("There are pending financial movements.");

                String issueMessage = "The following issues exist:\n" + String.join("\n", issues) + "\n\nAre you sure you want to force close this event?";
                int confirm = JOptionPane.showConfirmDialog(view, issueMessage, "Force Close Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            model.closeEvent(eventId);

            if (forced && !isReadyToClose) {
                JOptionPane.showMessageDialog(view, "Event forcefully closed with issues.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "✔ Event closed successfully.", "Closed", JOptionPane.INFORMATION_MESSAGE);
            }

            loadFilteredEvents();
            view.clearClosurePanel();
        });
    }

    private void reopenEvent() {
        int eventId = getSelectedEventId();
        if (eventId == -1) return;

        SwingUtil.exceptionWrapper(() -> {
             String currentStatus;
             try {
                currentStatus = model.getEventStatus(eventId); // Wrap in try-catch
             } catch (SQLException e) {
                showError("Error retrieving event status: " + e.getMessage());
                return; // Stop processing on error
             }

            if ("Planned".equalsIgnoreCase(currentStatus)) {
                showError("This event is already planned and cannot be reopened.");
                loadFilteredEvents();
                view.clearClosurePanel();
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to reopen this event?", "Reopen Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            model.reopenEvent(eventId);

            JOptionPane.showMessageDialog(view, "Event has been reopened.", "Reopened", JOptionPane.INFORMATION_MESSAGE);

            loadFilteredEvents();
            view.clearClosurePanel();
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // You can now access the workingDate within the controller, for example:
    /*
    private void checkDateRelatedCondition(int eventId) {
        // Example usage: Compare event end date to workingDate
        LocalDate eventEndDate = model.getEventEndDate(eventId); // Assuming model has this method
        if (workingDate.isBefore(eventEndDate)) {
            // Logic if working date is before event end date
        }
    }
    */
}