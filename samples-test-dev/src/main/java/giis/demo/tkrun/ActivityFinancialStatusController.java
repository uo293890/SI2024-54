package giis.demo.tkrun;

public class ActivityFinancialStatusController {
    private ActivityFinancialStatusModel model;
    private ActivityFinancialStatusView view;

    public ActivityFinancialStatusController(ActivityFinancialStatusModel model, ActivityFinancialStatusView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Fetches the financial status of an activity.
     */
    public ActivityFinancialStatusDTO getFinancialStatus(int eventId) {
        return model.getFinancialStatus(eventId);
    }
}