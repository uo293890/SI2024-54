package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ActivityFinancialStatusController {
    private ActivityFinancialStatusModel model;
    private ActivityFinancialStatusView view;

    public ActivityFinancialStatusController(ActivityFinancialStatusModel model, ActivityFinancialStatusView view) {
        this.model = model;
        this.view = view;
        this.initView();
        this.initController();
    }

    private void initView() {
        // Populate the event grid
        List<ActivityFinancialStatusDTO> events = model.getAllEvents();
        view.populateEventGrid(events);
    }

    private void initController() {
        
    }
}