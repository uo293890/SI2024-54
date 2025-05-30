package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ConsultActivityStatusController {
    private ConsultActivityStatusModel model;
    private ConsultActivityStatusView view;

    public ConsultActivityStatusController(ConsultActivityStatusModel model, 
                                                 ConsultActivityStatusView view) {
        this.model = model;
        this.view = view;
        this.initView();
        this.initController();
    }

    private void initView() {
        loadEventsTable();
    }

    private void initController() {
        view.getEventsTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SwingUtil.exceptionWrapper(() -> {
                    if (!e.getValueIsAdjusting()) {
                        int selectedRow = view.getEventsTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            String eventName = (String) view.getEventsTableModel().getValueAt(selectedRow, 0);
                            int eventId = getEventIdByName(eventName);
                            updateFinancialData(eventId);
                        }
                    }
                });
            }
        });
    }

    private void loadEventsTable() {
        List<ConsultActivityStatusDTO> events = model.getAllEvents();
        DefaultTableModel tableModel = view.getEventsTableModel();
        tableModel.setRowCount(0); // Clear existing data
        
        for (ConsultActivityStatusDTO event : events) {
            tableModel.addRow(new Object[]{
                event.getEventName(),
                event.getEventInidate(),
                event.getEventEnddate(),
                event.getEventStatus(),
                event.getTypeName()
            });
        }
    }

    private int getEventIdByName(String eventName) {
        List<ConsultActivityStatusDTO> events = model.getAllEvents();
        for (ConsultActivityStatusDTO event : events) {
            if (event.getEventName().equals(eventName)) {
                return event.getEventId();
            }
        }
        return -1;
    }

    private void updateFinancialData(int eventId) {
        updateSponsorsTable(eventId);
        updateIncomesTable(eventId);
        updateExpensesTable(eventId);
        updateFinancialSummaries(eventId);
    }

    private void updateSponsorsTable(int eventId) {
        List<ConsultActivityStatusDTO> sponsors = model.getSponsorsForEvent(eventId);
        DefaultTableModel tableModel = view.getSponsorsTableModel();
        tableModel.setRowCount(0);
        
        for (ConsultActivityStatusDTO sponsor : sponsors) {
            tableModel.addRow(new Object[]{
                sponsor.getSponsorName(),
                sponsor.getLevelName(),
                sponsor.getAgreementAmount(),
                sponsor.getAgreementStatus(),
                sponsor.getAgreementDate()
            });
        }
    }

    private void updateIncomesTable(int eventId) {
        List<ConsultActivityStatusDTO> incomes = model.getIncomesForEvent(eventId);
        DefaultTableModel tableModel = view.getIncomesTableModel();
        tableModel.setRowCount(0);
        
        for (ConsultActivityStatusDTO income : incomes) {
            tableModel.addRow(new Object[]{
                income.getIncexpConcept(),
                String.format("€%.2f", income.getIncexpAmount()),
                income.getIncexpStatus()
            });
        }
    }

    private void updateExpensesTable(int eventId) {
        List<ConsultActivityStatusDTO> expenses = model.getExpensesForEvent(eventId);
        DefaultTableModel tableModel = view.getExpensesTableModel();
        tableModel.setRowCount(0);
        
        for (ConsultActivityStatusDTO expense : expenses) {
            tableModel.addRow(new Object[]{
                expense.getIncexpConcept(),
                String.format("€%.2f", expense.getIncexpAmount()),
                expense.getIncexpStatus()
            });
        }
    }

    private void updateFinancialSummaries(int eventId) {
        // Get estimated values
        double sponsorsEstimated = model.getSponsorsEstimatedForEvent(eventId);
        double incomesEstimated = model.getIncomesEstimatedForEvent(eventId);
        double expensesEstimated = model.getExpensesEstimatedForEvent(eventId);
        
        // Get paid values
        double sponsorsPaid = model.getSponsorsPaidForEvent(eventId);
        double incomesPaid = model.getIncomesPaidForEvent(eventId);
        double expensesPaid = model.getExpensesPaidForEvent(eventId);
        
        // Update view
        view.updateFinancialData(
            sponsorsEstimated, sponsorsPaid,
            incomesEstimated, incomesPaid,
            expensesEstimated, expensesPaid
        );
    }
}