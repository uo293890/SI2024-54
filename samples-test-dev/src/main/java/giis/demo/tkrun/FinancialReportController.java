package giis.demo.tkrun;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

public class FinancialReportController {
    private FinancialReportModel model;
    private FinancialReportView view;
    private List<FinancialReportDTO> currentData;

    public FinancialReportController(FinancialReportModel model, FinancialReportView view) {
        this.model = model;
        this.view = view;
        initializeController();
    }

    private void initializeController() {
        setDefaultYear();
        view.getConsultButton().addActionListener(e -> refreshReportData());
    }

    private void setDefaultYear() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        view.setStartDate(currentYear + "-01-01");
        view.setEndDate(currentYear + "-12-31");
        refreshReportData();
    }

    private void refreshReportData() {
        String startDate = view.getStartDate();
        String endDate = view.getEndDate();
        String status = view.getStatus();
        
        currentData = model.getFinancialReport(startDate, endDate, status);
        view.updateActivitiesTable(currentData);
        updateTotals();
    }

    private void updateTotals() {
        double totalEstimatedIncome = 0, totalEstimatedExpenses = 0;
        double totalActualIncome = 0, totalActualExpenses = 0;
        
        for (FinancialReportDTO dto : currentData) {
            totalEstimatedIncome += dto.getEstimatedIncome();
            totalEstimatedExpenses += dto.getEstimatedExpenses();
            totalActualIncome += dto.getActualIncome();
            totalActualExpenses += dto.getActualExpenses();
        }
        double totalEstimatedBalance = totalEstimatedIncome - totalEstimatedExpenses;
        double totalActualBalance = totalActualIncome - totalActualExpenses;
        
        view.updateTotals(totalEstimatedIncome, totalEstimatedExpenses, totalActualIncome, totalActualExpenses, totalActualBalance);
    }
}