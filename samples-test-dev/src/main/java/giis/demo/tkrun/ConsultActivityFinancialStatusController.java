package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ConsultActivityFinancialStatusController {
    private ConsultActivityFinancialStatusModel model;
    private ConsultActivityFinancialStatusView view;

    public ConsultActivityFinancialStatusController(ConsultActivityFinancialStatusModel model, ConsultActivityFinancialStatusView view) {
        this.model = model;
        this.view = view;
        this.initView();
        this.initController();
    }

    private void initView() {
        List<ConsultActivityFinancialStatusDTO> events = model.getAllEvents();
        DefaultTableModel tableModel = (DefaultTableModel) view.getEditionsTable().getModel();
        tableModel.setRowCount(0); // Clear existing data
        for (ConsultActivityFinancialStatusDTO event : events) {
            tableModel.addRow(new Object[]{
                event.getTypeName(),
                event.getEventName(),
                event.getEventInidate(),
                event.getEventEnddate(),
                event.getEventStatus()
            });
        }
    }

    private void initController() {
        view.getEditionsTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = view.getEditionsTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        DefaultTableModel model = (DefaultTableModel) view.getEditionsTable().getModel();
                        int eventId = (int) model.getValueAt(selectedRow, 0);
                        
                        // Update sponsors table
                        updateSponsorsTable(eventId);
                        
                        // Update incomes table
                        updateIncomesTable(eventId);
                        
                        // Update expenses table
                        updateExpensesTable(eventId);
                        
                        // Update financial summaries
                        updateFinancialSummaries(eventId);
                    }
                }
            }
        });
    }
    
    private void updateSponsorsTable(int eventId) {
        List<ConsultActivityFinancialStatusDTO> agreements = model.getSponsorshipsForEvent(eventId);
        DefaultTableModel tableModel = (DefaultTableModel) view.getSponsorsTable().getModel();
        tableModel.setRowCount(0);
        
        for (ConsultActivityFinancialStatusDTO agreement : agreements) {
            tableModel.addRow(new Object[]{
                agreement.getSponsorName(),
                agreement.getLevelName(),
                agreement.getAgreementAmount(),
                agreement.getAgreementStatus(),
                agreement.getAgreementDate()
            });
        }
    }
    
    private void updateIncomesTable(int eventId) {
        List<ConsultActivityFinancialStatusDTO> incomes = model.getIncomesForEvent(eventId);
        DefaultTableModel tableModel = (DefaultTableModel) view.getIncomesTable().getModel();
        tableModel.setRowCount(0);
        
        for (ConsultActivityFinancialStatusDTO income : incomes) {
            tableModel.addRow(new Object[]{
                income.getInvoiceNumber(),
                income.getInvoiceAmount(),
                income.getAgreementStatus(),
                income.getInvoiceDate()
            });
        }
    }
    
    private void updateExpensesTable(int eventId) {
        List<ConsultActivityFinancialStatusDTO> expenses = model.getExpensesForEvent(eventId);
        DefaultTableModel tableModel = (DefaultTableModel) view.getExpensesTable().getModel();
        tableModel.setRowCount(0);
        
        for (ConsultActivityFinancialStatusDTO expense : expenses) {
            tableModel.addRow(new Object[]{
                expense.getIncexpConcept(),
                expense.getIncexpAmount(),
                expense.getIncexpStatus(),
                "" // No date in expenses table in new schema
            });
        }
    }
    
    private void updateFinancialSummaries(int eventId) {
        double[] totals = model.calculateTotals(eventId);
        double totalIncome = totals[0];
        double totalExpenses = totals[1];
        double balance = totals[2];
        
        // Get all records for estimated values
        List<ConsultActivityFinancialStatusDTO> sponsorships = model.getSponsorshipsForEvent(eventId);
        List<ConsultActivityFinancialStatusDTO> incomes = model.getIncomesForEvent(eventId);
        List<ConsultActivityFinancialStatusDTO> expenses = model.getExpensesForEvent(eventId);
        
        // Calculate estimated values (include both agreed and paid)
        double estimatedSponsorships = sponsorships.stream()
            .mapToDouble(ConsultActivityFinancialStatusDTO::getAgreementAmount)
            .sum();
        
        double estimatedIncomes = incomes.stream()
            .mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount)
            .sum();
        
        double estimatedExpenses = expenses.stream()
            .mapToDouble(dto -> Math.abs(dto.getIncexpAmount()))
            .sum();
        
        // Update labels
        view.getOverallEstimatedLabel().setText(String.format("Overall Estimated: %.2f (Income: %.2f, Expenses: %.2f)", 
            estimatedSponsorships + estimatedIncomes - estimatedExpenses,
            estimatedSponsorships + estimatedIncomes,
            estimatedExpenses));
            
        view.getOverallPaidLabel().setText(String.format("Overall Paid: %.2f (Income: %.2f, Expenses: %.2f)", 
            balance, totalIncome, totalExpenses));
        
        // Sponsorships section
        view.getSponsorEstimatedLabel().setText(String.format("Estimated: %.2f", estimatedSponsorships));
        view.getSponsorPaidLabel().setText(String.format("Paid: %.2f", 
            sponsorships.stream()
                .filter(dto -> "Paid".equals(dto.getAgreementStatus()))
                .mapToDouble(ConsultActivityFinancialStatusDTO::getAgreementAmount)
                .sum()));
        
        // Incomes section (non-sponsorship)
        view.getIncomesEstimatedLabel().setText(String.format("Estimated: %.2f", estimatedIncomes));
        view.getIncomesPaidLabel().setText(String.format("Paid: %.2f", 
            incomes.stream()
                .filter(dto -> "Paid".equals(dto.getIncexpStatus()))
                .mapToDouble(ConsultActivityFinancialStatusDTO::getIncexpAmount)
                .sum()));
        
        // Expenses section
        view.getExpensesEstimatedLabel().setText(String.format("Estimated: %.2f", estimatedExpenses));
        view.getExpensesPaidLabel().setText(String.format("Paid: %.2f", totalExpenses));
    }
}