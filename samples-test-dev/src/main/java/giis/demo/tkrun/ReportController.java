package giis.demo.tkrun;

import java.util.List;
import javax.swing.*;
import java.awt.event.*;

public class ReportController {
    private ReportModel model;
    private ReportView view;
    private List<ReportDTO> currentData;

    public ReportController(ReportModel model, ReportView view) {
        this.model = model;
        this.view = view;
        initializeController();
    }

    private void initializeController() {
        refreshReportData();
        view.getGoBackButton().addActionListener(e -> view.dispose());
        view.getConsultButton().addActionListener(e -> refreshReportData());
        view.getExportExcelButton().addActionListener(e -> exportToExcel());
        view.getExportPDFButton().addActionListener(e -> exportToPDF());
        view.getPreviewButton().addActionListener(e -> previewReport());
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
        double totalEstimatedIncome = 0, totalEstimatedExpenses = 0, totalActualIncome = 0, totalActualExpenses = 0;
        for (ReportDTO dto : currentData) {
            totalEstimatedIncome += dto.getEstimatedIncome();
            totalEstimatedExpenses += dto.getEstimatedExpenses();
            totalActualIncome += dto.getActualIncome();
            totalActualExpenses += dto.getActualExpenses();
        }
        double totalEstimatedBalance = totalEstimatedIncome - totalEstimatedExpenses;
        double totalActualBalance = totalActualIncome - totalActualExpenses;
        view.updateTotals(totalEstimatedIncome, totalEstimatedExpenses, totalEstimatedBalance, totalActualIncome, totalActualExpenses, totalActualBalance);
    }

    private void exportToExcel() {
        JOptionPane.showMessageDialog(view, "Funcionalidad de exportación a Excel aún no implementada.", "Exportar Excel", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportToPDF() {
        JOptionPane.showMessageDialog(view, "Funcionalidad de exportación a PDF aún no implementada.", "Exportar PDF", JOptionPane.INFORMATION_MESSAGE);
    }

    private void previewReport() {
        JOptionPane.showMessageDialog(view, "Vista previa del reporte aún no implementada.", "Vista Previa", JOptionPane.INFORMATION_MESSAGE);
    }
}
