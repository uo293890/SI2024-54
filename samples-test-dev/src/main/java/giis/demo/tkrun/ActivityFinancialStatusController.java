package giis.demo.tkrun;

public class ActivityFinancialStatusController {
    private ActivityFinancialStatusModel model;
    private ActivityFinancialStatusView view;

    public ActivityFinancialStatusController(ActivityFinancialStatusModel model, ActivityFinancialStatusView view) {
        this.model = model;
        this.view = view;
    }

    public void updateFinancialStatus(String activityName) {
        ActivityFinancialStatusDTO dto = model.getFinancialStatus(activityName);
        view.displayFinancialStatus(dto);
    }
}