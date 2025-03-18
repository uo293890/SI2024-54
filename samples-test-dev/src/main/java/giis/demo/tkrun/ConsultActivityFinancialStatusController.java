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
        // add event listeners
    }
    
    private void initView() {
    	List<ActivityFinancialStatusDTO> activities = model.getAllActivities();
    	Object[][] activityData = new Object[activities.size()][4];
        for (int i = 0; i < activities.size(); i++) {
        	activityData[i][0] = activities.get(i).getEditionId();
        	activityData[i][1] = activities.get(i).getEditionTitle();
        	activityData[i][2] = activities.get(i).getEditionStartDate();
        	activityData[i][3] = activities.get(i).getEditionStatus();
        }
        view.getActivityTable().setModel(new javax.swing.table.DefaultTableModel(activityData, new String[]{"Edition", "Name", "Date", "State"}));
    
    }
}