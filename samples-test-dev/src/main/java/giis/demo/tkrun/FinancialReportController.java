package giis.demo.tkrun;

import giis.demo.util.SwingUtil;
import giis.demo.util.Util;
import java.util.Date;

public class FinancialReportController {
    private FinancialReportModel model;
    private FinancialReportView view;

    public FinancialReportController(FinancialReportModel model, FinancialReportView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getBtnGenerate().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
            String startDate = view.getStartDate();
            String endDate = view.getEndDate();
            String status = view.getSelectedStatus();

            if (!validateDates(startDate, endDate)) {
                SwingUtil.showError("Formato de fecha inválido (YYYY-MM-DD) o rango incorrecto");
                return;
            }

            FinancialReportDTO report = model.generateReport(startDate, endDate, status);
            view.displayReport(report);
        }));
    }

    private boolean validateDates(String start, String end) {
        try {
            Date startDate = Util.isoStringToDate(start);
            Date endDate = Util.isoStringToDate(end);
            return !startDate.after(endDate);
        } catch (Exception e) {
            return false;
        }
    }
}