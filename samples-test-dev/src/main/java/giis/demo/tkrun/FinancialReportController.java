package giis.demo.tkrun;

import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class FinancialReportController {
    private FinancialReportModel model;
    private FinancialReportView view;

    public FinancialReportController(FinancialReportModel m, FinancialReportView v) {
        this.model = m;
        this.view = v;
        this.initView();
    }

    public void initController() {
        view.getBtnGenerateReport().addActionListener(e -> SwingUtil.exceptionWrapper(() -> generateReport()));
    }

    public void initView() {
        view.getFrame().setVisible(true);
    }

    private void generateReport() {
        String startDate = view.getStartDate();
        String endDate = view.getEndDate();
        String status = view.getStatus();
        FinancialReportDTO report = model.generateReport(startDate, endDate, status);
        view.displayReport(report);
    }
}