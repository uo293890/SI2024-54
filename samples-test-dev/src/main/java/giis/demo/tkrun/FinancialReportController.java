package giis.demo.tkrun;

import giis.demo.util.SwingUtil;

public class FinancialReportController {
    private FinancialReportModel model;
    private FinancialReportView view;

    public FinancialReportController(FinancialReportModel m, FinancialReportView v) {
        this.model = m;
        this.view = v;
        initController();
    }

    public void initController() {
        view.getBtnGenerateReport().addActionListener(e -> SwingUtil.exceptionWrapper(() -> generateReport()));
    }

    private void generateReport() {
        // Obtener los datos de entrada de la vista
        String startDate = view.getStartDate();
        String endDate = view.getEndDate();
        String status = view.getStatus();

        // Generar el reporte usando el modelo
        FinancialReportDTO report = model.generateReport(startDate, endDate, status);

        // Mostrar el reporte en la vista
        view.displayReport(report);
    }
}