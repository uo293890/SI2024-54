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

    private void initController() {
        
    }
    
    private void initView() {
    	List<ActivityFinancialStatusDTO> events = model.getAllEvents();
    	for (ActivityFinancialStatusDTO event : events) {
    		// view.getActivityTable().add
    	}
    
    }
}