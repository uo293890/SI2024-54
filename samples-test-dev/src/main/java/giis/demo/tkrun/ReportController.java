package giis.demo.tkrun;

import java.util.ArrayList;
import java.util.List;

public class ReportController {
    private ReportModel model;
    private ReportView view;

    public ReportController(ReportModel model, ReportView view) {
        this.model = model;
        this.view = view;
        initializeController();
    }

    private void initializeController() {
        updateView();
        view.getGoBackButton().addActionListener(e -> view.dispose());
        view.getConsultButton().addActionListener(e -> updateView());
    }

    private void updateView() {
        String startDate = view.getStartDate();
        String endDate = view.getEndDate();
        String status = view.getStatus();
        
        List<ReportDTO> reports = model.getFinancialReport(startDate, endDate, status);
        List<Object[]> dataActivities = new ArrayList<>();

        for (ReportDTO dto : reports) {
            dataActivities.add(new Object[]{
                dto.getId(),
                dto.getActivityName(),
                dto.getStatus(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getEstimatedIncome(),
                dto.getEstimatedExpenses(),
                dto.getActualIncome(),
                dto.getActualExpenses(),
                dto.getEstimatedBalance(),
                dto.getActualBalance()
            });
        }

        view.updateActivitiesTable(dataActivities);
    }
}
