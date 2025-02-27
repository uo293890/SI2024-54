package giis.demo.jdbc.activities;


import java.util.List;

import giis.demo.tkrun.Activity;
import giis.demo.tkrun.ActivityReport;

public class Reportservice {

    public ActivityReport generateReport(List<Activity> activities, ReportFilter filter) {
        ActivityReport report = new ActivityReport(activities);
        report.calculateTotals();
        return report;
    }
}
